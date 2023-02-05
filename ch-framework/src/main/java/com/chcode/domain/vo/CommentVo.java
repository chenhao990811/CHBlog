package com.chcode.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;
    //文章id
    private Long articleId;
    //根评论id,-1代表本身就是根评论
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid,如果是根评论则为-1
    private Long toCommentUserId;
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;

    private Long createBy;

    private Date createTime;

    private String username;

    // 子评论
    private List<CommentVo> children;
}
