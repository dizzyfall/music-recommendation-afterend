package com.dzy.model.dto.singer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  13:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingerQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3330937688653421684L;

    /**
     * 歌手名字
     */
    private String name;

    /**
     * 歌手别名
     */
    private String alias;

    /**
     * 歌手类别
     */
    private String category;

}
