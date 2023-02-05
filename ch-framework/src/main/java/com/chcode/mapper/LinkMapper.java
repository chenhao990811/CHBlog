package com.chcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chcode.domain.entity.Link;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-21 21:29:40
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}

