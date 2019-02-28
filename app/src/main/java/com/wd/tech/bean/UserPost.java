package com.wd.tech.bean;
import java.util.List;

public class UserPost {

    /**
     * communityUserPostVoList : [{"comment":0,"content":"哈喽","file":"","id":418,"praise":1,"whetherGreat":2},{"comment":0,"content":"谁删的帖子","file":"","id":417,"praise":1,"whetherGreat":2}]
     * communityUserVo : {"headPic":"http://172.17.8.100/images/tech/default/tech.jpg","nickName":"Game player","userId":1021,"whetherFollow":2,"whetherMyFriend":2}
     */

    List<CommunityUserPostVoListBean> communityUserPostVoList;
    CommunityUserVoBean communityUserVo;

    public List<CommunityUserPostVoListBean> getCommunityUserPostVoList() {
        return communityUserPostVoList;
    }

    public void setCommunityUserPostVoList(List<CommunityUserPostVoListBean> communityUserPostVoList) {
        this.communityUserPostVoList = communityUserPostVoList;
    }

    public CommunityUserVoBean getCommunityUserVo() {
        return communityUserVo;
    }

    public void setCommunityUserVo(CommunityUserVoBean communityUserVo) {
        this.communityUserVo = communityUserVo;
    }
}
