package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.Song;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Mapper
 * @createDate 2024-04-03 11:40:52
 * @Entity generator.domain.Song
 */
@Mapper
public interface SongMapper extends BaseMapper<Song> {

}




