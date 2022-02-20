package com.blog.services;

import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     *
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章查询
     *
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章查询
     *
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     * 文章归档
     *
     * @return
     */
    Result listArchives();

    /**
     * 通过id查询ArticleBody
     *
     * @param id
     * @return
     */
    ArticleVo findArticleById(Long id);

    /**
     * 发布文章
     *
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
