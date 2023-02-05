package com.chcode;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chcode.domain.entity.ArticleTag;
import com.chcode.service.ArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
public class AppTest {
    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 判断两个集合的元素是否完全相等
     */
//    @Test
//    public void testListComparing(){
//        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ArticleTag::getArticleId,9L);
//        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(wrapper);
//        List<String> collect = articleTags.stream()
//                .map(articleTag -> articleTag.getTagId())
//                .map(tag -> tag.toString())
//                .collect(Collectors.toList());
//
//
//
//        List<Long> tags = new ArrayList<>();
//        tags.add(4L);
//        tags.add(5L);
//        List<String> collect1 = tags.stream()
//                .map(tag -> tag.toString())
//                .collect(Collectors.toList());
//
//        collect.sort(Comparator.comparing(String::hashCode));
//        collect1.sort(Comparator.comparing(String::hashCode));
//        System.out.println(collect.toString().equals(collect1.toString()));
//    }
}
