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
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    @LogAnnotation(module = "注册", operator = "用户注册")
    public Result register(@RequestBody LoginParams loginParams) {
        return loginService.register(loginParams);
    }
}
