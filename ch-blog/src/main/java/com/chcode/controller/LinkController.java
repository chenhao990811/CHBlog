package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@Api(tags = "05 友链",description = "友链相关接口")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 3.5 友联查询
     * 功能：在友链页面要查询出所有的审核通过的友链。
     */
    @ApiOperation(value = "友联查询",notes = "友链页面要查询出所有的审核通过的友链")
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
