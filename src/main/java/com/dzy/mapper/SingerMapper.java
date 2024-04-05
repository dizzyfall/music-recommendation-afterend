package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.Singer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【singer(歌手表)】的数据库操作Mapper
 * @createDate 2024-04-05 13:35:40
 * @Entity generator.domain.Singer
 */
@Mapper
public interface SingerMapper extends BaseMapper<Singer> {

}




