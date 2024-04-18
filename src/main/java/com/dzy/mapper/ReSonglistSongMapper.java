package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.ReSonglistSong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【re_songlist_song】的数据库操作Mapper
 * @createDate 2024-04-12 11:54:16
 * @Entity generator.domain.ReSonglistSong
 */
@Mapper
public interface ReSonglistSongMapper extends BaseMapper<ReSonglistSong> {

    /**
     * 通过歌单id列表批量删除歌单-歌曲关联表数据
     *
     * @param creatorId
     * @param songlistIds
     * @return
     */
    Boolean deleteBatchBySonglistIds(Long creatorId, @Param("SonglistIds") List<Long> songlistIds);

}




