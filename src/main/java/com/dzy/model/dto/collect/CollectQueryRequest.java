package com.dzy.model.dto.collect;

import com.dzy.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询收藏歌曲请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/10  9:31
 */
@Data
public class CollectQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 5936507987375692804L;

    private Long userId;

}
