package com.blog.controller;

import com.blog.common.aop.LogAnnotation;
import com.blog.services.LoginService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogoutController {
    @Autowired
    private LoginService loginService;

    @GetMapping
    @LogAnnotation(module = "登出", operator = "用户登出")
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }
}
