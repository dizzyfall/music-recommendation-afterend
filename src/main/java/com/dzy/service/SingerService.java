package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.singer.SingerQueryRequest;
import com.dzy.model.dto.singer.SingerSearchTextQueryRequest;
import com.dzy.model.dto.singer.SingerTagsQueryRequest;
import com.dzy.model.entity.Singer;
import com.dzy.model.vo.singer.SingerDetailVO;
import com.dzy.model.vo.singer.SingerIntroVO;

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
     * @param singerTagsQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.singer.SingerVO>
     * @date 2024/4/27  15:13
     */
    Page<SingerIntroVO> listAllSingerPage(SingerTagsQueryRequest singerTagsQueryRequest);

    /**
     * 分页
     * ES
     * 按搜索词查询歌手
     *
     * @param singerSearchTextQueryRequest
     * @return
     */
    Page<SingerIntroVO> listSingerBySearchTextByPage(SingerSearchTextQueryRequest singerSearchTextQueryRequest);

    /**
     * 转SingerIntroVO
     *
     * @param singer
     * @return
     */
    SingerIntroVO getSingerIntroVO(Singer singer);

    /**
     * 分页
     * 按标签查询歌手（包含‘全部’标签）
     * 标签为固定标签：地区、性别、类型
     *
     * @param singerTagsQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.singer.SingerVO>
     * @date 2024/4/27  15:14
     */
    Page<SingerIntroVO> listSingerByTagsByPage(SingerTagsQueryRequest singerTagsQueryRequest);

    /**
     * 根据歌手id（json字符串）获取歌手姓名列表
     *
     * @param singerIdList
     * @return
     */
    List<String> getSingerNameList(String singerIdList);

    /**
     * 通过歌手姓名列表转换为前端直接使用的歌手姓名字符串
     *
     * @param singerIdList
     * @return java.lang.String
     * @date 2024/4/26  17:31
     */
    String getSingerNameStr(String singerIdList);

    /**
     * 查询指定歌手详细信息
     *
     * @param singerQueryRequest
     * @return com.dzy.model.vo.singer.SingerDetailVO
     * @date 2024/4/30  11:27
     */
    SingerDetailVO searchSingerDetail(SingerQueryRequest singerQueryRequest);

    /**
     * 获取SingerDetailVO
     *
     * @param singer
     * @return com.dzy.model.vo.singer.SingerDetailVO
     * @date 2024/4/30  12:05
     */
    SingerDetailVO getSingerDetailVO(Singer singer);

}
