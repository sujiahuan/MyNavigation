package org.jiahuan.entity.analog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private double valueMax;
    private double valueMin;

    /**
     * 1：状态/2：参数
     */
    private int type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

}
