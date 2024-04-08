package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.Album;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【album(专辑表)】的数据库操作Mapper
 * @createDate 2024-04-07 13:26:21
 * @Entity generator.domain.Album
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

}




