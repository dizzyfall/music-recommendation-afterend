package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.singer.SingerSearchTextQueryRequest;
import com.dzy.model.dto.singer.SingerTagsQueryRequest;
import com.dzy.model.vo.singer.SingerVO;
import com.dzy.service.SingerService;
import com.dzy.utils.ResponseUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 歌手基本功能控制层
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  11:47
 */
@RestController
@RequestMapping("/singer")
public class SingerController {

    @Resource
    private SingerService singerService;

    /**
     * 分页
     * 查询所有的歌手
     * 按热度排序
     *
     * @param singerTagsQueryRequest
     * @return
     */
    @PostMapping("/list/all")
    public BaseResponse<List<SingerVO>> singerListRetrieveByPage(@RequestBody SingerTagsQueryRequest singerTagsQueryRequest) {
        if (singerTagsQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<SingerVO> singerVOPage = singerService.listAllSingerPage(singerTagsQueryRequest);
        List<SingerVO> singerVOList = singerVOPage.getRecords();
        if (CollectionUtils.isEmpty(singerVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "查询数据为空");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, singerVOList, "获取歌手列表成功");
    }

    /**
     * 分页
     * ES
     * 按搜索词查询歌手
     *
     * @param singerSearchTextQueryRequest
     * @return
     */
    @PostMapping("/list/searchtext")
    public BaseResponse<List<SingerVO>> singerListRetrieveBySearchTextByPage(@RequestBody SingerSearchTextQueryRequest singerSearchTextQueryRequest) {
        if (singerSearchTextQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<SingerVO> singerVOPage = singerService.listSingerBySearchTextByPage(singerSearchTextQueryRequest);
        List<SingerVO> singerVOList = singerVOPage.getRecords();
        if (CollectionUtils.isEmpty(singerVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "查询数据为空");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, singerVOList, "获取歌手列表成功");
    }

    /**
     * 分页
     * 按标签查询歌手（包含‘全部’标签）
     * 标签为固定标签：地区、性别、类型
     *
     * @param singerTagsQueryRequest
     * @return
     */
    @PostMapping("/list/tags")
    public BaseResponse<List<SingerVO>> singerListRetrieveByTagsByPage(@RequestBody SingerTagsQueryRequest singerTagsQueryRequest) {
        if (singerTagsQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<SingerVO> singerVOPage = singerService.listSingerByTagsByPage(singerTagsQueryRequest);
        List<SingerVO> singerVOList = singerVOPage.getRecords();
        if (CollectionUtils.isEmpty(singerVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "查询数据为空");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, singerVOList, "获取歌手列表成功");
    }

}
