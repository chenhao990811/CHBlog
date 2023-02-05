package com.chcode.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.chcode.ResponseResult;
import com.chcode.domain.entity.Category;
import com.chcode.domain.vo.CategoryVo;
import com.chcode.domain.vo.CategoryVo2;
import com.chcode.domain.vo.CategoryVo3;
import com.chcode.domain.vo.ExcelCategoryVo;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.service.CategoryService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 5.30 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 5.29.2.2 更新分类
     * @param categoryVo2
     * @return
     */
    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryVo2 categoryVo2){
        Category category = BeanCopyUtils.copyBean(categoryVo2, Category.class);
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    /**
     * 5.29.2.1 根据id查询分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    /**
     * 5.28 新增分类
     * @param categoryVo3
     * @return
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryVo3 categoryVo3){
        Category category = BeanCopyUtils.copyBean(categoryVo3, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    /**
     * 5.27 分页查询分类列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listCategory(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.listCategory(pageNum,pageSize,name,status);
    }


    /**
     * 5.9 导出所有分类到Excel
     * // 文件写入了response中，因此不能再用response返回（json），因此方法返回值是void
     * @param response
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryList = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(),ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (IOException e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 5.8.2.1 查询所有分类接口
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
}
