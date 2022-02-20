package com.blog.controller;

import com.blog.common.cache.Cache;
import com.blog.services.CommentsService;
import com.blog.vo.Result;
import com.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
//    @Cache(expire = 5 * 60 * 1000, name = "comment")
    public Result comments(@PathVariable("id") Long id) {
        return commentsService.commentsByArticleId(id);
    }

    @PostMapping("create/change")
//    @Cache(expire = 5 * 60 * 1000, name = "comment_change")
    public Result comment(@RequestBody CommentParam commentParam) {
        return commentsService.comment(commentParam);
    }
}
