package com.smarter.onejoke.utils;

import java.io.Serializable;

/**
 * Created by panl on 15/2/13.
 */
public class JokeInfo implements Serializable {

    private String contents;
    private long unixTime;
    private String updateTime;
    public JokeInfo() {

    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}
