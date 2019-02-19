package com.wd.tech.bean;

public class BannerBean {

    /**
     * imageUrl : http://172.17.8.100/images/tech/banner/073040514318.jpg
     * jumpUrl : wd://information?infoId=1
     * rank : 1
     * title : 关于滴滴顺风车事件的几点思考
     */

    private String imageUrl;
    private String jumpUrl;
    private int rank;
    private String title;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
