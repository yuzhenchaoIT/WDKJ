package com.wd.tech.core.http;


import com.wd.tech.R;
import com.wd.tech.bean.AllInfo;
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.CommentList;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.FindTitleBean;
import com.wd.tech.bean.FindGroupByid;
import com.wd.tech.bean.FindUserByPhone;
import com.wd.tech.bean.FindUserJoinGroup;
import com.wd.tech.bean.FollowUser;
import com.wd.tech.bean.FriendGroup;
import com.wd.tech.bean.FriendNoticePageList;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.MyPost;
import com.wd.tech.bean.NoticeList;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.TaskList;
import com.wd.tech.bean.User;
import com.wd.tech.bean.UserInRecord;
import com.wd.tech.bean.details.FindAllCommentListBean;
import com.wd.tech.bean.UserPost;
import com.wd.tech.bean.UserIntegral;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.bean.details.VipGoodsBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IRequest {
    /**
     * 注册
     *
     * @param phone
     * @param name
     * @param pwd
     * @return
     */
    @POST("user/v1/register")
    @FormUrlEncoded
    Observable<Result> register(@Field("phone") String phone,
                                @Field("nickName") String name,
                                @Field("pwd") String pwd);

    /**
     * 登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    @POST("user/v1/login")
    @FormUrlEncoded
    Observable<Result<User>> login(@Field("phone") String phone,
                                   @Field("pwd") String pwd);

    /**
     * 微信登录
     *
     * @param ak
     * @param code
     * @return
     */
    @POST("user/v1/weChatLogin")
    @FormUrlEncoded
    Observable<Result<User>> weChatLogin(@Header("ak") String ak,
                                         @Field("code") String code);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/getUserInfoByUserId")
    Observable<Result<QueryUser>> getUserInfoByUserId(@Header("userId") int userId,
                                                      @Header("sessionId") String sessionId);

    /**
     * 完善用户信息
     *
     * @param userId
     * @param sessionId
     * @param name
     * @param sex
     * @param signature
     * @param birthday
     * @param email
     * @return
     */
    @POST("user/verify/v1/perfectUserInfo")
    @FormUrlEncoded
    Observable<Result> perfectUserInfo(@Header("userId") int userId,
                                       @Header("sessionId") String sessionId,
                                       @Field("nickName") String name,
                                       @Field("sex") int sex,
                                       @Field("signature") String signature,
                                       @Field("birthday") String birthday,
                                       @Field("email") String email);

    /**
     * 用户收藏列表
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findAllInfoCollection")
    Observable<Result<List<AllInfo>>> findAllInfoCollection(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("page") int page,
                                                            @Query("count") int count);

    /**
     * 添加收藏
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @POST("user/verify/v1/addCollection")
    @FormUrlEncoded
    Observable<Result> addCollection(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("infoId") int infoId);


    /**
     * 取消收藏（支持批量操作）
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @DELETE("user/verify/v1/cancelCollection")
    Observable<Result> cancelCollection(@Header("userId") int userId,
                                        @Header("sessionId") String sessionId,
                                        @Query("infoId") String infoId);

    /**
     * 用户关注列表
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findFollowUserList")
    Observable<Result<List<FollowUser>>> findFollowUserList(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("page") int page,
                                                            @Query("count") int count);

    /**
     * 取消关注
     *
     * @param userId
     * @param sessionId
     * @param focusId
     * @return
     */
    @DELETE("user/verify/v1/cancelFollow")
    Observable<Result> cancelFollow(@Header("userId") int userId,
                                    @Header("sessionId") String sessionId,
                                    @Query("focusId") int focusId);

    /**
     * 资讯推荐展示列表
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("information/v1/infoRecommendList")
    Observable<Result<List<HomeListBean>>> recommendList(@Header("userId") int userId,
                                                         @Header("sessionId") String sessionId,
                                                         @Query("plateId") int plateId,
                                                         @Query("page") int page,
                                                         @Query("count") int count);

    /**
     * 修改用户签名
     *
     * @param userId
     * @param sessionId
     * @param signature
     * @return
     */
    @PUT("user/verify/v1/modifySignature")
    Observable<Result> modifySignature(@Header("userId") int userId,
                                       @Header("sessionId") String sessionId,
                                       @Query("signature") String signature);

    /**
     * 我的帖子
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("community/verify/v1/findMyPostById")
    Observable<Result<List<MyPost>>> findMyPostById(@Header("userId") int userId,
                                                    @Header("sessionId") String sessionId,
                                                    @Query("page") int page,
                                                    @Query("count") int count);

    /**
     * 删除帖子
     *
     * @param userId
     * @param sessionId
     * @param commun
     * @return
     */
    @DELETE("community/verify/v1/deletePost")
    Observable<Result> deletePost(@Header("userId") int userId,
                                  @Header("sessionId") String sessionId,
                                  @Query("communityId") String commun);

    /**
     * 签到
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @POST("user/verify/v1/userSign")
    Observable<Result> userSign(@Header("userId") int userId,
                                @Header("sessionId") String sessionId);

    /**
     * 查询当天签到状态
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserSignStatus")
    Observable<Result> findUserSignStatus(@Header("userId") int userId,
                                          @Header("sessionId") String sessionId);

    /**
     * 查询用户连续签到天数
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findContinuousSignDays")
    Observable<Result> findContinuousSignDays(@Header("userId") int userId,
                                              @Header("sessionId") String sessionId);

    /**
     * 查询用户当月所有签到的日期
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserSignRecording")
    Observable<Result> findUserSignRecording(@Header("userId") int userId,
                                             @Header("sessionId") String sessionId);

    /**
     * 查询用户任务列表
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserTaskList")
    Observable<Result<List<TaskList>>> findUserTaskList(@Header("userId") int userId,
                                                        @Header("sessionId") String sessionId);

    /**
     * 查询用户系统通知
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("tool/verify/v1/findSysNoticeList")
    Observable<Result<List<NoticeList>>> findSysNoticeList(@Header("userId") int userId,
                                                           @Header("sessionId") String sessionId,
                                                           @Query("page") int page,
                                                           @Query("count") int count);








    /**
     * 查询用户积分
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserIntegral")
    Observable<Result<UserIntegral>> findUserIntegral(@Header("userId") int userId,
                                                      @Header("sessionId") String sessionId);

    /**
     * 查询用户积分明细
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findUserIntegralRecord")
    Observable<Result<List<UserInRecord>>> findUserIntegralRecord(@Header("userId") int userId,
                                                                  @Header("sessionId") String sessionId,
                                                                  @Query("page") int page,
                                                                  @Query("count") int count);

    /**
     * 修改用户密码
     *
     * @param userId
     * @param sessionId
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @PUT("user/verify/v1/modifyUserPwd")
    @FormUrlEncoded
    Observable<Result> modifyUserPwd(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("oldPwd") String oldPwd,
                                     @Field("newPwd") String newPwd);






    /**
     * 用户上传头像
     *
     * @param userId
     * @param sessionId
     * @param body
     * @return
     */
    @POST("user/verify/v1/modifyHeadPic")
    Observable<Result> modifyHeadPic(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Body MultipartBody body);

    /**
     * 资讯分类跳转列表
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("information/v1/infoRecommendList")
    Observable<Result<List<HomeListBean>>> plateList(@Header("userId") int userId,
                                                     @Header("sessionId") String sessionId,
                                                     @Query("plateId") int plateId,
                                                     @Query("page") int page,
                                                     @Query("count") int count);

    /**
     * 轮播图
     *
     * @return
     */
    @GET("information/v1/bannerShow")
    Observable<Result<List<BannerBean>>> bannerShow();

    /**
     * 社区列表
     */
    @GET("community/v1/findCommunityList")
    Observable<Result<List<CommunityListBean>>> communityList(
            @Header("userId") int userId, @Header("sessionId") String sessionId,
            @Query("page") int page, @Query("count") int count);

    @GET("chat/verify/v1/initFriendList")
    Observable<Result<List<InitFriendlist>>> groupList(
//            @Header("userId") int userId, @Header("sessionId") String sessionId,
            @Header("userId") int userId,
            @Header("sessionId") String sessionId);

    /**
     * 所有分类板块查询
     *
     * @return
     */
    @GET("information/v1/findAllInfoPlate")
    Observable<Result<List<AllInfoPlateBean>>> allInfoPlate();

    @GET("information/v1/findInformationDetails")
    Observable<Result<InforDetailsBean>> informationDetails(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("id") int id);

    /**
     * 发布圈子
     *
     * @param userId
     * @param sessionId
     * @param body
     * @return
     */
    @POST("community/verify/v1/releasePost")
    Observable<Result> fabuquanzi(@Header("userId") int userId, @Header("sessionId") String sessionId, @Body MultipartBody body);

    /**
     * 根据手机号查询用户信息
     *
     * @param userId
     * @param sessionId
     * @param phone
     * @return
     */
    @GET("user/verify/v1/findUserByPhone")
    Observable<Result<FindUserByPhone>> findUserByPhone(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("phone") String phone
    );

    /**
     * 添加好友
     *
     * @param userId
     * @param sessionId
     * @param friendUid
     * @param remark
     * @return
     */
    @POST("chat/verify/v1/addFriend")
    Observable<Result> addFriend(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("friendUid") int friendUid,
            @Query("remark") String remark
    );


    /**
     * 资讯搜索的接口(根据标题模糊查询)
     * information/v1/findInformationByTitle
     */
    @GET("information/v1/findInformationByTitle")
    Observable<Result<List<FindTitleBean>>> getFindTitle(@Query("title") String title,
                                                         @Query("page") int page,
                                                         @Query("count") int count);


    /**
     * 评论列表
     *
     * @param userId
     * @param sessionId
     * @param communityId
     * @param page
     * @param count
     * @return
     */
    @GET("community/v1/findCommunityUserCommentList")
    Observable<Result<List<CommentList>>> findCommunityUserCommentList(@Header("userId") int userId,
                                                                       @Header("sessionId") String sessionId,
                                                                       @Query("communityId") int communityId,
                                                                       @Query("page") int page,
                                                                       @Query("count") int count);

    /**
     * 圈子发布
     *
     * @param userId
     * @param sessionId
     * @param communityId
     * @param content
     * @return
     */
    @POST("community/verify/v1/addCommunityComment")
    @FormUrlEncoded
    Observable<Result> addCommunityComment(@Header("userId") int userId,
                                           @Header("sessionId") String sessionId,
                                           @Field("communityId") int communityId,
                                           @Field("content") String content);

    /**
     * 根据id查组
     *
     * @param userId
     * @param sessionId
     * @param groupId
     * @return
     */
    @GET("group/verify/v1/findGroupInfo")
    Observable<Result<FindGroupByid>> findGroupByid(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("groupId") int groupId
    );

    /**
     * 添加群
     *
     * @param userId
     * @param sessionId
     * @param groupId
     * @param remark
     * @return
     */
    @POST("group/verify/v1/applyAddGroup")
    Observable<Result> addGroup(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("groupId") int groupId,
            @Query("remark") String remark
    );

    /**
     * 创建群
     *
     * @param userId
     * @param sessionId
     * @param name
     * @param description
     * @return
     */
    @POST("group/verify/v1/createGroup")
    Observable<Result> createGroup(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("name") String name,
            @Query("description") String description
    );

    /**
     * 查询加入的群
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("group/verify/v1/findUserJoinedGroup")
    Observable<Result<List<FindUserJoinGroup>>> findUserJoinedGroup(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId
    );

    /**
     * 查看好友通知
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("chat/verify/v1/findFriendNoticePageList")
    Observable<Result<List<FriendNoticePageList>>> friendNoticePageList(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("page") int page,
            @Query("count") int count
    );

    @PUT("chat/verify/v1/reviewFriendApply")
    Observable<Result> reviewFriend(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("noticeId") int noticeId,
            @Query("flag") int flag
    );

    /**
     * 删除好友
     *
     * @param userId
     * @param sessionId
     * @param friendUid
     * @return
     */
    @DELETE("chat/verify/v1/deleteFriendRelation")
    Observable<Result> deleteFriendRelation(@Header("userId") int userId,
                                            @Header("sessionId") String sessionId,
                                            @Query("friendUid") int friendUid);

    /**
     * 转移好友到其他分组
     *
     * @param userId
     * @param sessionId
     * @param newGroupId
     * @param friendUid
     * @return
     */
    @PUT("chat/verify/v1/transferFriendGroup")
    @FormUrlEncoded
    Observable<Result> transferFriendGroup(@Header("userId") int userId,
                                           @Header("sessionId") String sessionId,
                                           @Field("newGroupId") int newGroupId,
                                           @Field("friendUid") int friendUid);


    /**
     * 做任务
     *
     * @param userId
     * @param sessionId
     * @param taskId
     * @return
     */
    @POST("user/verify/v1/doTheTask")
    @FormUrlEncoded
    Observable<Result> doTheTask(@Header("userId") int userId,
                                 @Header("sessionId") String sessionId,
                                 @Field("taskId") int taskId);
    /**
     * 修改用户昵称
     *
     * @param userId
     * @param sessionId
     * @param nickName
     * @return
     */
    @PUT("usererify1/modifyNickName")
    @FormUrlEncoded
    Observable<Result> modifyNickName(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("nickName") String nickName);
    /**
     * 修改邮箱
     *
     * @param userId
     * @param sessionId
     * @param email
     * @return
     */
    @PUT("user/verify/v1/modifyEmail")
    @FormUrlEncoded
    Observable<Result> modifyEmail(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Field("email") String email);

    /**
     * 点赞
     * @param userId
     * @param sessionId
     * @param communityId
     * @return
     */
    @POST("community/verify/v1/addCommunityGreat")
    @FormUrlEncoded
    Observable<Result> addCommunityGreat(@Header("userId") int userId,
                                         @Header("sessionId") String sessionId,
                                         @Field("communityId") int communityId);

    /**
     * 取消点赞
     * @param userId
     * @param sessionId
     * @param communityId
     * @return
     */
    @DELETE("community/verify/v1/cancelCommunityGreat")
    Observable<Result> cancelCommunityGreat(@Header("userId") int userId,
                                            @Header("sessionId") String sessionId,
                                            @Query("communityId") int communityId);
    /**
     * 详情页面的评论查看
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @param page
     * @param count
     * @return
     */

    @GET("information/v1/findAllInfoCommentList")
    Observable<Result<List<FindAllCommentListBean>>> findAllInfoCommentList(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("infoId") int infoId,
            @Query("page") int page,
            @Query("count") int count);

    /**
     * 资讯用户评论
     *
     * @param userId
     * @param sessionId
     * @param content
     * @param infoId
     * @return
     */
    @POST("information/verify/v1/addInfoComment")
    @FormUrlEncoded
    Observable<Result> addInfoComment(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("content") String content,
                                      @Field("infoId") int infoId);


    /**
     * 查看别人的社区
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @POST("user/verify/v1/addFollow")
    @FormUrlEncoded
    Observable<Result> addFollow(@Header("userId") int userId,
                                 @Header("sessionId") String sessionId,
                                 @Field("focusId") int focusId);
    @GET("community/verify/v1/findUserPostById")
    Observable<Result<List<UserPost>>> findUserPostById(
            @Header("userId") int userId,
            @Header("sessionId") String sessionId,
            @Query("fromUid") int fromUid,
            @Query("page") int page,
            @Query("count") int count);

    /**
     * 资讯积分兑换
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @param integralCost
     * @return
     */
    @POST("information/verify/v1/infoPayByIntegral")
    @FormUrlEncoded
    Observable<Result> infoPayByIntegral(@Header("userId") int userId,
                                         @Header("sessionId") String sessionId,
                                         @Field("infoId") int infoId,
                                         @Field("integralCost") int integralCost);

    /**
     * 查询所有会员商品
     *
     * @return
     */
    @GET("tool/v1/findVipCommodityList")
    Observable<Result<List<VipGoodsBean>>> findVipCommodityList();

    /**
     * 用户购买VIP
     *
     * @param userId
     * @param sessionId
     * @param commodityId
     * @param sign
     * @return
     */

    @POST("tool/verify/v1/buyVip")
    @FormUrlEncoded
    Observable<Result> buyVip(@Header("userId") int userId,
                              @Header("sessionId") String sessionId,
                              @Field("commodityId") int commodityId,
                              @Field("sign") String sign);
    /**
     * 查询个人群
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("chat/verify/v1/findFriendGroupList")
    Observable<Result<List<FriendGroup>>> findFriendGroupList(@Header("userId") int userId,
                                                              @Header("sessionId") String sessionId);

    /**
     * 创建自定义好友分组
     * @param userId
     * @param sessionId
     * @param groupName
     * @return
     */
    @POST("chat/verify/v1/addFriendGroup")
    @FormUrlEncoded
    Observable<Result> addFriendGroup(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("groupName") String groupName);

    /**
     * 删除好友聊天记录
     * @param userId
     * @param sessionId
     * @param friendUid
     * @return
     */
    @DELETE("chat/verify/v1/deleteChatRecord")
    Observable<Result> deleteChatRecord(@Header("userId") int userId,
                                        @Header("sessionId")String sessionId,
                                        @Query("friendUid") int friendUid);

    /**
     * 微信支付
     *
     * @param userId
     * @param sessionId
     * @param orderId
     * @param payType
     * @return
     */
    @POST("tool/verify/v1/pay")
    @FormUrlEncoded
    Observable<Result<String>> wxPay(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("orderId") String orderId,
                                     @Field("payType") int payType);

    /**
     * 详情页面  点赞
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */

    @POST("information/verify/v1/addGreatRecord")
    @FormUrlEncoded
    Observable<Result> addGreatRecord(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("infoId") int infoId);

    /**
     * 详情页面  取消点赞
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */

    @DELETE("information/verify/v1/cancelGreat")
    Observable<Result> cancelGreat(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Query("infoId") int infoId);


    /***
     * 绑定faceId
     * @param userId
     * @param sessionId
     * @param featureInfo
     * @return
     */
    @PUT("user/verify/v1/bindingFaceId")
    @FormUrlEncoded
    Observable<Result> bindingFaceId(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("featureInfo")String featureInfo);


    /**
     * 刷脸登陆
     * @param faceId
     * @return
     */
    @POST("user/v1/faceLogin")
    @FormUrlEncoded
    Observable<Result<User>> faceLogin(@Field("faceId")String faceId);


}
