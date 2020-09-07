package org.jiahuan.entity.coun;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public class CounDivisor extends Model<CounDivisor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    private String name;

    private String code;

    private Integer avgMin;

    private Integer avgMax;

    private Integer max;

    private Integer min;

    private Integer cou;

    private Integer zavg;

    private Integer zmax;

    private Integer zmin;

    private String flag;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getCou() {
        return cou;
    }

    public void setCou(Integer cou) {
        this.cou = cou;
    }

    public Integer getZavg() {
        return zavg;
    }

    public void setZavg(Integer zavg) {
        this.zavg = zavg;
    }

    public Integer getZmax() {
        return zmax;
    }

    public void setZmax(Integer zmax) {
        this.zmax = zmax;
    }

    public Integer getZmin() {
        return zmin;
    }

    public void setZmin(Integer zmin) {
        this.zmin = zmin;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getAvgMin() {
        return avgMin;
    }

    public void setAvgMin(Integer avgMin) {
        this.avgMin = avgMin;
    }

    public Integer getAvgMax() {
        return avgMax;
    }

    public void setAvgMax(Integer avgMax) {
        this.avgMax = avgMax;
    }

    @Override
    public String toString() {
        return "CounDivisor{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", avgMin='" + avgMin + '\'' +
                ", avgMax='" + avgMax + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", cou='" + cou + '\'' +
                ", zavg='" + zavg + '\'' +
                ", zmax='" + zmax + '\'' +
                ", zmin='" + zmin + '\'' +
                ", flag='" + flag + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
