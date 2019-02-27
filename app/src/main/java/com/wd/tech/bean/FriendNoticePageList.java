package com.wd.tech.bean;

public class FriendNoticePageList {

    /**
     * fromHeadPic : http://172.17.8.100/images/tech/head_pic/2018-09-29/20180929102959.jpg
     * fromNickName : 小白
     * fromUid : 1012
     * noticeId : 167
     * noticeTime : 1538963511000
     * receiveUid : 1078
     * remark : 加个好友
     * status : 1
     */

    private String fromHeadPic;
    private String fromNickName;
    private int fromUid;
    private int noticeId;
    private long noticeTime;
    private int receiveUid;
    private String remark;
    private int status;

    public String getFromHeadPic() {
        return fromHeadPic;
    }

    public void setFromHeadPic(String fromHeadPic) {
        this.fromHeadPic = fromHeadPic;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public long getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(long noticeTime) {
        this.noticeTime = noticeTime;
    }

    public int getReceiveUid() {
        return receiveUid;
    }

    public void setReceiveUid(int receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
