package com.dzy.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Singer;
import com.dzy.service.SingerService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取歌手信息
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  17:18
 */
public class SingerDetail {

    @Resource
    private SingerService singerService;

    public void getSingerDetail() {
        //歌手第一页请求参数
//        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":0,\"g_tk_new_20200303\":5381,\"g_tk\":5381},\"req_1\":{\"module\":\"music.musicsearch.HotkeyService\",\"method\":\"GetHotkeyForQQMusicMobile\",\"param\":{\"searchid\":\"22608093746361070\",\"remoteplace\":\"txt.yqq.top\",\"from\":\"yqqweb\"}},\"req_2\":{\"module\":\"music.musichallSinger.SingerList\",\"method\":\"GetSingerListIndex\",\"param\":{\"area\":-100,\"sex\":-100,\"genre\":-100,\"index\":-100,\"sin\":0,\"cur_page\":1}},\"req_3\":{\"module\":\"music.paycenterapi.LoginStateVerificationApi\",\"method\":\"GetChargeAccount\",\"param\":{\"appid\":\"mlive\"}}}";
//        String url = "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1710080540412&sign=zzb1e7b1ad5tqo5o9tdcoejvvepkswg5ee827ec";
        //歌手第二页请求参数
//        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":0,\"g_tk_new_20200303\":5381,\"g_tk\":5381},\"req_1\":{\"module\":\"music.musichallSinger.SingerList\",\"method\":\"GetSingerListIndex\",\"param\":{\"area\":-100,\"sex\":-100,\"genre\":-100,\"index\":-100,\"sin\":80,\"cur_page\":2}}}";

        //循环获取
        int i = 4;
        int j = 240;
        //字符串拼接请求参数
        //页号、sin这两个参数
        //第一页请求参数比较复杂，单独提取数据，从第二页开始
        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":791700642,\"g_tk_new_20200303\":933994069,\"g_tk\":933994069},\"req_1\":{\"module\":\"music.musichallSinger.SingerList\",\"method\":\"GetSingerListIndex\",\"param\":{\"area\":-100,\"sex\":-100,\"genre\":-100,\"index\":-100,\"sin\":240,\"cur_page\":4}}}";
        String url = "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1710168842696&sign=zzb11c9de17nxhhlh8udcdobxped6qegdd5330dd";

        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();

        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject req = (JSONObject) map.get("req_1");
        JSONObject data = (JSONObject) req.get("data");
        JSONArray singerlist = (JSONArray) data.get("singerlist");
        if (singerlist == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<Singer> res = new ArrayList<>();
        for (Object object : singerlist) {
            JSONObject tempSinger = (JSONObject) object;
            Singer singer = new Singer();

            if (StringUtils.isBlank(tempSinger.getStr("singer_pic"))) {
                singer.setAvatarPath(null);
            } else {
                singer.setAvatarPath(tempSinger.getStr("singer_pic"));
            }

            if (StringUtils.isBlank(tempSinger.getStr("singer_name"))) {
                singer.setName(null);
            } else {
                singer.setName(tempSinger.getStr("singer_name"));
            }

            if (StringUtils.isBlank(tempSinger.getStr("other_name"))) {
                singer.setAlias(null);
            } else {
                singer.setAlias(tempSinger.getStr("other_name"));
            }

            if (StringUtils.isBlank(tempSinger.getStr("spell"))) {
                singer.setSpell(null);
            } else {
                singer.setSpell(tempSinger.getStr("spell"));
            }
            res.add(singer);
        }
        singerService.saveBatch(res);
    }

}
