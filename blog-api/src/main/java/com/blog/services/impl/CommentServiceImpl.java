package com.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mapper.CommentMapper;
import com.blog.pojo.Comment;
import com.blog.pojo.SysUser;
import com.blog.services.CommentsService;
import com.blog.services.SysUserService;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.CommentVo;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import com.blog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Result commentsByArticleId(Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", id)
                .eq("level", 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return Result.success(copylist(comments));
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser user = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(user.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copylist(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    @Autowired
    private SysUserService userService;

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
//        commentVo.setId(String.valueOf(comment.getId()));
        commentVo.setId(comment.getId());
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        Long authorId = comment.getAuthorId();
        UserVo userVo = userService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        Integer level = comment.getLevel();
        if (level == 1) {
            //评论的评论
            List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
            commentVo.setChildrens(commentVoList);
        }
        if (level > 1) {
            // 给谁评论
            UserVo toUserVo = this.userService.findUserVoById(comment.getToUid());
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id)
                .eq("level", 2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copylist(comments);
    }
}
