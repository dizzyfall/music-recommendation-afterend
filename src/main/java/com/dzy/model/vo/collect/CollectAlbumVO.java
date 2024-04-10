package com.dzy.model.vo.collect;

import com.dzy.model.vo.album.AlbumVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 专辑简介视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:49
 */
@Data
public class CollectAlbumVO implements Serializable {

    private static final long serialVersionUID = -1690743240929656353L;

    private AlbumVO albumVO;

    /**
     * 歌手姓名列表
     */
    private List<String> singerNameList;

}
