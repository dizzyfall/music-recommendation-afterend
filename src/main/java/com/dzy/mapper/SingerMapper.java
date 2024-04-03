package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.Singer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @description 针对表【singer(歌手表)】的数据库操作Mapper
 * @createDate 2024-03-10 11:42:01
 * @Entity generator.domain.Singer
 */
@Mapper
public interface SingerMapper extends BaseMapper<Singer> {

}




