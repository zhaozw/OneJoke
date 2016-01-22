package com.smarter.onejoke.model;

import java.io.Serializable;

/**
 * Created by panl on 15/2/13.
 */
public class JokeInfo implements Serializable {

    private String content;
    private String hashId;
    private String updatetime;
    private long unixtime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }


}
