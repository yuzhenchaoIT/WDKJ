package com.wd.tech.bean.details;

public class VipGoodsBean {


    /**
     * commodityId : 1001
     * commodityName : 会员周卡
     * imageUrl : http://172.17.8.100/images/tech/community_pic/vip_zk.jpg
     * price : 0.01
     */

    private int commodityId;
    private String commodityName;
    private String imageUrl;
    private double price;

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
