package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "07 上传",description = "上传相关接口")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    /**
     * 3.14 头像上传接口
     * @param img
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "头像上传",notes = "头像上传")
    @ApiImplicitParam(name = "img",value = "上传的图片，需要是jpg，png格式")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
