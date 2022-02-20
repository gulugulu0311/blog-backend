package com.blog.handler;

import com.alibaba.fastjson.JSON;
import com.blog.pojo.SysUser;
import com.blog.services.LoginService;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.ErrorCode;
import com.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        SysUser user = new SysUser();
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (token == null) {
            log.info("token为null");
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
//        Map<String, Object> map = JWTUtils.checkToken(token);
//        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
//        SysUser user = JSON.parseObject(userJson, SysUser.class);
        // SysUser sysUser = loginService.checkToken(token);
//        if (user == null){
//            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().print(JSON.toJSONString(result));
//            return false;
//        }
        try {
//            Map<String, Object> map = JWTUtils.checkToken(token);
            String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
            user = JSON.parseObject(userJson, SysUser.class);
            if (user == null) {
                log.info("未查询到该用户");
                Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JSON.toJSONString(result));
                return false;
            }
        } catch (Exception e) {
            log.info(e.toString());
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //是登录状态，放行
        UserThreadLocal.put(user);
        log.info("user:{}", user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
