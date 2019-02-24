package com.wd.tech.bean;

public class AllInfo {

    /**
     * createTime : 1538104445000
     * id : 3
     * infoId : 1
     * thumbnail : https://img.huxiucdn.com/article/cover/201808/28/103850448205.jpg?imageView2/1/w/710/h/400/|imageMogr2/strip/interlace/1/quality/85/format/jpg
     * title : 关于滴滴顺风车事件的几点思考
     */

    private long createTime;
    private int id;
    private int infoId;
    private String thumbnail;
    private String title;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AllInfo{" +
                "createTime=" + createTime +
                ", id=" + id +
                ", infoId=" + infoId +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
