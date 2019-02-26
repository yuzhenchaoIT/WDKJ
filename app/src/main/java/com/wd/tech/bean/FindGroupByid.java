package com.wd.tech.bean;

import android.net.Uri;

import java.io.Serializable;

public class FindGroupByid implements Serializable {

    /**
     * currentCount : 2
     * description : 天下第一
     * groupId : 10000
     * groupImage : D:/image/2018-09-19/20180919083221.jpg
     * groupName : 天下第一
     * hxGroupId : 1
     * maxCount : 10
     * ownerUid : 1010
     */

    private int currentCount;
    private String description;
    private int groupId;
    private String groupImage;

    public String getGroupImage() {
        return groupImage;
    }

    private String groupName;
    private String hxGroupId;
    private int maxCount;
    private int ownerUid;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHxGroupId() {
        return hxGroupId;
    }

    public void setHxGroupId(String hxGroupId) {
        this.hxGroupId = hxGroupId;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }
}
