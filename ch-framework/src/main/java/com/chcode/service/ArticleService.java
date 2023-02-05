package com.chcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chcode.ResponseResult;
import com.chcode.domain.dto.ArticleDto;
import com.chcode.domain.entity.Article;
import com.chcode.domain.vo.AdminArticleDetailVo;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();


    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(ArticleDto articleDto);

    ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult selectArticleDetail(Long id);

    ResponseResult updateArticle(AdminArticleDetailVo articleDetailVo);

}
