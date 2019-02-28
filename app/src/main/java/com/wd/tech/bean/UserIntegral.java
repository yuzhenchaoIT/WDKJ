package com.wd.tech.bean;

public class UserIntegral {

    /**
     * amount : 0
     * id : 2
     * updateTime : 1538099908000
     * userId : 1010
     */

    private int amount;
    private int id;
    private long updateTime;
    private int userId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserIntegral{" +
                "amount=" + amount +
                ", id=" + id +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                '}';
    }
}
