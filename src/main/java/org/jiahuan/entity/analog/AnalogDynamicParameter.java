package org.jiahuan.entity.analog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */

@Data
public class AnalogDynamicParameter extends Model<AnalogDynamicParameter> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;
    @TableField(exist=false)
    private String divisorCode;
    @TableField(exist=false)
    private String divisorName;

    private int divisorId;

    private String value;

    private int type;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
