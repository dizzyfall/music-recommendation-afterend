package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.common.PageRequest;
import com.dzy.model.dto.singer.SingerSearchTextQueryRequest;
import com.dzy.model.dto.singer.SingerTagsQueryRequest;
import com.dzy.model.entity.Singer;
import com.dzy.model.vo.singer.SingerVO;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【singer(歌手表)】的数据库操作Service
 * @createDate 2024-03-10 11:42:01
 */
public interface SingerService extends IService<Singer> {

    /**
     * 查询所有的歌手
     * 按热度排序
     *
     * @param pageRequest
     * @return
     */
    Page<SingerVO> listAllSingerPage(PageRequest pageRequest);

    /**
     * 分页
     * ES
     * 按搜索词查询歌手
     *
     * @param singerSearchTextQueryRequest
     * @return
     */
    Page<SingerVO> listSingerBySearchTextByPage(SingerSearchTextQueryRequest singerSearchTextQueryRequest);

    /**
     * 脱敏
     * 转SingerVO
     *
     * @param singer
     * @return
     */
    SingerVO singerToSingerVO(Singer singer);

    /**
     * 分页
     * 按标签查询歌手
     * 标签为固定标签：地区、性别、类型
     *
     * @param singerTagsQueryRequest
     * @return
     */
    Page<SingerVO> listSingerByTagsByPage(SingerTagsQueryRequest singerTagsQueryRequest);

    /**
     * 根据歌手id（json字符串）获取歌手姓名列表
     *
     * @param singerIdList
     * @return
     */
    List<String> getSingerNameList(String singerIdList);
}
