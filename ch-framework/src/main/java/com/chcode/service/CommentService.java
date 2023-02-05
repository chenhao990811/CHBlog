package com.chcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-12-21 21:29:39
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

