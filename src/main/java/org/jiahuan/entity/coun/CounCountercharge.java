package org.jiahuan.entity.coun;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jh
 * @since 2020-08-07
 */
public class CounCountercharge extends Model<CounCountercharge> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;

        /**
     * 校验CN号
     */
         private String verifyCn;

        /**
     * 返回9011/9012
     */
         private Integer responseParameter;

        /**
     * 返回状态
     */
         private Integer responseStatus;

        /**
     * 联接状态
     */
         private Integer connetionStatus;

    public CounCountercharge() {
    }

    public CounCountercharge(Integer deviceId, String verifyCn, Integer responseParameter, Integer responseStatus, Integer connetionStatus) {
        this.deviceId = deviceId;
        this.verifyCn = verifyCn;
        this.responseParameter = responseParameter;
        this.responseStatus = responseStatus;
        this.connetionStatus = connetionStatus;
    }

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

    public String getVerifyCn() {
        return verifyCn;
    }

    public void setVerifyCn(String verifyCn) {
        this.verifyCn = verifyCn;
    }

    public Integer getResponseParameter() {
        return responseParameter;
    }

    public void setResponseParameter(Integer responseParameter) {
        this.responseParameter = responseParameter;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Integer getConnetionStatus() {
        return connetionStatus;
    }

    public void setConnetionStatus(Integer connetionStatus) {
        this.connetionStatus = connetionStatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CounCountercharge{" +
        "id=" + id +
        ", deviceId=" + deviceId +
        ", verifyCn=" + verifyCn +
        ", responseParameter=" + responseParameter +
        ", responseStatus=" + responseStatus +
        ", connetionStatus=" + connetionStatus +
        "}";
    }
}
