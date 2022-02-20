package com.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.mapper.ArticleBodyMapper;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.pojo.Article;
import com.blog.pojo.ArticleBody;
import com.blog.pojo.ArticleTag;
import com.blog.pojo.SysUser;
import com.blog.pojo.dos.Archives;
import com.blog.services.*;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.*;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        // 分页查询article 数据库表
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        QueryWrapper<Article> wrapper = new QueryWrapper<>();
//        // category id查询
//        if (pageParams.getCategoryId() != null) {
//            wrapper.eq("category_id", pageParams.getCategoryId());
//        }
//        // tag id查询
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null) {
//            QueryWrapper<ArticleTag> tagQueryWrapper = new QueryWrapper<>();
//            tagQueryWrapper.eq("tag_id", pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(tagQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                wrapper.in("id", articleIdList);
//            }
//        }
//        //LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        // 是否置顶排序
//        // order by create_date desc
//        //queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        wrapper.orderByDesc("weight").orderByDesc("create_date");
//        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
//        List<Article> records = articlePage.getRecords();
//
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        //return Result.success(articleVoList);
//        return Result.success(articleVoList);
//    }
//

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(), true, true));
    }

    @Override
    public Result hotArticle(int limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper
                .orderByDesc("view_counts")
                .select("id", "title")
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper
                .orderByDesc("create_date")
                .select("id", "title")
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        threadService.updateViewCount(articleMapper, article);
        return copy(article, true, true, true, true);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser user = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(user.getId());
//        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);
        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
//                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(String.valueOf(article.getId()));
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        for (Article record : records) {
            articleVos.add(copy(record, true, true, false, false));
        }
        return articleVos;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(String.valueOf(article.getId()));
        articleVo.setId(article.getId());
        BeanUtils.copyProperties(article, articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 并不是所有的接口都需要作者信息或标签
        if (isTag) {
            Long id = article.getId();
            articleVo.setTags(tagService.findTagByArticleId(id));
        }
        if (isAuthor) {
            Long id = article.getAuthorId();
            articleVo.setAuthor(userService.findUserByAuthorId(id).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }

    @Autowired
    private CategoryService categoryService;

    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @Autowired
    private ArticleBodyMapper bodyMapper;

    private ArticleBodyVo findArticleBody(Long id) {
        QueryWrapper<ArticleBody> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", id);
        ArticleBody articleBody = bodyMapper.selectOne(wrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
