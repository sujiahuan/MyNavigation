package org.jiahuan.entity.analog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public class AnDivisorParameter extends Model<AnDivisorParameter> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    @TableField(exist=false)
    private String divisorCode;

    private Integer divisorId;

    private Double avgMin;

    private Double avgMax;

    private Double max;

    private Double min;

    private Double cou;

    private Double zavg;

    private Double zmax;

    private Double zmin;

    private String flag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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

    public String getDivisorCode() {
        return divisorCode;
    }

    public void setDivisorCode(String divisorCode) {
        this.divisorCode = divisorCode;
    }

    public Integer getDivisorId() {
        return divisorId;
    }

    public void setDivisorId(Integer divisorId) {
        this.divisorId = divisorId;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max){
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getCou() {
        return cou;
    }

    public void setCou(Double cou) {
        this.cou = cou;
    }

    public Double getZavg() {
        return zavg;
    }

    public void setZavg(Double zavg) {
        this.zavg = zavg;
    }

    public Double getZmax() {
        return zmax;
    }

    public void setZmax(Double zmax) {
        this.zmax = zmax;
    }

    public Double getZmin() {
        return zmin;
    }

    public void setZmin(Double zmin) {
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

    public Double getAvgMin() {
        return avgMin;
    }

    public void setAvgMin(Double avgMin) {
        this.avgMin = avgMin;
    }

    public Double getAvgMax() {
        return avgMax;
    }

    public void setAvgMax(Double avgMax) {
        this.avgMax = avgMax;
    }

    @Override
    public String toString() {
        return "AnalogDivisorParameter{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", divisorCode=" + divisorCode +
                ", divisorId=" + divisorId +
                ", avgMin=" + avgMin +
                ", avgMax=" + avgMax +
                ", max=" + max +
                ", min=" + min +
                ", cou=" + cou +
                ", zavg=" + zavg +
                ", zmax=" + zmax +
                ", zmin=" + zmin +
                ", flag='" + flag + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
