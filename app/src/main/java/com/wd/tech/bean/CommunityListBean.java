package com.wd.tech.bean;

import java.util.List;

public class CommunityListBean {
    /**
     * comment : 0
     * communityCommentVoList : []
     * content :
     * file : http://mobile.bwstudent.com/images/tech/community_pic/2019-02-18/4059820190218231746.png
     * headPic : http://thirdwx.qlogo.cn/mmopen/vi_32/LyklBLYRZ7K1MYMbLezsYm6Zia2vIiaFGfliavXXklia8sOZXrACmvmMRwJHBv5J5hUdR7yIETFDNbg9d5vjmeiaLdA/132
     * id : 13
     * nickName : 杠杠了
     * praise : 2
     * publishTime : 1550503066000
     * userId : 6
     * whetherFollow : 2
     * whetherGreat : 2
     * whetherVip : 2
     * signature : 期待
     */

    private int comment;
    private String content;
    private String file;
    private String headPic;
    private int id;
    private String nickName;
    private int praise;
    private long publishTime;
    private int userId;
    private int whetherFollow;
    private int whetherGreat;
    private int whetherVip;
    private String signature;
    private List<PlList> communityCommentVoList;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public List<PlList> getCommunityCommentVoList() {
        return communityCommentVoList;
    }

    public void setCommunityCommentVoList(List<PlList> communityCommentVoList) {
        this.communityCommentVoList = communityCommentVoList;
    }

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

    public int getWhetherGreat() {
        return whetherGreat;
    }

    public void setWhetherGreat(int whetherGreat) {
        this.whetherGreat = whetherGreat;
    }

    public int getWhetherVip() {
        return whetherVip;
    }

    public void setWhetherVip(int whetherVip) {
        this.whetherVip = whetherVip;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "CommunityListBean{" +
                "comment=" + comment +
                ", content='" + content + '\'' +
                ", file='" + file + '\'' +
                ", headPic='" + headPic + '\'' +
                ", id=" + id +
                ", nickName='" + nickName + '\'' +
                ", praise=" + praise +
                ", publishTime=" + publishTime +
                ", userId=" + userId +
                ", whetherFollow=" + whetherFollow +
                ", whetherGreat=" + whetherGreat +
                ", whetherVip=" + whetherVip +
                ", signature='" + signature + '\'' +
                ", communityCommentVoList=" + communityCommentVoList +
                '}';
    }
}
