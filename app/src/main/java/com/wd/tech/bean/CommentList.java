package com.wd.tech.bean;

public class CommentList {

    /**
     * content : 首发
     * headPic : D:/image/2018-09-19/20180919083221.jpg
     * nickName : 小明
     * commentTime : 1538040675000
     * userId : 1012
     */

    private String content;
    private String headPic;
    private String nickName;
    private long commentTime;
    private int userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommentList{" +
                "content='" + content + '\'' +
                ", headPic='" + headPic + '\'' +
                ", nickName='" + nickName + '\'' +
                ", commentTime=" + commentTime +
                ", userId=" + userId +
                '}';
    }
}
