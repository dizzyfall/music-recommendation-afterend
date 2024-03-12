package com.dzy.model.vo.singer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 歌手返回信息VO
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  11:49
 */
@Data
public class SingerVO {
    /**
     * 歌手id
     */
    private Long id;

    /**
     * 歌手名字
     */
    private String name;

}
