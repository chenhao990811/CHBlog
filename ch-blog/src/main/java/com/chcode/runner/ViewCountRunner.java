package com.chcode.runner;

import com.chcode.domain.entity.Article;
import com.chcode.mapper.ArticleMapper;
import com.chcode.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ①在应用启动时把博客的浏览量存储到redis中
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    // 项目启动预处理，从数据库中查询出文章信息，并封装到redis中，存入redis中的数据格式{article_id,view_count}
    @Override
    public void run(String... args) throws Exception {
        //查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String,Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue(); // long类型参数在redis中无法递增，因此转为Integer
                }));
        //存储到redis中， TODO "article:viewCount" 定义为常量
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
