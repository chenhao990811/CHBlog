package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.entity.Article;
import com.chcode.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
@RestController
@RequestMapping("/article")
@Api(tags = "01 文章",description = "文章相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 3.18 更新浏览次数
     * @return
     * ②更新浏览量时去更新redis中的数据
     */
    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新浏览次数",notes = "使用定时任务，每次更新浏览量时去更新redis中的数据")
    @ApiImplicitParam(name = "id",value = "文章主键id")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

    /**
     * 3.4 文章详情接口
     * 功能：要求在文章列表点击阅读全文时能够跳转到文章详情页面，可以让用户阅读文章正文。
     * 要求：①要在文章详情中展示其分类名
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取文章详情",notes = "要在文章详情中展示其分类名")
    @ApiImplicitParam(name = "id",value = "文章主键id")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 3.3 分页查询文章列表
     * 功能：在首页和分类页面都需要查询文章列表。
     *      首页：查询所有的文章（传入参数categoryId=0）
     *      分类页面：查询对应分类下的文章（传入分类的id）
     * 要求：①只能查询正式发布的文章(status=0) ②置顶的文章要显示在最前面 (is_top=1)
     */
    @GetMapping("/articleList")
    @ApiOperation(value = "分页查询文章列表",notes = "只能查询正式发布的文章(status=0) 置顶的文章要显示在最前面 (is_top=1)")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        ResponseResult result = articleService.articleList(pageNum,pageSize,categoryId);
        return result;
    }

    /**
     * 3.1 热门文章列表
     * 功能：需要查询浏览量最高的前10篇文章的信息（分页）。
     *      要求展示文章标题和浏览量（Vo：titile,view_count）
     *      能让用户自己点击跳转到具体的文章详情进行浏览（Vo：id）
     * ResponseResult中封装HotArticleVo：（id,titile,view_count）
     * 注意：不能把草稿展示出来，不能把删除了的文章查询出来（status=0,已发布）。
     *      要按照浏览量进行降序排序（view_count）
     * @return
     */
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "获取热门文章列表",notes = "不能把草稿展示出来（status=0,已发布），不能把删除了的文章查询出来（delFlag=0），按照浏览量进行降序排序")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

//    @GetMapping("/list")
//    public List<Article> test(){
//        return articleService.list();
//    }
}
