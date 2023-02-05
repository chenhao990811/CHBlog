package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.domain.dto.TagListDto;
import com.chcode.domain.entity.Tag;
import com.chcode.domain.vo.PageVo;
import com.chcode.domain.vo.TagVo;
import com.chcode.mapper.TagMapper;
import com.chcode.service.TagService;
import com.chcode.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-01-08 03:52:24
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }

    @Override
    public ResponseResult updateTag(TagVo tagVo) {
        Tag tag = BeanCopyUtils.copyBean(tagVo, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        // 根据ID获取标签信息
        Tag tag = getById(id);
        // 封装vo返回
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        // yml配置了逻辑删除字段，删除方法会转变为更新
        // UPDATE ch_tag SET del_flag=1 WHERE id=? AND del_flag=0
        getBaseMapper().deleteById(id);
//        getBaseMapper().deleteTag(id); // 自定义sql
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        TagMapper mapper = getBaseMapper();
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        mapper.insert(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName())
                .or()
                .like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        // 将获取到的tagList 转化为 tagVoList
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);

        //封装数据返回
        PageVo pageVo = new PageVo(tagVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult reorderId() {
        TagMapper tagMapper = getBaseMapper();
        tagMapper.reorderId();
        return ResponseResult.okResult();
    }
}


