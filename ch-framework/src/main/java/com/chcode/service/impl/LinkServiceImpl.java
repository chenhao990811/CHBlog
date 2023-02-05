package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.Link;
import com.chcode.domain.vo.LinkVo;
import com.chcode.domain.vo.LinkVo2;
import com.chcode.domain.vo.PageVo;
import com.chcode.mapper.LinkMapper;
import com.chcode.service.LinkService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-12-21 21:29:40
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status) {
        // 构建查询条件wrapper
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Link::getName,name); // 根据友链名称进行模糊查询
        wrapper.eq(StringUtils.hasText(status),Link::getStatus,status); // 根据状态进行查询

        // 封装pageVo返回
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<Link> records = page.getRecords();
        List<LinkVo2> linkVo2s = BeanCopyUtils.copyBeanList(records, LinkVo2.class);
        PageVo pageVo = new PageVo(linkVo2s, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 3.5 友联查询
     * 功能：在友链页面要查询出所有的审核通过的友链
     * @return
     */
    @Override
    public ResponseResult getAllLink() {
        // 构造查询条件，需要审核通过
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);

        // 查询出所有满足条件的link
        List<Link> links = this.list(wrapper);

        // 转化Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);

        return ResponseResult.okResult(linkVos);
    }
}


