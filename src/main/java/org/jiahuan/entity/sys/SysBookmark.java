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
 * @since 2020-06-16
 */
public class SysBookmark extends Model<SysBookmark> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String link;

    private Double version;

    private String openmode;

    private String username;

    private String password;

    private Integer parentId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getOpenmode() {
        return openmode;
    }

    public void setOpenmode(String openmode) {
        this.openmode = openmode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
        return "Bookmark{" +
        "id=" + id +
        ", title=" + title +
        ", link=" + link +
        ", version=" + version +
        ", openmode=" + openmode +
        ", username=" + username +
        ", password=" + password +
        ", parentId=" + parentId +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
