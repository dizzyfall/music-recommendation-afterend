package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.common.PageRequest;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.singer.SingerQueryRequest;
import com.dzy.model.entity.Singer;
import com.dzy.model.vo.singer.SingerVO;
import com.dzy.service.SingerService;
import com.dzy.utils.ResponseUtil;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
     * 分页查询所有的歌手
     *
     * @param pageRequest 分页请求
     * @return
     */
    @PostMapping("/list/all")
    public BaseResponse<List<SingerVO>> singerListByPage(@RequestBody PageRequest pageRequest){
        Page<SingerVO> singerVOPage = singerService.searchAllSingerPage(pageRequest);
        List<SingerVO> singerVOList = singerVOPage.getRecords();
        if(CollectionUtils.isEmpty(singerVOList)){
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR,"查询数据为空");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS,singerVOList,"获取歌手列表成功");
    }

    /**
     * 分页查询按指定字段的歌手
     *
     * @param singerQueryRequest
     * @return
     */
    @PostMapping("/list/specific")
    public BaseResponse<List<SingerVO>> singerListByCategoryByPage(@RequestBody SingerQueryRequest singerQueryRequest){
        if(singerQueryRequest == null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<SingerVO> singerVOPage = singerService.searchSingerBySpecificByPage(singerQueryRequest);
        List<SingerVO> singerVOList = singerVOPage.getRecords();
        if(CollectionUtils.isEmpty(singerVOList)){
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR,"查询数据为空");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS,singerVOList,"获取歌手列表成功");
    }

}
