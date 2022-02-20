package com.blog.services;

import com.blog.vo.Result;
import com.blog.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 通过文章id找到评论
     *
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     * 评论功能
     *
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
