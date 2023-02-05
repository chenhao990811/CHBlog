package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.dto.TagListDto;
import com.chcode.domain.entity.Tag;
import com.chcode.domain.vo.PageVo;
import com.chcode.domain.vo.TagVo;
import com.chcode.service.TagService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 5.8.2.2 查询所有标签接口
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> tagVos = tagService.listAllTag();
        return ResponseResult.okResult(tagVos);
    }

    /**
     * 5.7.1.2 修改标签接口
     * @param tagVo
     * @return
     */
    @PutMapping
    public ResponseResult updateTag(@RequestBody TagVo tagVo){
        return tagService.updateTag(tagVo);
    }

    /**
     * 5.7.1.1 获取标签信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }

    /**
     * 5.6 删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    /**
     * 5.5 新增标签
     * @param tagListDto
     * @return
     */
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    /**
     * 5.4 查询标签列表(通过名字查询)
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    /**
     * id重排序
     * @return
     */
//    @PutMapping("/reorderId")
//    public ResponseResult reorderId(){
//        return tagService.reorderId();
//    }

//    @GetMapping("list")
//    public ResponseResult list(){
//        return ResponseResult.okResult(tagService.list());
//    }
}
