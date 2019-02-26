package com.wd.tech.bean;

public class FindTitleBean {

    /**
     * id : 4
     * releaseTime : 1535449854000
     * source : 本文转自公众号“企鹅智库”
     * title : 最后的红利：三四五线网民时间和金钱消费报告
     */

    private int id;
    private long releaseTime;
    private String source;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
