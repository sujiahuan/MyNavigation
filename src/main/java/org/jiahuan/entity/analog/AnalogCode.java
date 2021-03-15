package org.jiahuan.entity.analog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
@Data
public class AnalogCode extends Model<AnalogCode> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;

    private Integer divisorId;
    @TableField(exist=false)
    private String divisorName;
    @TableField(exist=false)
    private String divisorCode;


}
