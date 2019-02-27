package com.wd.tech.bean;

public class MyPost {

    /**
     * comment : 0
     * content : 首发
     * file : http://172.17.8.100/images/tech/head_pic/2018-09-20/20180920081958.jpg
     * headPic : D:/image/2018-09-19/20180919083221.jpg
     * id : 23
     * nickName : 小明
     * praise : 0
     * publishTime : 1538098426000
     * signature : 秋天不回来
     * userId : 1012
     */

    private int comment;
    private String content;
    private String file;
    private String headPic;
    private int id;
    private String nickName;
    private int praise;
    private long publishTime;
    private String signature;
    private int userId;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
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

    @Override
    public String toString() {
        return "MyPost{" +
                "comment=" + comment +
                ", content='" + content + '\'' +
                ", file='" + file + '\'' +
                ", headPic='" + headPic + '\'' +
                ", id=" + id +
                ", nickName='" + nickName + '\'' +
                ", praise=" + praise +
                ", publishTime=" + publishTime +
                ", signature='" + signature + '\'' +
                ", userId=" + userId +
                '}';
    }
}
