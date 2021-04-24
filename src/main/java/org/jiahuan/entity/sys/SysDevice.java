package org.jiahuan.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class SysDevice extends Model<SysDevice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * ip地址
     */
    private String ip;

    private String name;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 设备唯一标识
     */
    private String mn;

    /**
     * 废水/废气类型
     */
    private String monitoringType;

    /**
     * 05/17协议
     */
    private String agreement;

    /**
     * 自动连接 0否/1是
     */
    private boolean autoConnection;

    private Integer subpackage;

    private Integer subpackageNumber;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    @TableField(exist = false)
    private Integer copyDeviceId;

}
