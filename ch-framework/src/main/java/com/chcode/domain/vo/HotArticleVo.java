package com.chcode.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo {
    // 文章的id，用于跳转
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
