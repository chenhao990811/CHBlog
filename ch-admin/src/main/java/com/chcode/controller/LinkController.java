package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.entity.Link;
import com.chcode.domain.vo.LinkVo2;
import com.chcode.service.LinkService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;


    /**
     * 5.34 删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 5.33.2.2 修改友链
     * @param linkVo2
     * @return
     */
    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkVo2 linkVo2){
        Link link = BeanCopyUtils.copyBean(linkVo2, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
     * 5.33.2.1 根据id查询友联
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult selectLinkById(@PathVariable Long id){
        Link byId = linkService.getById(id);
        LinkVo2 linkVo2 = BeanCopyUtils.copyBean(byId, LinkVo2.class);
        return ResponseResult.okResult(linkVo2);
    }

    /**
     * 5.31 分页查询友链列表
     * 需要分页查询友链列表。
     *    能根据友链名称进行模糊查询。
     *    能根据状态进行查询。
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.listLink(pageNum,pageSize,name,status);
    }
}
