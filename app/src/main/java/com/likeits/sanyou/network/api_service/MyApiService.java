package com.likeits.sanyou.network.api_service;


import com.likeits.sanyou.adapter.CategoryInfoEntity;
import com.likeits.sanyou.adapter.ImformationInfoEntity;
import com.likeits.sanyou.adapter.NearFirendInfoEntity;
import com.likeits.sanyou.entity.AddressBean;
import com.likeits.sanyou.entity.BankInfo;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.entity.CenterPosterBean;
import com.likeits.sanyou.entity.CirclePostsBean;
import com.likeits.sanyou.entity.CollectInfoEntity;
import com.likeits.sanyou.entity.EaseUserEntity;
import com.likeits.sanyou.entity.HomeAdInfoEntity;
import com.likeits.sanyou.entity.HomeNewInfoEntity;
import com.likeits.sanyou.entity.InformaitonDetailsInfoEntity;
import com.likeits.sanyou.entity.InformationDetailsCommentInfoEntity;
import com.likeits.sanyou.entity.LoginUserInfoEntity;
import com.likeits.sanyou.entity.ShopHomeInfoEntity;
import com.likeits.sanyou.entity.ShopIndentEntity;
import com.likeits.sanyou.entity.UserInfoEntity;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.ui.shop.bean.ShoppingCartBean;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MyApiService {

    String BASE_URL = "http://semyooapp.wbteam.cn/";
    //全部商品分类接口
    String GetCategory = BASE_URL + "?s=/api/shop/getcategory";
    //商品列表
    String GetShopList = BASE_URL + "?s=/api/shop/getlist";

    //省市县联动接口
    String GetPCD = BASE_URL + "?s=/api/shop/getpcd";
    //添加购物车接口
    String GetAddShop = BASE_URL + "?s=/api/shop/addshopcart";
    //添加下单接口
    String AddShop = BASE_URL + "?s=/api/shop/addshop";
    //广告接口
    String GetGuanggao = BASE_URL + "?s=/api/index/guanggao";
    //页资讯列表接口
    String GetHomeList = BASE_URL + "?s=/api/news/gethomelist";
    //获取用户信息
    String GetInfo = BASE_URL + "?s=/api/member/get_info";
    //我的邮寄地址
    String GETADDRESS = BASE_URL + "?s=/api/shop/getaddress";
    //添加绑定设备接口
    String AddDevice = BASE_URL + "?s=/api/member/adddevice";
    //解绑设备接口
    String EditDevice = BASE_URL + "?s=/api/member/editdevice";
    //获取绑定设备列表接口
    String DeviceList = BASE_URL + "?s=/api/member/devicelist";
    //商品推荐列表接口
    String GetShopHomeList = BASE_URL + "?s=/api/shop/gethomelist";
    //商品搜索接口
    String SearchShop = BASE_URL + "?s=/api/shop/searchshop";
    //微信支付订单接口
    String WXPay = BASE_URL + "?s=/api/pay/wxpay";
    //支付宝支付订单接口
    String Alipay = BASE_URL + "?s=/api/pay/alipay";
    //编辑订单状态接口
    String EditShop = BASE_URL + "?s=/api/shop/editshop";
    //修改用户密码接口
    String ChangePwd = BASE_URL + "?s=/api/member/changepwd";
    //返现列表接口
    String GetCashBack = BASE_URL + "?s=/api/shop/getcashback";
    //上传头像接口
    String UploadImg = BASE_URL + "?s=/api/index/uploadForBase64";
    //我发布的资讯
    String MyListNews = BASE_URL + "?s=/api/news/mylist";
    //帖子列表接口
    String Getulist = BASE_URL + "?s=/api/news/getulist";

    //用户注册接口
    @FormUrlEncoded
    @POST("?s=/api/member/register")
    Observable<HttpResult<EmptyEntity>> RegisterUser(@Field("mobile") String mobile, @Field("password") String password);

    //用户登陆接口
    @FormUrlEncoded
    @POST("?s=/api/member/login")
    Observable<HttpResult<LoginUserInfoEntity>> Login(@Field("mobile") String mobile, @Field("password") String password);

    //上传头像接口
    @FormUrlEncoded
    @POST("?s=/api/member/upimg_base64")
    Observable<HttpResult<EmptyEntity>> UploadImg(@Field("ukey") String ukey, @Field("pic") String pic);

    //编辑用户资料接口
    @FormUrlEncoded
    @POST("?s=/api/member/edit_info")
    Observable<HttpResult<EmptyEntity>> EdidInfo(@Field("ukey") String ukey,
                                                 @Field("nickname") String nickname,
                                                 @Field("qq") String qq,
                                                 @Field("sex") String sex,
                                                 @Field("birthday") String birthday,
                                                 @Field("address") String address
    );

    //获取用户信息接口
    @FormUrlEncoded
    @POST("?s=/api/member/get_info")
    Observable<HttpResult<UserInfoEntity>> GetInfo(@Field("ukey") String ukey);

    //重置密码接口
    @FormUrlEncoded
    @POST("?s=/api/member/resetpwd")
    Observable<HttpResult<EmptyEntity>> ResetPwd(@Field("mobile") String mobile,
                                                 @Field("pwd") String pwd,
                                                 @Field("rpwd") String rpwd);

    //修改用户密码接口
    @FormUrlEncoded
    @POST("?s=/api/member/changepwd")
    Observable<HttpResult<EmptyEntity>> ChangePwd(@Field("ukey") String ukey,
                                                  @Field("old_pwd") String old_pwd,
                                                  @Field("pwd") String pwd,
                                                  @Field("rpwd") String rpwd
    );

    //用户意见反馈接口
    @FormUrlEncoded
    @POST("?s=/api/member/feedback")
    Observable<HttpResult<EmptyEntity>> FeedBack(@Field("ukey") String ukey,
                                                 @Field("mobile") String mobile,
                                                 @Field("email") String email,
                                                 @Field("content") String content
    );

    //获取环信好友列表接口
    @FormUrlEncoded
    @POST("?s=/api/index/get_ease_friend")
    Observable<HttpResult<EaseUserEntity>> GetEaseFriend(@Field("ukey") String ukey
    );

    //获取环信用户信息接口
    @FormUrlEncoded
    @POST("?s=/api/index/get_easemob_userinfo")
    Observable<HttpResult<EmptyEntity>> GetEasemobUserinfo(@Field("ukey") String ukey,
                                                           @Field("easemob_id") String easemob_id
    );

    //获取附近的人列表接口
    @FormUrlEncoded
    @POST("?s=/api/member/distance")
    Observable<HttpResult<ArrayList<NearFirendInfoEntity>>> GetMemberDistance(@Field("ukey") String ukey,
                                                                              @Field("lat") double lat,
                                                                              @Field("lon") double lon
    );
    //资讯

    /**
     * 资讯列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/getlist")
    Observable<HttpResult<ArrayList<ImformationInfoEntity>>> GetNewsList(@Field("ukey") String ukey,
                                                                         @Field("page") String page,
                                                                         @Field("cid") String cid
    );

    /**
     * 资讯详情接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/getdetails")
    Observable<HttpResult<InformaitonDetailsInfoEntity>> GetNewsTails(@Field("ukey") String ukey,
                                                                      @Field("id") String id

    );

    /**
     * 评论列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/getcomment")
    Observable<HttpResult<ArrayList<InformationDetailsCommentInfoEntity>>> GetComment(@Field("ukey") String ukey,
                                                                                      @Field("page") String page,
                                                                                      @Field("id") String id
    );

    /**
     * 评论写入接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/addcomment")
    Observable<HttpResult<ArrayList<InformationDetailsCommentInfoEntity>>> GetAddComment(@Field("ukey") String ukey,
                                                                                         @Field("id") String id,
                                                                                         @Field("content") String content
    );

    /**
     * 资讯点赞接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/likes")
    Observable<HttpResult<EmptyEntity>> GetNewsLikes(@Field("ukey") String ukey,
                                                     @Field("id") String id,
                                                     @Field("status") String status
    );
    //商城

    /**
     * 全部商品分类接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getcategory")
    Observable<HttpResult<EmptyEntity>> GetCategory(@Field("ukey") String ukey

    );

    /**
     * 商品列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getlist")
    Observable<HttpResult<ArrayList<CategoryInfoEntity>>> GetShopList(@Field("ukey") String ukey,
                                                                      @Field("cid") String cid

    );

    /**
     * 商品详情接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getdetails")
    Observable<HttpResult<CategoryDetails>> GetShopDetails(@Field("ukey") String ukey,
                                                           @Field("id") String id
    );

    /**
     * 收藏商品接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/collection")
    Observable<HttpResult<EmptyEntity>> GetShopCollection(@Field("ukey") String ukey,
                                                          @Field("id") String id,
                                                          @Field("status") String status
    );

    /**
     * 我的商品收藏接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getcollectionlist")
    Observable<HttpResult<ArrayList<CollectInfoEntity>>> GetShopCollectionList(@Field("ukey") String ukey
    );

    /**
     * 添加邮寄地址接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/addaddress")
    Observable<HttpResult<EmptyEntity>> GetShopAddAddress(@Field("ukey") String ukey,
                                                          @Field("address") String address,
                                                          @Field("name") String name,
                                                          @Field("phone") String phone,
                                                          @Field("province") String province,
                                                          @Field("city") String city,
                                                          @Field("district") String district
    );

    /**
     * 编辑邮寄地址接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/editaddress")
    Observable<HttpResult<EmptyEntity>> GetShopEditaddress(@Field("ukey") String ukey,
                                                           @Field("id") String id,
                                                           @Field("address") String address,
                                                           @Field("name") String name,
                                                           @Field("phone") String phone,
                                                           @Field("province") String province,
                                                           @Field("city") String city,
                                                           @Field("district") String district
    );

    /**
     * 我的邮寄地址接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getaddress")
    Observable<HttpResult<ArrayList<AddressBean>>> GetAddress(@Field("ukey") String ukey
    );

    /**
     * 删除地址接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/editaddress")
    Observable<HttpResult<EmptyEntity>> GetDelAddress(@Field("ukey") String ukey,
                                                      @Field("id") String id,
                                                      @Field("status") String status
    );

    /**
     * 设置默认邮寄地址接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/editaddress")
    Observable<HttpResult<EmptyEntity>> GetCheckAddress(@Field("ukey") String ukey,
                                                        @Field("id") String id,
                                                        @Field("def") String def
    );

    /**
     * 获取商品|购物车订单接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getshop")
    Observable<HttpResult<ArrayList<ShopIndentEntity>>> GetShop(@Field("ukey") String ukey,
                                                                @Field("status") String status,
                                                                @Field("page") String page
    );

    /**
     * 广告接口
     */
    @FormUrlEncoded
    @POST("?s=/api/index/guanggao")
    Observable<HttpResult<ArrayList<HomeAdInfoEntity>>> GetGuanggao(@Field("ukey") String ukey,
                                                                    @Field("cid") int cid
    );

    /**
     * 首页资讯列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/gethomelist")
    Observable<HttpResult<ArrayList<HomeNewInfoEntity>>> GetHomeList(@Field("ukey") String ukey
    );

    /**
     * 商品推荐列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/gethomelist")
    Observable<HttpResult<ArrayList<ShopHomeInfoEntity>>> GetShopHomeList(@Field("ukey") String ukey
    );

    /**
     * 获取购物车订单接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getshopcart")
    Observable<HttpResult<ArrayList<ShoppingCartBean>>> GetShopCart(@Field("ukey") String ukey
    );


    /**
     * 编辑&删除购物车接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/editbuy")
    Observable<HttpResult<EmptyEntity>> EditBuy(@Field("ukey") String ukey,
                                                @Field("shopid") String shopid,
                                                @Field("num") String num,
                                                @Field("pid") String pid
    );

    /**
     * 提现银行列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/getbank")
    Observable<HttpResult<ArrayList<BankInfo>>> GetBank(@Field("ukey") String ukey
    );

    /**
     * 返现提现接口
     */
    @FormUrlEncoded
    @POST("?s=/api/shop/withdrawals")
    Observable<HttpResult<EmptyEntity>> WithDrawals(@Field("ukey") String ukey,
                                                    @Field("amount") String amount,
                                                    @Field("name") String name,
                                                    @Field("bankid") String bankid,
                                                    @Field("banknumber") String banknumber
    );

    /**
     * 资讯发布接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/addnews")
    Observable<HttpResult<EmptyEntity>> AddNews(@Field("ukey") String ukey,
                                                @Field("title") String title,
                                                @Field("content") String content,
                                                @Field("cover_list") String cover_list
    );

    /**
     * 删除资讯接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/deletenews")
    Observable<HttpResult<EmptyEntity>> DelNews(@Field("ukey") String ukey,
                                                @Field("nid") String nid
    );

    /**
     * 我发布的资讯
     */
    @FormUrlEncoded
    @POST("?s=/api/news/mylist")
    Observable<HttpResult<ArrayList<CenterPosterBean>>> MyListNews(@Field("ukey") String ukey,
                                                                   @Field("page") String page
    );

    /**
     * 帖子列表接口
     */
    @FormUrlEncoded
    @POST("?s=/api/news/getulist")
    Observable<HttpResult<ArrayList<CirclePostsBean>>> Getulist(@Field("ukey") String ukey
    );
}
