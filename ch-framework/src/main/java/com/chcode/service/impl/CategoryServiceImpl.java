package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.Article;
import com.chcode.domain.entity.Category;
import com.chcode.domain.vo.CategoryVo;
import com.chcode.domain.vo.CategoryVo2;
import com.chcode.domain.vo.PageVo;
import com.chcode.mapper.CategoryMapper;
import com.chcode.service.ArticleService;
import com.chcode.service.CategoryService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Override
    public ResponseResult getCategoryById(Long id) {
        Category byId = getById(id);
        CategoryVo2 categoryVo2 = BeanCopyUtils.copyBean(byId, CategoryVo2.class);
        return ResponseResult.okResult(categoryVo2);
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        // 构建Wrapper
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Category::getName,name);
        wrapper.eq(StringUtils.hasText(status),Category::getStatus,status);

        // 分页，封装VO返回
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        List<Category> records = page.getRecords();
        List<CategoryVo2> categoryVo2s = BeanCopyUtils.copyBeanList(records, CategoryVo2.class);
        PageVo pageVo = new PageVo(categoryVo2s, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private ArticleService articleService;
    // ①要求只展示有发布正式文章的分类（连表ch_article表的category_id属性）
    // ②必须是正常状态的分类
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章(status=0)
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重（Stream流）
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream() .
                filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories,CategoryVo.class);
        // 自己实现SQL
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        // 注意：查询条件，状态为正常
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> categoryList = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}
