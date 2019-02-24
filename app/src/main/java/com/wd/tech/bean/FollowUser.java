package com.wd.tech.bean;

public class FollowUser {

    /**
     * focusTime : 1539910015000
     * focusUid : 1011
     * headPic : http://172.17.8.100/images/tech/head_pic/2018-10-17/20181017141335.png
     * nickName : 迪玛希
     * signature : 123
     * userId : 1018
     * whetherMutualFollow : 2
     * whetherVip : 2
     */

    private long focusTime;
    private int focusUid;
    private String headPic;
    private String nickName;
    private String signature;
    private int userId;
    private int whetherMutualFollow;
    private int whetherVip;

    public long getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(long focusTime) {
        this.focusTime = focusTime;
    }

    public int getFocusUid() {
        return focusUid;
    }

    public void setFocusUid(int focusUid) {
        this.focusUid = focusUid;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWhetherMutualFollow() {
        return whetherMutualFollow;
    }

    public void setWhetherMutualFollow(int whetherMutualFollow) {
        this.whetherMutualFollow = whetherMutualFollow;
    }

    public int getWhetherVip() {
        return whetherVip;
    }

    public void setWhetherVip(int whetherVip) {
        this.whetherVip = whetherVip;
    }

    @Override
    public String toString() {
        return "FollowUser{" +
                "focusTime=" + focusTime +
                ", focusUid=" + focusUid +
                ", headPic='" + headPic + '\'' +
                ", nickName='" + nickName + '\'' +
                ", signature='" + signature + '\'' +
                ", userId=" + userId +
                ", whetherMutualFollow=" + whetherMutualFollow +
                ", whetherVip=" + whetherVip +
                '}';
    }
}
