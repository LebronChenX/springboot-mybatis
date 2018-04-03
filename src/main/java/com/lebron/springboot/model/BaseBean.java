package com.lebron.springboot.model;

import javax.persistence.Id;

public class BaseBean {

    @Id
    protected String id;
    protected Integer deleteFlag;
    protected Long createTime;
    protected Long updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseBean [id=" + id + ", deleteFlag=" + deleteFlag + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }

}
