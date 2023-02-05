package com.chcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chcode.ResponseResult;
import com.chcode.domain.entity.Category;

public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult getCategoryById(Long id);
}
