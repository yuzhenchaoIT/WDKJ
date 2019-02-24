package com.wd.tech.bean;

public class HomeListBean {


    /**
     * collection : 15
     * id : 55
     * releaseTime : 1539587804000
     * share : 37
     * source : 南七道
     * summary : 这两年在大数据领域，纯粹讲概念没有技术的公司都死完了。
     * thumbnail : https://img.huxiucdn.com/article/cover/201510/13/174903890379.png?imageView2/1/w/710/h/400/|imageMogr2/strip/interlace/1/quality/85/format/png
     * title : 谁杀死了大数据创业者？
     * whetherAdvertising : 2
     * whetherCollection : 2
     * whetherPay : 2
     * infoAdvertisingVo : {"content":"DI·进化\u20142018UBDC全域大数据峰...","id":3,"pic":"https://img.huxiucdn.com/activity/201808/06/173922452003.jpg?imageView2/1/w/370/h/208/|imageMogr2/strip/interlace/1/quality/85/format/jpg"}
     */

    private int collection;
    private int id;
    private long releaseTime;
    private int share;
    private String source;
    private String summary;
    private String thumbnail;
    private String title;
    private int whetherAdvertising;
    private int whetherCollection;
    private int whetherPay;
    private InfoAdvertisingVoBean infoAdvertisingVo;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

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

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public int getWhetherAdvertising() {
        return whetherAdvertising;
    }

    public void setWhetherAdvertising(int whetherAdvertising) {
        this.whetherAdvertising = whetherAdvertising;
    }

    public int getWhetherCollection() {
        return whetherCollection;
    }

    public void setWhetherCollection(int whetherCollection) {
        this.whetherCollection = whetherCollection;
    }

    public int getWhetherPay() {
        return whetherPay;
    }

    public void setWhetherPay(int whetherPay) {
        this.whetherPay = whetherPay;
    }

    public InfoAdvertisingVoBean getInfoAdvertisingVo() {
        return infoAdvertisingVo;
    }

    public void setInfoAdvertisingVo(InfoAdvertisingVoBean infoAdvertisingVo) {
        this.infoAdvertisingVo = infoAdvertisingVo;
    }


}
