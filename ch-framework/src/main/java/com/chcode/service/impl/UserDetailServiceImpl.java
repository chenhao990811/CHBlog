package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.LoginUser;
import com.chcode.domain.entity.User;
import com.chcode.mapper.MenuMapper;
import com.chcode.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// 查询数据库中的用户信息，与登录时输入的用户信息进行比较
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);

        //判断是否查到用户  如果没查到抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        //返回用户信息
        // TODO 查询权限信息封装（后台用户才需要ch_user表中的type属性）
        if (user.getType().equals(SystemConstants.ADMAIN)){
            List<String> menuList = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,menuList);
        }

        return new LoginUser(user,null);
    }
}
