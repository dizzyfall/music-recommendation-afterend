package com.dzy.controller;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:35
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.vo.album.AlbumInfoVO;
import com.dzy.model.vo.album.AlbumSongVO;
import com.dzy.model.vo.album.AlbumVO;
import com.dzy.service.AlbumService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<List<AlbumVO>> albumRetrieveByPage(@RequestBody AlbumQueryRequest albumQueryRequest) {
        if (albumQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<AlbumVO> albumVOPage = albumService.listAlbumByPage(albumQueryRequest);
        List<AlbumVO> albumVOList = albumVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumVOList);
    }

    /**
     * 查询指定专辑信息
     *
     * @param albumSongQueryRequest
     * @return
     */
    @PostMapping("/info")
    public BaseResponse<AlbumInfoVO> albumInfoRetrieve(@RequestBody AlbumSongQueryRequest albumSongQueryRequest) {
        if (albumSongQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumInfoVO albumInfoVO = albumService.searchAlbumInfo(albumSongQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumInfoVO);
    }

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    @PostMapping("/song")
    public BaseResponse<List<AlbumSongVO>> albumSongRetrieve(@RequestBody AlbumSongQueryRequest albumSongQueryRequest) {
        if (albumSongQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<AlbumSongVO> albumSongVOList = albumService.listSong(albumSongQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumSongVOList);
    }
}
