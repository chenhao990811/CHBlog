package com.chcode.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT = "1";
    /**
     *  文章是正常发布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0";

    /**
     * 分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";

    /**
     * 友链审核通过
     */
    public static final char LINK_STATUS_NORMAL = '0';

    /**
     * 评论为根评论，root_id = -1
     */
    public static final Integer ROOT_COMMENT = -1;
    /**
     * 评论类型为文章 type = "0"
     */
    public static final String COMMENT_TYPE_ARTICLE = "0";

    /**
     * 评论类型为友链 type = "1"
     */
    public static final String COMMENT_TYPE_LINK = "1";

    /**
     * 权限信息表中，权限类型为菜单，C
     */
    public static final String MENU = "C";
    /**
     * 权限信息表中，权限类型为按钮，F
     */
    public static final String BUTTON = "F";
    /**
     *  权限是正常状态
     */
    public static final int STATUS_NORMAL = 0;

    /**
     * 分类是正常状态 status = "0"
     */
    public static final String NORMAL = "0";

    /**
     * 后台用户标志，ch_user表中的 type属性 = "1"
     */
    public static final String ADMAIN = "1";
}