package com.wd.tech.bean;

public class CommunityUserVoBean {
    private String headPic;
    private String nickName;
    private int userId;
    private int whetherFollow;
    private int whetherMyFriend;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWhetherFollow() {
        return whetherFollow;
    }

    public void setWhetherFollow(int whetherFollow) {
        this.whetherFollow = whetherFollow;
    }

    public int getWhetherMyFriend() {
        return whetherMyFriend;
    }

    public void setWhetherMyFriend(int whetherMyFriend) {
        this.whetherMyFriend = whetherMyFriend;
    }
}
