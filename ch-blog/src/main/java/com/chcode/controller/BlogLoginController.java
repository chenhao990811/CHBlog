package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.entity.User;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.exception.SystemException;
import com.chcode.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.awt.geom.RectangularShape;

@RestController
@Api(tags = "03 登录",description = "登录相关接口")
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    /**
     * 3.6 登录功能实现
     * @param user
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录功能",notes = "用户输入用户名，密码进行登录")
    @ApiImplicitParam(name = "user",value = "用户名，密码")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            // 提示：必须传用户名（统一异常处理）
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    /**
     * 3.9 退出登录接口
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录",notes = "清除redis中的用户信息")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
