package com.wd.tech.bean;

public class NoticeList {
    private int id;
    private String receiveUid;
    private String content;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiveUid() {
        return receiveUid;
    }

    public void setReceiveUid(String receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "NoticeList{" +
                "id=" + id +
                ", receiveUid='" + receiveUid + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
