package org.jiahuan.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jh
 * @since 2021-05-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDeviceNavigation extends Model<SysDeviceNavigation> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;

    private Integer navigationId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
