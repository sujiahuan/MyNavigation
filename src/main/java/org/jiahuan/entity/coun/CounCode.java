package org.jiahuan.entity.coun;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
public class CounCode extends Model<CounCode> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;

    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CounCode{" +
        "id=" + id +
        ", deviceId=" + deviceId +
        ", code=" + code +
        "}";
    }
}
