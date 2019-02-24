package com.wd.tech.bean;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/21
 */
public class FriendInfoList {
    int friendUid;
    String nickName;
    String remarkName;
    String headPic;
    String signature;
    String userName;

    public int getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(int friendUid) {
        this.friendUid = friendUid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
