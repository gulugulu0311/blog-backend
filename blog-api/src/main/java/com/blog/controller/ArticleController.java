package com.blog.controller;

import com.blog.common.aop.LogAnnotation;
import com.blog.common.cache.Cache;
import com.blog.services.ArticleService;
import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页   文章列表
     *
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章", operator = "获取文章列表")
//    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    @PostMapping("new")
    public Result newArticle() {
        int limit = 3;
        return articleService.newArticle(limit);
    }

    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result view(@PathVariable("id") Long id) {
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

}
