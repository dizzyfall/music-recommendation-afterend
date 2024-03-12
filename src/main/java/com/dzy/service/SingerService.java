package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.PageRequest;
import com.dzy.model.dto.singer.SingerQueryRequest;
import com.dzy.model.entity.Singer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.vo.singer.SingerVO;

/**
* @author DZY
* @description 针对表【singer(歌手表)】的数据库操作Service
* @createDate 2024-03-10 11:42:01
*/
public interface SingerService extends IService<Singer> {

    /**
     * 分页查询所有的歌手
     *
     * @param pageRequest
     * @return
     */
    Page<SingerVO> searchAllSingerPage(PageRequest pageRequest);

    /**
     * 分页查询按指定字段的歌手
     *
     * @param singerQueryRequest
     * @return
     */
    Page<SingerVO> searchSingerBySpecificByPage(SingerQueryRequest singerQueryRequest);

    SingerVO singerToSingerVO(Singer singer);
}
