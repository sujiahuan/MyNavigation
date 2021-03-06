package org.jiahuan.entity.analog;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class AnRemoteControl extends Model<AnRemoteControl> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;
    
    private Integer verifyPlatformCommand;

        /**
     * 校验CN号
     */
        @TableField(fill = FieldFill.UPDATE)
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
        @TableField(exist = false)
        private boolean connetionStatus;

    public AnRemoteControl() {
    }

    public AnRemoteControl(Integer deviceId, Integer verifyPlatformCommand, String verifyCn, Integer responseParameter, Integer responseStatus) {
        this.deviceId = deviceId;
        this.verifyPlatformCommand = verifyPlatformCommand;
        this.verifyCn = verifyCn;
        this.responseParameter = responseParameter;
        this.responseStatus = responseStatus;
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

    public Integer getVerifyPlatformCommand() {
        return verifyPlatformCommand;
    }

    public void setVerifyPlatformCommand(Integer verifyPlatformCommand) {
        this.verifyPlatformCommand = verifyPlatformCommand;
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

    public boolean getConnetionStatus() {
        return connetionStatus;
    }

    public void setConnetionStatus(boolean connetionStatus) {
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
                ", verifyPlatformCommand=" + verifyPlatformCommand +
                ", verifyCn='" + verifyCn + '\'' +
                ", responseParameter=" + responseParameter +
                ", responseStatus=" + responseStatus +
                ", connetionStatus=" + connetionStatus +
                '}';
    }
}
