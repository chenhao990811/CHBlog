package com.chcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chcode.ResponseResult;
import com.chcode.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-08 03:52:21
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    void reorderId();

    void deleteTag(Long id);
}

