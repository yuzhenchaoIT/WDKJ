package com.wd.tech.bean;

public class UserInRecord {
    private int direction;
    private int type;
    private int amount;
    private long createTime;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserInRecord{" +
                "direction=" + direction +
                ", type=" + type +
                ", amount=" + amount +
                ", createTime=" + createTime +
                '}';
    }
}
