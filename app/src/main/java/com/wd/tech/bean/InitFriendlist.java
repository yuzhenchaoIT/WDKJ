package com.wd.tech.bean;

import java.util.List;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/21
 */
public class InitFriendlist {
    private int black;

    private int currentNumber;

    private int customize;

    private List<FriendInfoList> friendInfoList ;

    private int groupId;

    private String groupName;

    public void setBlack(int black){
        this.black = black;
    }
    public int getBlack(){
        return this.black;
    }
    public void setCurrentNumber(int currentNumber){
        this.currentNumber = currentNumber;
    }
    public int getCurrentNumber(){
        return this.currentNumber;
    }
    public void setCustomize(int customize){
        this.customize = customize;
    }
    public int getCustomize(){
        return this.customize;
    }
    public void setFriendInfoList(List<FriendInfoList> friendInfoList){
        this.friendInfoList = friendInfoList;
    }
    public List<FriendInfoList> getFriendInfoList(){
        return this.friendInfoList;
    }
    public void setGroupId(int groupId){
        this.groupId = groupId;
    }
    public int getGroupId(){
        return this.groupId;
    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return this.groupName;
    }



}
