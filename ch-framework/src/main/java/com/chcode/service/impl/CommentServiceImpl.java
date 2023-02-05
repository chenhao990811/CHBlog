package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.Comment;
import com.chcode.domain.vo.CommentVo;
import com.chcode.domain.vo.PageVo;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.exception.SystemException;
import com.chcode.mapper.CommentMapper;
import com.chcode.service.CommentService;
import com.chcode.service.UserService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-21 21:29:40
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空（Mybatis-Plus自动填充）
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // TODO 敏感词处理
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        // 评论类型
        wrapper.eq(Comment::getType,commentType);
        // 对articleId进行判断（对应文章的评论）,只有当评论类型为文章时，才需要此条件（commentType不能写前面，否则空指针异常）
        wrapper.eq(SystemConstants.COMMENT_TYPE_ARTICLE.equals(commentType),Comment::getArticleId,articleId);
        // 根评论 rootId为-1
        wrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);

        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        // 转化为vo
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            // 查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            // 赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        //
        wrapper.eq(Comment::getRootId,id);
        wrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(wrapper);

        // 封装vo
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询（为-1则是根评论）
            if (commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}


