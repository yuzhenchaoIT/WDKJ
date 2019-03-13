package com.wd.tech.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Conversation {
    private String headPic;
    private String nickName;
    private String pwd;
    @Id(autoincrement = true)
    private long userId;
    private String userName;
    @Generated(hash = 895508773)
    public Conversation(String headPic, String nickName, String pwd, long userId,
            String userName) {
        this.headPic = headPic;
        this.nickName = nickName;
        this.pwd = pwd;
        this.userId = userId;
        this.userName = userName;
    }
    @Generated(hash = 1893991898)
    public Conversation() {
    }
    public String getHeadPic() {
        return this.headPic;
    }
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
