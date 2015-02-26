package com.smarter.onejoke.utils;

import java.io.Serializable;

/**
 * Created by panl on 15/2/10.
 */
public class PicInfo implements Serializable{

    private String picUrl;
    private String description;
    private long unixTime;
    private long updateTime;
    public PicInfo() {
    }
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


}
