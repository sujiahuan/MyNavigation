package org.jiahuan.entity.sys;

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

         private Integer subpackage;

         private Integer subpackageNumber;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public Integer getSubpackage() {
        return subpackage;
    }

    public void setSubpackage(Integer subpackage) {
        this.subpackage = subpackage;
    }

    public Integer getSubpackageNumber() {
        return subpackageNumber;
    }

    public void setSubpackageNumber(Integer subpackageNumber) {
        this.subpackageNumber = subpackageNumber;
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

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CounDevice{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", port=" + port +
                ", mn='" + mn + '\'' +
                ", monitoringType='" + monitoringType + '\'' +
                ", agreement='" + agreement + '\'' +
                ", subpackage=" + subpackage +
                ", subpackageNumber=" + subpackageNumber +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
