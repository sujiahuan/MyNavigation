package org.jiahuan.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
@Data
public class SysNavigation extends Model<SysNavigation> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图标
     */
    private Integer icomId;

    @TableField(exist=false)
    private String icomName;

    /**
     * 名称
     */
    private String name;

    private Integer type;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
