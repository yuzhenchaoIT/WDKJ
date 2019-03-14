package com.wd.tech.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    /**
     * nickName : 徐云杰
     * phone : 18600151568
     * pwd : R+0jdN3P4MXHPMFVe9cX5MbX5ulIXHJkfigPLKEeTBY5lUgxJWUNg0js1oGtbsKiLFL4ScqdmUbtHXIfrgQnWrwTNjf09OJLycbeJ+ka4+CV7I1eEqG8DtZPnQoCyxjoYMjO4soDl6EX9YgqaZp3DlUH4pXrYHYz58YyFkSeJEk=
     * sessionId : 15372410025241007
     * userId : 1007
     * userName : 4Vg15Z18600151568
     * whetherVip : 2
     * whetherFaceId : 1
     */
    @Id
    private long id;
    private String nickName;
    private String phone;
    private String pwd;
    private String sessionId;
    private int userId;
    private String userName;
    private int whetherVip;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    private int whetherFaceId;
    private String statu;
    private String headPic;
    @Generated(hash = 155928060)
    public User(long id, String nickName, String phone, String pwd, String sessionId, int userId, String userName, int whetherVip, int whetherFaceId, String statu, String headPic) {
        this.id = id;
        this.nickName = nickName;
        this.phone = phone;
        this.pwd = pwd;
        this.sessionId = sessionId;
        this.userId = userId;
        this.userName = userName;
        this.whetherVip = whetherVip;
        this.whetherFaceId = whetherFaceId;
        this.statu = statu;
        this.headPic = headPic;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWhetherVip() {
        return whetherVip;
    }

    public void setWhetherVip(int whetherVip) {
        this.whetherVip = whetherVip;
    }

    public int getWhetherFaceId() {
        return whetherFaceId;
    }

    public void setWhetherFaceId(int whetherFaceId) {
        this.whetherFaceId = whetherFaceId;
    }


}
