package com.wd.tech.core.http;


import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRequest {
    /**
     * 注册
     * @param phone
     * @param name
     * @param pwd
     * @return
     */
    @POST("user/v1/register")
    @FormUrlEncoded
    Observable<Result> register(@Field("phone")String phone,
                                @Field("nickName")String name,
                                @Field("pwd")String pwd);

    /**
     * 登录
     * @param phone
     * @param pwd
     * @return
     */
    @POST("user/v1/login")
    @FormUrlEncoded
    Observable<Result<User>> login(@Field("phone")String phone,
                                   @Field("pwd")String pwd);

    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/getUserInfoByUserId")
    Observable<Result<QueryUser>> getUserInfoByUserId(@Header("userId") int userId,
                                                      @Header("sessionId") String sessionId);

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

}
