package com.chcode.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    // 分类id
    private Long id;
    // 分类名
    private String name;
    // 描述
    private String description;
}
