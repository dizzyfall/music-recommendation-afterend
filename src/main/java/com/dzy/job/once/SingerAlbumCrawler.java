package com.dzy.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dzy.model.entity.Album;
import com.dzy.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 获取歌手信息
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  17:18
 */
@Component
public class SingerAlbumCrawler {

    @Autowired
    private AlbumService albumService;

//    public void getSingerSong() {
//        //字符串拼接请求参数
//        //页号、sin这两个参数
//        //第一页请求参数比较复杂，单独提取数据，从第二页开始
//        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":791700642,\"g_tk_new_20200303\":1332467997,\"g_tk\":1332467997},\"req_1\":{\"module\":\"music.musicsearch.HotkeyService\",\"method\":\"GetHotkeyForQQMusicMobile\",\"param\":{\"searchid\":\"31476582721695545\",\"remoteplace\":\"txt.yqq.top\",\"from\":\"yqqweb\"}},\"req_2\":{\"method\":\"GetSingerDetail\",\"param\":{\"singer_mids\":[\"0010PLKl2Wgolz\"],\"ex_singer\":1,\"wiki_singer\":1,\"group_singer\":0,\"pic\":1,\"photos\":0},\"module\":\"music.musichallSinger.SingerInfoInter\"},\"req_3\":{\"method\":\"GetAlbumList\",\"param\":{\"singerMid\":\"0010PLKl2Wgolz\",\"order\":0,\"begin\":0,\"num\":5,\"songNumTag\":0,\"singerID\":0},\"module\":\"music.musichallAlbum.AlbumListServer\"},\"req_4\":{\"method\":\"GetSingerMvList\",\"param\":{\"singermid\":\"0010PLKl2Wgolz\",\"count\":20,\"start\":0,\"order\":1},\"module\":\"MvService.MvInfoProServer\"},\"req_5\":{\"method\":\"GetSingerSongList\",\"param\":{\"singerMid\":\"0010PLKl2Wgolz\",\"order\":1,\"begin\":0,\"num\":10},\"module\":\"musichall.song_list_server\"},\"req_6\":{\"method\":\"cgi_qry_concern_num\",\"module\":\"Concern.ConcernSystemServer\",\"param\":{\"vec_userinfo\":[{\"userid\":\"0010PLKl2Wgolz\",\"usertype\":1}]}},\"req_7\":{\"method\":\"GetSimilarSingerList\",\"param\":{\"singerMid\":\"0010PLKl2Wgolz\",\"num\":5},\"module\":\"music.SimilarSingerSvr\"},\"req_8\":{\"module\":\"music.paycenterapi.LoginStateVerificationApi\",\"method\":\"GetChargeAccount\",\"param\":{\"appid\":\"mlive\"}},\"req_9\":{\"module\":\"MessageCenter.MessageCenterServer\",\"method\":\"GetMessage\",\"param\":{\"uin\":\"791700642\",\"red_dot\":[{\"msg_type\":1}]}},\"req_10\":{\"module\":\"GlobalComment.GlobalCommentMessageReadServer\",\"method\":\"GetMessage\",\"param\":{\"uin\":\"791700642\",\"page_num\":0,\"page_size\":1,\"last_msg_id\":\"\",\"type\":0}}}";
//
//
//        String url = "\n" +
//                "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1716458525512&sign=zzb5c6f96012lqxm2vudqaiwzve7r9cfa8d77efd2";
//
//
//        String result = HttpRequest
//                .post(url)
//                .body(json)
//                .execute()
//                .body();
//
//        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
//        JSONObject req_2 = (JSONObject) map.get("req_2");
//        JSONObject data_2 = (JSONObject) req_2.get("data");
//        JSONArray singer_list = (JSONArray) data_2.get("singer_list");
//
//        JSONObject req = (JSONObject) map.get("req_4");
//        JSONObject data = (JSONObject) req.get("data");
//        JSONArray list = (JSONArray) data.get("list");
//        if (list == null) {
//            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
//        }
//        List<SongExcel> res = new ArrayList<>();
//        for (Object object : list) {
//            JSONObject tempSong = (JSONObject) object;
//            SongExcel songExcel = new SongExcel();
//            songExcel.setTitle(tempSong.getStr("title"));
//            songExcel.setPlayCount(tempSong.getStr("playcnt"));
//            // 将字符串转换为long
//            long timestamp = Long.parseLong(tempSong.getStr("pubdate")) * 1000;
//            // 将long转换为Date对象
//            Date date = new Date(timestamp);
//            // 定义日期格式
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            // 将Date对象格式化为字符串
//            String formattedDate = sdf.format(date);
//            songExcel.setPublishTime(formattedDate);
//            res.add(songExcel);
//        }
//
//        // 写入 Excel
//        String fileName = "F.I.R.飞儿乐团歌曲信息.xlsx";
//        EasyExcel.write(fileName, SongExcel.class)
//                .sheet("歌曲列表")
//                .doWrite(res);
//    }

