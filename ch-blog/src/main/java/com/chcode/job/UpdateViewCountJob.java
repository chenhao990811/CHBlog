package com.chcode.job;

import com.chcode.domain.entity.Article;
import com.chcode.service.ArticleService;
import com.chcode.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ③定时任务每隔10分钟把Redis中的浏览量更新到数据库中
@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService; // 批量更新

    @Scheduled(cron = "0/30 * * * * ?")
    public void updateViewCount(){
        System.out.println("定时任务 —— 每隔三十秒更新一次文章浏览量");
        // 获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),entry.getValue().longValue()))
                .collect(Collectors.toList());

        // 更新到数据库中
        articleService.updateBatchById(articles);
    }
}
