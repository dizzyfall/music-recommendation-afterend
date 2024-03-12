package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.common.PageRequest;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.singer.SingerQueryRequest;
import com.dzy.model.entity.Singer;
import com.dzy.model.vo.singer.SingerVO;
import com.dzy.service.SingerService;
import com.dzy.mapper.SingerMapper;
import com.dzy.utils.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author DZY
* @description 针对表【singer(歌手表)】的数据库操作Service实现
* @createDate 2024-03-10 11:42:01
*/
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer>
    implements SingerService{

    /**
     * 分页查询所有的歌手
     *
     * @param pageRequest
     * @return
     */
    @Override
    public Page<SingerVO> searchAllSingerPage(PageRequest pageRequest) {
        int pageCurrent = pageRequest.getPageCurrent();
        int pageSize = pageRequest.getPageSize();
        Page<Singer> page = new Page<>(pageCurrent,pageSize);
        Page<Singer> singerPage = this.page(page);
        List<SingerVO> singerVOList = singerPage.getRecords().stream().map(this::singerToSingerVO).collect(Collectors.toList());
        Page<SingerVO> singerVOPage = new Page<>(pageCurrent,pageSize,singerPage.getTotal());
        return singerVOPage.setRecords(singerVOList);
    }

    @Override
    //todo 搜索词搜索,使用es
    public Page<SingerVO> searchSingerBySpecificByPage(SingerQueryRequest singerQueryRequest) {
        if(singerQueryRequest == null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        //歌手名字
        String name = singerQueryRequest.getName();
        if(StringUtils.isNotBlank(name)){
            queryWrapper.eq("singer_name",name);
        }
        //歌手别名
        String alias = singerQueryRequest.getAlias();
        List<String> aliasList = JsonUtil.convertJsonToList(alias);
        if(CollectionUtils.isNotEmpty(aliasList)){
            for (String aliasTag : aliasList) {
                //标签不能为空白字符
                if(StringUtils.isNotBlank(aliasTag)){
                    queryWrapper=queryWrapper.like("singer_alias",aliasTag);
                }
            }
        }
        //歌手类型
        String category = singerQueryRequest.getCategory();
        List<String> categoryList = JsonUtil.convertJsonToList(category);
        if(CollectionUtils.isNotEmpty(categoryList)){
            for (String categoryTag : categoryList) {
                //标签不能为空白字符
                if(StringUtils.isNotBlank(categoryTag)){
                    queryWrapper=queryWrapper.like("singer_category",categoryTag);
                }
            }
        }
        //查询歌手
        int pageCurrent = singerQueryRequest.getPageCurrent();
        int pageSize = singerQueryRequest.getPageSize();
        Page<Singer> page = new Page<>(pageCurrent,pageSize);
        Page<Singer> singerPage = this.page(page, queryWrapper);
        List<SingerVO> singerVOList = singerPage.getRecords().stream().map(this::singerToSingerVO).collect(Collectors.toList());
        Page<SingerVO> singerVOPage = new Page<>(pageCurrent,pageSize,singerPage.getTotal());
        return singerVOPage.setRecords(singerVOList);
    }

    @Override
    public SingerVO singerToSingerVO(Singer singer){
        if(singer==null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SingerVO singerVO = new SingerVO();
        try {
            BeanUtils.copyProperties(singer,singerVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"Bean复制属性错误");
        }
        return singerVO;
    }
}




