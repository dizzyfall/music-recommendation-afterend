package com.dzy.mapper;

import com.dzy.model.entity.Song;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DZY
* @description 针对表【song(歌曲表)】的数据库操作Mapper
* @createDate 2024-04-05 13:41:01
* @Entity generator.domain.Song
*/
@Mapper
public interface SongMapper extends BaseMapper<Song> {

}




