package com.dzy.model.dto.album;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -1897904946366794619L;

    private Long singerId;

}
