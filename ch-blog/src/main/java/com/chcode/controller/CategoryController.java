package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.service.ArticleService;
import com.chcode.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags = "02 分类",description = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 3.2 查询分类列表
     * 功能：页面上需要展示分类列表，用户可以点击具体的分类查看该分类下的文章列表
     * ResponseResult中封装Vo，CategoryVo：分类id【跳转】，名称【展示】
     * 注意： ①要求只展示有发布正式文章的分类（连表，ch_article表的category_id属性）
     *      ②必须是正常状态的分类（status=0）
     * @return
     */
    @GetMapping("/getCategoryList")
    @ApiOperation(value = "查询分类列表",notes = "页面上需要展示分类列表，用户可以点击具体的分类查看该分类下的文章列表")
    public ResponseResult getCategoryList(){
        ResponseResult result = categoryService.getCategoryList();
        return result;
    }
}
