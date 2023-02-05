package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadImgController {

    @Autowired
    private UploadService uploadService;

    /**
     * 5.8.3.3 上传图片接口
     * @param img
     * @return
     */
    @PostMapping
    public ResponseResult upload(@RequestParam("img") MultipartFile img){
        try {
            return uploadService.uploadImg(img);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
