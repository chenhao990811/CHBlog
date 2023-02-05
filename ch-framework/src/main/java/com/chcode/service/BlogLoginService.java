package com.chcode.service;

import com.chcode.ResponseResult;
import com.chcode.domain.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
