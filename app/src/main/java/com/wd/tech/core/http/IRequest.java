package com.wd.tech.core.http;


import com.wd.tech.R;
import com.wd.tech.bean.AllInfo;
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.FindUserByPhone;
import com.wd.tech.bean.FollowUser;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.MyPost;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.details.InforDetailsBean;

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
     * @param ak
     * @param code
     * @return
     */
    @POST("user/v1/weChatLogin")
    @FormUrlEncoded
    Observable<Result<User>> weChatLogin(@Header("ak")String ak,
                                         @Field("code")String code);

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
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findAllInfoCollection")
    Observable<Result<List<AllInfo>>> findAllInfoCollection(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("page")int page,
                                                            @Query("count")int count);

    /**
     * 取消收藏（支持批量操作）
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @DELETE("user/verify/v1/cancelCollection")
    Observable<Result> cancelCollection(@Header("userId") int userId,
                                        @Header("sessionId") String sessionId,
                                        @Query("infoId")String infoId);

    /**
     * 用户关注列表
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findFollowUserList")
    Observable<Result<List<FollowUser>>> findFollowUserList(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("page")int page,
                                                            @Query("count")int count);

    /**
     * 取消关注
     * @param userId
     * @param sessionId
     * @param focusId
     * @return
     */
    @DELETE("user/verify/v1/cancelFollow")
    Observable<Result> cancelFollow(@Header("userId") int userId,
                                    @Header("sessionId") String sessionId,
                                    @Query("focusId")int focusId);
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
     * @param userId
     * @param sessionId
     * @param signature
     * @return
     */
    @PUT("user/verify/v1/modifySignature")
    Observable<Result> modifySignature(@Header("userId") int userId,
                                       @Header("sessionId") String sessionId,
                                       @Query("signature")String signature);

    /**
     * 我的帖子
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
     * @param userId
     * @param sessionId
     * @param commun
     * @return
     */
    @DELETE("community/verify/v1/deletePost")
    Observable<Result> deletePost(@Header("userId") int userId,
                                  @Header("sessionId") String sessionId,
                                  @Query("communityId")String commun);

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
//            @Header("userId") int userId, @Header("sessionId") String sessionId,
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


    @POST("community/verify/v1/releasePost")
    Observable<Result> fabuquanzi(@Header("userId") int userId, @Header("sessionId") String sessionId, @Body MultipartBody body);
    /**
     * 根据手机号查询用户信息
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
}
