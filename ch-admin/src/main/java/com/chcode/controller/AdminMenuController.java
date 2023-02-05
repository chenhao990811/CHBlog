package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.entity.Menu;
import com.chcode.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class AdminMenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 5.17 删除菜单
     * 注意：如果要删除的菜单有子菜单，则不允许删除
     * @param id
     * @return
     */
    @DeleteMapping("system/menu/{id}")
    public ResponseResult deleteMenuById(@PathVariable Long id){
//        menuService.removeById(id);
//        return ResponseResult.okResult();
        return menuService.deleteMenuById(id);
    }

    /**
     * 5.16.2.2 更新菜单
     * 注意：不能把父菜单设置为当前菜单
     * @param menu
     * @return
     */
    @PutMapping("system/menu")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 5.16.2.1 根据id查询菜单数据
     * @param id
     * @return
     */
    @GetMapping("system/menu/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    /**
     * 5.15 新增菜单
     * @param menu
     * @return
     */
    @PostMapping("system/menu")
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 5.14 菜单列表
     * 菜单要按照父菜单id和orderNum进行排序
     * @return
     */
    @GetMapping("system/menu/list")
    public ResponseResult listMenu(String menuName,String status){
        return menuService.listMenu(menuName,status);
    }
}