    public void getAlbumDetailaToDB() {

        //字符串拼接请求参数
        //页号、sin这两个参数
        //第一页请求参数比较复杂，单独提取数据，从第二页开始
        String json = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":791700642,\"g_tk_new_20200303\":522037469,\"g_tk\":522037469},\"req_1\":{\"module\":\"music.musicsearch.HotkeyService\",\"method\":\"GetHotkeyForQQMusicMobile\",\"param\":{\"searchid\":\"35038433435197562\",\"remoteplace\":\"txt.yqq.top\",\"from\":\"yqqweb\"}},\"req_2\":{\"method\":\"GetSingerDetail\",\"param\":{\"singer_mids\":[\"0025NhlN2yWrP4\"],\"ex_singer\":1,\"wiki_singer\":1,\"group_singer\":0,\"pic\":1,\"photos\":0},\"module\":\"music.musichallSinger.SingerInfoInter\"},\"req_3\":{\"method\":\"GetAlbumList\",\"param\":{\"singerMid\":\"0025NhlN2yWrP4\",\"order\":0,\"begin\":0,\"num\":5,\"songNumTag\":0,\"singerID\":0},\"module\":\"music.musichallAlbum.AlbumListServer\"},\"req_4\":{\"method\":\"GetSingerMvList\",\"param\":{\"singermid\":\"0025NhlN2yWrP4\",\"count\":20,\"start\":0,\"order\":1},\"module\":\"MvService.MvInfoProServer\"},\"req_5\":{\"method\":\"GetSingerSongList\",\"param\":{\"singerMid\":\"0025NhlN2yWrP4\",\"order\":1,\"begin\":0,\"num\":10},\"module\":\"musichall.song_list_server\"},\"req_6\":{\"method\":\"cgi_qry_concern_num\",\"module\":\"Concern.ConcernSystemServer\",\"param\":{\"vec_userinfo\":[{\"userid\":\"0025NhlN2yWrP4\",\"usertype\":1}]}},\"req_7\":{\"method\":\"GetSimilarSingerList\",\"param\":{\"singerMid\":\"0025NhlN2yWrP4\",\"num\":5},\"module\":\"music.SimilarSingerSvr\"},\"req_8\":{\"module\":\"music.paycenterapi.LoginStateVerificationApi\",\"method\":\"GetChargeAccount\",\"param\":{\"appid\":\"mlive\"}},\"req_9\":{\"module\":\"MessageCenter.MessageCenterServer\",\"method\":\"GetMessage\",\"param\":{\"uin\":\"791700642\",\"red_dot\":[{\"msg_type\":1}]}},\"req_10\":{\"module\":\"GlobalComment.GlobalCommentMessageReadServer\",\"method\":\"GetMessage\",\"param\":{\"uin\":\"791700642\",\"page_num\":0,\"page_size\":1,\"last_msg_id\":\"\",\"type\":0}}}";

        String url = "\n" +
                "\n" +
                "\n" +
                "https://u6.y.qq.com/cgi-bin/musics.fcg?_=1716692916355&sign=zzbf7378ecabgwrxkd2dniafbglsdngoqa5140084";

        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();

        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject req = (JSONObject) map.get("req_3");
        JSONObject data = (JSONObject) req.get("data");
        JSONArray albumList = (JSONArray) data.get("albumList");
        List<Album> res = new ArrayList<>();
        for (Object object : albumList) {
            JSONObject temp = (JSONObject) object;
            Album album = new Album();
            album.setTitle(temp.getStr("albumName"));
            album.setType(temp.getStr("albumType"));
            album.setSongCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = temp.getStr("publishDate");
            Date date = null;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            album.setPublishTime(date);
            res.add(album);
        }
        albumService.saveBatch(res);
    }

}
