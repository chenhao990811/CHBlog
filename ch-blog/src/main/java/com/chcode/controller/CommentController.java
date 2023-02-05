package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.dto.AddCommentDto;
import com.chcode.domain.entity.Comment;
import com.chcode.service.CommentService;
import com.chcode.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "04 评论",description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 3.10 查询评论列表接口
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "查询评论列表",notes = "查询文章对应的评论列表")
    @ApiImplicitParam(name = "articleId",value = "文章id")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_ARTICLE,articleId,pageNum,pageSize);
    }

    /**
     * 3.12 友联评论列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "友联评论列表",notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",paramType = "Integer")
    })
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_LINK,null,pageNum,pageSize);
    }

    /**
     * 3.11 发表评论接口
     * @param addCommentDto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "发表评论",notes = "注意评论间的父子关系")
    @ApiImplicitParam(name = "addCommentDto",value = "评论相关信息")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }
}
