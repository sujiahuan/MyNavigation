package org.jiahuan.entity.coun;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wj
 * @since 2020-08-02
 */
public class CounDataType extends Model<CounDataType> {

    private static final long serialVersionUID = 1L;

    private Integer id;

        /**
     * 设备id
     */
         private Integer deviceId;

        /**
     * 类型：1实时/2分钟/3小时/4
     */
         private Integer dataType;

        /**
     * 是否定时发送：0否/1是
     */
         private Integer isTiming;

    /**
     * 折算：合（join）/分（divide）/没有（none）
     */
    private String zs;

    /**
     * 补发数据间隔
     */
    private Integer dateInterval;

    public CounDataType() {
    }

    public CounDataType(Integer deviceId, Integer dataType, Integer isTiming, String zs, Integer dateInterval) {
        this.deviceId = deviceId;
        this.dataType = dataType;
        this.isTiming = isTiming;
        this.zs = zs;
        this.dateInterval = dateInterval;
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

    public Integer getIsTiming() {
        return isTiming;
    }

    public void setIsTiming(Integer isTiming) {
        this.isTiming = isTiming;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getDataInterval() {
        return dateInterval;
    }

    public void setDataInterval(Integer dateInterval) {
        this.dateInterval = dateInterval;
    }

    @Override
    public String toString() {
        return "CounDataType{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", dataType=" + dataType +
                ", isTiming=" + isTiming +
                ", zs='" + zs + '\'' +
                ", interval=" + dateInterval +
                '}';
    }
}
