package com.wd.tech.bean;

public class FriendGroup {

    /**
     * currentNumber : 0
     * customize : 1
     * groupId : 274
     * groupName : 我的好友
     */

    private int currentNumber;
    private int customize;
    private int groupId;
    private String groupName;

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public int getCustomize() {
        return customize;
    }

    public void setCustomize(int customize) {
        this.customize = customize;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
