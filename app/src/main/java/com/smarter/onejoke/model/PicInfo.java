package com.smarter.onejoke.model;

import java.io.Serializable;

/**
 * Created by panl on 15/2/10.
 */
public class PicInfo implements Serializable {

  /*  {
        "content":"二汪",
            "hashId":"13AF55EB201FADD4DB8AD3C0FC053E72",
            "unixtime":1418954054,
            "updatetime":"2014-12-19 09:54:14",
            "url":"http://img.juhe.cn/joke/201412/19/13AF55EB201FADD4DB8AD3C0FC053E72.gif"
    }*/

    private String url;
    private String hashId;
    private String content;
    private long unixtime;
    private String updatetime;

    public PicInfo() {
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
