package com.dzy.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Singer;
import com.dzy.model.job.easyexcel.SingerExcel;
import com.dzy.service.SingerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取歌手信息
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  17:18
 */
@Component
public class SingerDetailCrawler {

    @Autowired
    private SingerService singerService;

    public void getSingerDetail() {
        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":791700642,\"g_tk_new_20200303\":5381,\"g_tk\":5381},\"req_1\":{\"module\":\"music.musichallSinger.SingerList\",\"method\":\"GetSingerListIndex\",\"param\":{\"area\":4,\"sex\":2,\"genre\":7,\"index\":-100,\"sin\":0,\"cur_page\":1}}}";

        String url = "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1716464913318&sign=zzbb5203142be0euog9vosplouazkujgfe0c161a";

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
        List<SingerExcel> res = new ArrayList<>();
        for (Object object : singerlist) {
            JSONObject tempSinger = (JSONObject) object;
            SingerExcel singer = new SingerExcel();
            if (StringUtils.isBlank(tempSinger.getStr("singer_pic"))) {
                singer.setAvatarPath(null);
            } else {
                singer.setAvatarPath(tempSinger.getStr("singer_pic"));
            }
            if (StringUtils.isBlank(tempSinger.getStr("singer_name"))) {
                singer.setSingerName(null);
            } else {
                singer.setSingerName(tempSinger.getStr("singer_name"));
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
            singer.setArea(4);
            singer.setSex(2);
            singer.setGenre(1);
            res.add(singer);
        }

        // 写入 Excel
        String fileName = "日本-组合-流行-歌手信息.xlsx";
        EasyExcel.write(fileName, SingerExcel.class)
                .sheet("歌手列表")
                .doWrite(res);
    }

    public void getSingerDetailaToDB() {

        //字符串拼接请求参数
        //页号、sin这两个参数
        //第一页请求参数比较复杂，单独提取数据，从第二页开始
        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":0,\"g_tk_new_20200303\":5381,\"g_tk\":5381},\"req_1\":{\"module\":\"music.musichallSinger.SingerList\",\"method\":\"GetSingerListIndex\",\"param\":{\"area\":3,\"sex\":1,\"genre\":7,\"index\":-100,\"sin\":0,\"cur_page\":1}}}";

        String url = "\n" +
                "\n" +
                "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1716514446059&sign=zzb79e1ecf45olbjq04jeh7dmjdfbwrq2bfc09c8";

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
                singer.setSingerName(null);
            } else {
                singer.setSingerName(tempSinger.getStr("singer_name"));
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

            singer.setArea(1);
            singer.setSex(0);
            singer.setGenre(1);
            res.add(singer);
        }
        singerService.saveBatch(res);
    }

}
