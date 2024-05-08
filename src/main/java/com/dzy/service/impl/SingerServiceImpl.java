package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SingerMapper;
import com.dzy.model.dto.singer.SingerQueryRequest;
import com.dzy.model.dto.singer.SingerSearchTextQueryRequest;
import com.dzy.model.dto.singer.SingerTagsQueryRequest;
import com.dzy.model.entity.Singer;
import com.dzy.model.enums.SingerTagEnum;
import com.dzy.model.vo.singer.SingerDetailVO;
import com.dzy.model.vo.singer.SingerIntroVO;
import com.dzy.service.SingerService;
import com.dzy.service.SongService;
import com.dzy.commonutils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DZY
 * @description 针对表【singer(歌手表)】的数据库操作Service实现
 * @createDate 2024-03-10 11:42:01
 */
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer>
        implements SingerService {

    @Resource
    private SongService songService;

    /**
     * 查询所有的歌手
     * 按热度排序
     *
     * @param singerTagsQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.singer.SingerVO>
     * @date 2024/4/27  15:15
     */
    @Override
    public Page<SingerIntroVO> listAllSingerPage(SingerTagsQueryRequest singerTagsQueryRequest) {
        if (singerTagsQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        int pageCurrent = singerTagsQueryRequest.getPageCurrent();
        int pageSize = singerTagsQueryRequest.getPageSize();
        Page<Singer> page = new Page<>(pageCurrent, pageSize);
        Page<Singer> singerPage = this.page(page);
        List<SingerIntroVO> singerIntroVOList = singerPage.getRecords().stream().map(this::getSingerIntroVO).collect(Collectors.toList());
        Page<SingerIntroVO> singerVOPage = new Page<>(pageCurrent, pageSize, singerPage.getTotal());
        return singerVOPage.setRecords(singerIntroVOList);
    }

    /**
     * 分页
     * ES
     * 按搜索词查询歌手
     *
     * @param singerQueryRequest
     * @return
     */
    //todo 搜索词搜索,使用es
    @Override
    public Page<SingerIntroVO> listSingerBySearchTextByPage(SingerSearchTextQueryRequest singerQueryRequest) {
        if (singerQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        //查询歌手
        int pageCurrent = singerQueryRequest.getPageCurrent();
        int pageSize = singerQueryRequest.getPageSize();
        Page<Singer> page = new Page<>(pageCurrent, pageSize);
        Page<Singer> singerPage = this.page(page, queryWrapper);
        List<SingerIntroVO> singerIntroVOList = singerPage.getRecords().stream().map(this::getSingerIntroVO).collect(Collectors.toList());
        Page<SingerIntroVO> singerVOPage = new Page<>(pageCurrent, pageSize, singerPage.getTotal());
        return singerVOPage.setRecords(singerIntroVOList);
    }

    /**
     * 转SingerIntroVO
     *
     * @param singer
     * @return com.dzy.model.vo.singer.SingerVO
     * @date 2024/4/30  11:07
     */
    @Override
    public SingerIntroVO getSingerIntroVO(Singer singer) {
        if (singer == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SingerIntroVO singerIntroVO = new SingerIntroVO();
        try {
            BeanUtils.copyProperties(singer, singerIntroVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return singerIntroVO;
    }

    /**
     * 根据歌手标签获取查询条件构造器
     *
     * @param singerTagsQueryRequest
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.dzy.model.entity.Singer>
     * @date 2024/4/27  16:03
     */
    public QueryWrapper<Singer> getSingerTagQueryWrapper(SingerTagsQueryRequest singerTagsQueryRequest) {
        if (singerTagsQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Singer> singerTagQueryWrapper = new QueryWrapper<>();
        Integer area = singerTagsQueryRequest.getArea();
        if (area != null && !area.equals(SingerTagEnum.ALL.getSingerTagId())) {
            singerTagQueryWrapper.eq("area", area);
        }
        Integer sex = singerTagsQueryRequest.getSex();
        if (sex != null && !sex.equals(SingerTagEnum.ALL.getSingerTagId())) {
            singerTagQueryWrapper.eq("sex", sex);
        }
        Integer genre = singerTagsQueryRequest.getGenre();
        if (genre != null && !genre.equals(SingerTagEnum.ALL.getSingerTagId())) {
            singerTagQueryWrapper.eq("genre", genre);
        }
        return singerTagQueryWrapper;
    }

    /**
     * 分页
     * 按标签查询歌手（包含‘全部’标签）
     * 标签为固定标签：地区、性别、类型
     *
     * @param singerTagsQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.singer.SingerVO>
     * @date 2024/4/27  15:14
     */
    @Override
    public Page<SingerIntroVO> listSingerByTagsByPage(SingerTagsQueryRequest singerTagsQueryRequest) {
        if (singerTagsQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Singer> queryWrapper = getSingerTagQueryWrapper(singerTagsQueryRequest);
        //查询歌手
        int pageCurrent = singerTagsQueryRequest.getPageCurrent();
        int pageSize = singerTagsQueryRequest.getPageSize();
        Page<Singer> page = new Page<>(pageCurrent, pageSize);
        Page<Singer> singerPage = this.page(page, queryWrapper);
        //脱敏
        List<SingerIntroVO> singerIntroVOList = singerPage.getRecords().stream().map(this::getSingerIntroVO).collect(Collectors.toList());
        //新分页对象
        Page<SingerIntroVO> singerVOPage = new Page<>(pageCurrent, pageSize, singerPage.getTotal());
        singerVOPage.setRecords(singerIntroVOList);
        return singerVOPage;
    }

    /**
     * 根据歌手id（json字符串）获取歌手姓名列表
     *
     * @param singerIdList
     * @return
     */
    @Override
    public List<String> getSingerNameList(String singerIdList) {
        if (StringUtils.isBlank(singerIdList)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<String> idList = JsonUtil.convertJsonToList(singerIdList);
        List<Singer> singerList = this.listByIds(idList);
        List<String> SingerNameList = singerList.stream().map(Singer::getSingerName).collect(Collectors.toList());
        return SingerNameList;
    }

    /**
     * 通过歌手姓名列表转换为前端直接使用的歌手姓名字符串（xxx/xxx/xxx）
     *
     * @param singerIdList
     * @return java.lang.String
     * @date 2024/4/26  17:32
     */
    @Override
    public String getSingerNameStr(String singerIdList) {
        List<String> singerNameList = getSingerNameList(singerIdList);
        return StringUtils.join(singerNameList, "/");
    }

    /**
     * 查询指定歌手详细信息
     *
     * @param singerQueryRequest
     * @return com.dzy.model.vo.singer.SingerDetailVO
     * @date 2024/4/30  11:31
     */
    @Override
    public SingerDetailVO searchSingerDetail(SingerQueryRequest singerQueryRequest) {
        if (singerQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long singerId = singerQueryRequest.getSingerId();
        if (singerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", singerId);
        Singer singer = this.getOne(queryWrapper);
        if (singer == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "暂无此歌手");
        }
        return getSingerDetailVO(singer);
    }

    /**
     * 获取SingerDetailVO
     *
     * @param singer
     * @return com.dzy.model.vo.singer.SingerDetailVO
     * @date 2024/4/30  12:02
     */
    @Override
    public SingerDetailVO getSingerDetailVO(Singer singer) {
        if (singer == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SingerDetailVO singerDetailVO = SingerDetailVO.objToVO(singer);
        singerDetailVO.setSingerId(singer.getId());
        return singerDetailVO;
    }

}




