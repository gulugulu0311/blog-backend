package com.blog.services.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mapper.SysUserMapper;
import com.blog.pojo.SysUser;
import com.blog.services.SysUserService;
import com.blog.utils.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.LoginUserVo;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public SysUser findUserByAuthorId(Long authorId) {
        SysUser sysUser = userMapper.selectById(authorId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("游客");
        }
        return sysUser;
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = userMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("游客");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
//        userVo.setId(String.valueOf(sysUser.getId()));
        userVo.setId(sysUser.getId());
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String password) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account)
                .eq("password", password)
                .select("account", "id", "avatar", "nickname")
                .last("limit 1");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Result getUserInfoByToken(String token) {


        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        SysUser user = JSON.parseObject(userJson, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
//        loginUserVo.setId(String.valueOf(user.getId()));
        loginUserVo.setId(user.getId());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account)
                .last("limit 1");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        this.userMapper.insert(sysUser);
    }
}
