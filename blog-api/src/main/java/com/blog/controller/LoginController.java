package com.blog.controller;

import com.blog.common.aop.LogAnnotation;
import com.blog.services.LoginService;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    @LogAnnotation(module = "登录", operator = "用户登录")
    public Result login(@RequestBody LoginParams loginParams) {
        return loginService.login(loginParams);
    }
}
