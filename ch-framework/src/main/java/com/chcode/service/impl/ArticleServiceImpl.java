package com.chcode.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.dto.ArticleDto;
import com.chcode.domain.entity.Article;
import com.chcode.domain.entity.ArticleTag;
import com.chcode.domain.entity.Category;
import com.chcode.domain.vo.*;
import com.chcode.mapper.ArticleMapper;
import com.chcode.mapper.CategoryMapper;
import com.chcode.service.ArticleService;
import com.chcode.service.ArticleTagService;
import com.chcode.service.CategoryService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;



    @Override
    @Transactional
    public ResponseResult updateArticle(AdminArticleDetailVo articleDetailVo) {
        System.out.println("============================更新接口测试================================");
        // 更新文章表内容（转化vo ==》article）
        Article updateArticle = BeanCopyUtils.copyBean(articleDetailVo, Article.class);
        // TODO MyBatis自动填充失效（debug进入了MyMetaObjectHandler方法）
        updateById(updateArticle);

        Long articleId = articleDetailVo.getId();
        // 更新文章-标签关系表内容（转化vo ==》List<articleTag>）
        List<Long> tagsFromPage = articleDetailVo.getTags();  // 提交更新页面中的tags
        List<Long> tagsFromDatabase = getTagsFromDatabase(articleId); // 数据库中的tags

        /** 更新文章-标签关系表内容：（判断，tags发生变化才更新）
         *      方案一：删除所有文章对应的articleTag，然后保存新的tag（实际使用）
         *      TODO 方案二：根据tags的具体变化，进行articleTag的增删 （待扩展）
         */
        if (!ObjectUtils.isEmpty(tagsFromPage) && ObjectUtils.isEmpty(tagsFromDatabase)){
            // 数据库中，新增article_tag关系
            insertArticleTag(articleId,tagsFromPage);
        }else if (ObjectUtils.isEmpty(tagsFromPage) && !ObjectUtils.isEmpty(tagsFromDatabase)){
            // 数据库中，删除article_tag关系
            deleteArticleTagByarticleId(articleId);
        }else if (ObjectUtils.isEmpty(tagsFromPage) && ObjectUtils.isEmpty(tagsFromDatabase)){
            // tags无变化，直接返回
            return ResponseResult.okResult();
        }
        if (isTagsChange(tagsFromPage,tagsFromDatabase)){
            // 删除数据库中 articleId 对应的记录
            deleteArticleTagByarticleId(articleId);
            // article_tag表中，新增 tagsFromPage
            insertArticleTag(articleId,tagsFromPage);
        }
        return ResponseResult.okResult();
    }

    // article_tag表中，新增 tagsFromPage
    private void insertArticleTag(Long articleId, List<Long> tagsFromPage){
        List<ArticleTag> articleTags = alterToArticleTagList(articleId, tagsFromPage);
        articleTagService.saveBatch(articleTags);
    }

    // 从article_tag表中，删除articleId对应的记录
    private void deleteArticleTagByarticleId(Long articleId){
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,articleId);
        articleTagService.getBaseMapper().delete(wrapper);
    }

    // 根据articleId，tags集合，生成List<ArticleTag>
    private List<ArticleTag> alterToArticleTagList(Long articleId,List<Long> tags){
        List<ArticleTag> articleTagList = tags.stream()
                .map(tag -> new ArticleTag(articleId, tag))
                .collect(Collectors.toList());
        return articleTagList;
    }

    /**
     * 先将tags集合，转化为List<String>
     * 使用list自带的sort方法先进性排序，然后转成toString去判断两个集合是否相等
     *
     * @param tags List<Long>
     * @param tags2 List<String>
     * @return true:tags改变；false：tags未改变 （改变了则需要更新文章-标签关联表）
     */
    private boolean isTagsChange(List<Long> tags,List<Long> tags2){
        List<String> tagsStringList = changeToStringList(tags);
        List<String> tags2StringList = changeToStringList(tags2);

        tagsStringList.sort(Comparator.comparing(String::hashCode));
        tags2StringList.sort(Comparator.comparing(String::hashCode));

        return !tagsStringList.toString().equals(tags2StringList.toString());
    }

    // 获取数据库中文章id对应的tags_id集合
    private List<Long> getTagsFromDatabase(Long id){
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(wrapper);
        List<Long> tags2 = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());  // 数据库中的tags
        return tags2;
    }

    // List<Long> 集合转化为 List<String>
    private List<String> changeToStringList(List<Long> list){
        List<String> collect = list.stream()
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        return aLong.toString();
                    }
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ResponseResult selectArticleDetail(Long id) {
        // 根据id查询文章信息，封装vo
        Article article = getBaseMapper().selectById(id);
        AdminArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, AdminArticleDetailVo.class);

        // 查询出文章的标签列表并注入vo
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(wrapper);
        List<Long> tags = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        articleDetailVo.setTags(tags);

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary) {
        // 编写查询条件(注意：不能把删除了的文章查询出来)
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title),Article::getTitle,title)
                .or(StringUtils.hasText(title)&&StringUtils.hasText(summary))
                .like(StringUtils.hasText(summary),Article::getSummary,summary);
        wrapper.orderByAsc(Article::getId);
        // 分页
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        List<Article> articleList = page.getRecords();
        // 封装vo返回
        List<AdminArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, AdminArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult add(ArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article); // save后，会将id赋值给article对象

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 3.18 更新浏览次数
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    /**
     * 3.4 文章详情
     * @param id
     * @return 分类id，分类名，文章内容，创建日期，文章id，是否允许评论，文章标题，浏览量
     * 要求：①要在文章详情中展示其分类名
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 查询出id对应的文章信息
        Article article = this.getById(id);

        //从redis中获取viewCount ④读取文章浏览量时从redis读取
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        // 在文章详情中展示其分类名
//        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());

        // 封装Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        // 在文章详情中展示其分类名（分类非空判断）
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        if (category != null){
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 3.3 分页查询文章列表 TODO 业务逻辑错误，待修复
     * @param pageNum 分页，页码
     * @param pageSize 分页，每页大小
     * @param categoryId 0：主页查询文章列表(所有分类的文章都要)；
     *                      任意数据库中存在的分类id：分类查询文章列表；
     *                      其他：错误
     * @return 标题，发布时间，浏览量，摘要，缩略图，文章id（阅读全文跳转），所属分类名（根据categoryId查询）
     * 	要求：①只能查询正式发布的文章(status=0) ②置顶的文章要显示在最前面 (is_top=1)
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 构造查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(categoryId)&&categoryId.equals(0L)){
            // 查询所有分类下的文章
        }else{
            // 查询categoryId对应分类下的文章
            queryWrapper.eq(!ObjectUtils.isEmpty(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        }
        // ①只能查询正式发布的文章(status=0)
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // ②置顶的文章要显示在最前面 (is_top=1)
        queryWrapper.orderByDesc(Article::getIsTop);

        // 分页
        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        page(articlePage,queryWrapper);

        // 返回前端的数据集合
        List<Article> articles = articlePage.getRecords();
        // 配置articles中的categoryName属性
        List<Article> collect = articles.stream()
                .map(article -> article.setCategoryName(getCategoryNameById(article.getCategoryId())))
                .collect(Collectors.toList());

        // 封装成Vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(collect, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    // 通过categoryId，获取CategoryName
    private String getCategoryNameById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        return category.getName();
    }


    /**
     * 3.1 热门文章列表
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章（status=0,已发布）
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序（view_count）
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条（未添加分页拦截器前，分页功能不生效）
        Page<Article> page = new Page<>(1, 10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        //bean拷贝（原理：字段名和字段类型一致） entity ==> Vo
//        List<HotArticleVo> hotArticleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            hotArticleVos.add(vo);
//        }
        // 使用BeanCopyUtils工具类
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }
}
