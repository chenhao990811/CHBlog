package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.dto.ArticleDto;
import com.chcode.domain.vo.AdminArticleDetailVo;
import com.chcode.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class AdminArticleController {
    @Autowired
    private ArticleService articleService;


    /**
     * 5.13 删除文章
     * 注意：是逻辑删除不是物理删除（yml配置，Article中添加注解`@TableLogic`）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable Long id){
//        return articleService.deleteArticleById(id);
        articleService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 5.12.3.2 更新文章接口
     * @param articleDetailVo
     * @return
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody AdminArticleDetailVo articleDetailVo){
        return articleService.updateArticle(articleDetailVo);
    }

    /**
     * 5.12.3.1 查询文章详情接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult selectArticleDetail(@PathVariable("id") Long id){
        return articleService.selectArticleDetail(id);
    }

    /**
     * 5.11 文章列表
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.listArticle(pageNum,pageSize,title,summary);
    }

    /**
     * 5.8.3.4 新增博文接口
     * 细节：
     * 01 文章标签，一对多，ch_article_tag表(如何获取刚插入文章的ID，与标签ID对应)
     * 02 文章的保存与文章标签对应关系的保存应当同时成功/失败（事务）
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody ArticleDto articleDto){
        return articleService.add(articleDto);
    }
}
