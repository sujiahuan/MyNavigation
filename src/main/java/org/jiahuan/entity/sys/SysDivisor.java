package org.jiahuan.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jh
 * @since 2020-09-29
 */
@Data
public class SysDivisor extends Model<SysDivisor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private int navigationId;

    private String name;

    private String code;

    /**
     * 0是排污因子，1是动态因子
     */
    private int type;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
