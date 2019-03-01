package com.wd.tech.bean.details;

public class FindAllCommentListBean {


    /**
     * commentTime : 1538030031000
     * content : 不错
     * id : 10
     * infoId : 3
     * nickName : 小明
     * headPic : url
     * userId : 1010
     */

    private long commentTime;
    private String content;
    private int id;
    private int infoId;
    private String nickName;
    private String headPic;
    private int userId;

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
