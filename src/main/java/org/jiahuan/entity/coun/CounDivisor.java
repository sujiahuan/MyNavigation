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

    private String avg;

    private String max;

    private String min;

    private String cou;

    private String zavg;

    private String zcou;

    private String zmax;

    private String zmin;

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

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCou() {
        return cou;
    }

    public void setCou(String cou) {
        this.cou = cou;
    }

    public String getZavg() {
        return zavg;
    }

    public void setZavg(String zavg) {
        this.zavg = zavg;
    }

    public String getZcou() {
        return zcou;
    }

    public void setZcou(String zcou) {
        this.zcou = zcou;
    }

    public String getZmax() {
        return zmax;
    }

    public void setZmax(String zmax) {
        this.zmax = zmax;
    }

    public String getZmin() {
        return zmin;
    }

    public void setZmin(String zmin) {
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

    @Override
    public String toString() {
        return "CounDivisor{" +
        "id=" + id +
        ", deviceId=" + deviceId +
        ", name=" + name +
        ", code=" + code +
        ", avg=" + avg +
        ", max=" + max +
        ", min=" + min +
        ", cou=" + cou +
        ", zavg=" + zavg +
        ", zcou=" + zcou +
        ", zmax=" + zmax +
        ", zmin=" + zmin +
        ", flag=" + flag +
        ", gmtCreat=" + gmtCreate +
        ", gmtModify=" + gmtModified +
        "}";
    }
}
