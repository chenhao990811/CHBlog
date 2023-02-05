package com.chcode.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticleListVo {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 所属分类id
     */
    private Long categoryId;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;
    /**
     * 状态（0已发布，1草稿）
     */
    private String status;
    /**
     * 访问量
     */
    private Long viewCount;
    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

//    @TableField(fill = FieldFill.INSERT)
//    private Long createBy;
//    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private Long updateBy;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
//    private Integer delFlag;

    // 此注解说明categoryName属性在数据库表中是不存在的
//    @TableField(exist = false)
//    private String categoryName;
}
