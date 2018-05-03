package com.likeits.sanyou.network;


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
import com.likeits.sanyou.network.entity.BaseHttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.shop.bean.ShoppingCartBean;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpMethods extends BaseHttpMethods {


    private HttpMethods() {
        super();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void RegisterUser(MySubscriber<EmptyEntity> subscriber, String mobile, String password) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.RegisterUser(mobile, password);
        toSubscribe(observable, subscriber);
    }

    public void Login(Subscriber<HttpResult<LoginUserInfoEntity>> subscriber, String mobile, String password) {
        Observable<HttpResult<LoginUserInfoEntity>> observable = myApiService.Login(mobile, password);
        toSubscribe(observable, subscriber);
    }

    public void ResetPwd(MySubscriber<EmptyEntity> subscriber, String mobile, String password, String rpwd) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.ResetPwd(mobile, password, rpwd);
        toSubscribe(observable, subscriber);
    }

    public void UploadImg(MySubscriber<EmptyEntity> subscriber, String ukey, String pic) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.UploadImg(ukey, pic);
        toSubscribe(observable, subscriber);
    }

    public void EdidInfo(MySubscriber<EmptyEntity> subscriber, String ukey, String nickname, String qq, String sex, String birthday, String address) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.EdidInfo(ukey, nickname, qq, sex, birthday, address);
        toSubscribe(observable, subscriber);
    }

    public void GetInfo(MySubscriber<UserInfoEntity> subscriber, String ukey) {
        Observable<HttpResult<UserInfoEntity>> observable = myApiService.GetInfo(ukey);
        toSubscribe(observable, subscriber);
    }

    public void ChangePwd(MySubscriber<EmptyEntity> subscriber, String ukey, String old_pwd, String pwd, String rpwd) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.ChangePwd(ukey, old_pwd, pwd, rpwd);
        toSubscribe(observable, subscriber);
    }

    public void FeedBack(MySubscriber<EmptyEntity> subscriber, String ukey, String mobile, String email, String content) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.FeedBack(ukey, mobile, email, content);
        toSubscribe(observable, subscriber);
    }

    /**
     * 环信好友列表
     */
    public void GetEaseFriend(MySubscriber<EaseUserEntity> subscriber, String ukey) {
        Observable<HttpResult<EaseUserEntity>> observable = myApiService.GetEaseFriend(ukey);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取环信用户信息接口
     */
    public void GetEasemobUserinfo(MySubscriber<EmptyEntity> subscriber, String ukey, String easemob_id) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetEasemobUserinfo(ukey, easemob_id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取环信用户信息接口
     */
    public void GetMemberDistance(MySubscriber<ArrayList<NearFirendInfoEntity>> subscriber, String ukey, double lat, double lon) {
        Observable observable = myApiService.GetMemberDistance(ukey, lat, lon);
        toSubscribe(observable, subscriber);
    }

    public void GetNewsList(MySubscriber<ArrayList<ImformationInfoEntity>> subscriber, String ukey, String page, String cid) {
        Observable observable = myApiService.GetNewsList(ukey, page, cid);
        toSubscribe(observable, subscriber);
    }

    public void GetNewsTails(MySubscriber<InformaitonDetailsInfoEntity> subscriber, String ukey, String id) {
        Observable<HttpResult<InformaitonDetailsInfoEntity>> observable = myApiService.GetNewsTails(ukey, id);
        toSubscribe(observable, subscriber);
    }

    public void GetComment(MySubscriber<ArrayList<InformationDetailsCommentInfoEntity>> subscriber, String ukey, String page, String id) {
        Observable observable = myApiService.GetComment(ukey, page, id);
        toSubscribe(observable, subscriber);
    }

    public void GetAddComment(MySubscriber<ArrayList<InformationDetailsCommentInfoEntity>> subscriber, String ukey, String id, String content) {
        Observable observable = myApiService.GetAddComment(ukey, id, content);
        toSubscribe(observable, subscriber);
    }

    public void GetNewsLikes(MySubscriber<EmptyEntity> subscriber, String ukey, String id, String status) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetNewsLikes(ukey, id, status);
        toSubscribe(observable, subscriber);
    }

    public void GetCategory(MySubscriber<EmptyEntity> subscriber, String ukey) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetCategory(ukey);
        toSubscribe(observable, subscriber);
    }

    public void GetShopList(MySubscriber<ArrayList<CategoryInfoEntity>> subscriber, String ukey, String cid) {
        Observable observable = myApiService.GetShopList(ukey, cid);
        toSubscribe(observable, subscriber);
    }

    public void GetShopDetails(MySubscriber<CategoryDetails> subscriber, String ukey, String id) {
        Observable<HttpResult<CategoryDetails>> observable = myApiService.GetShopDetails(ukey, id);
        toSubscribe(observable, subscriber);
    }

    public void GetShopCollection(MySubscriber<EmptyEntity> subscriber, String ukey, String id, String status) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetShopCollection(ukey, id, status);
        toSubscribe(observable, subscriber);
    }

    public void GetShopCollectionList(MySubscriber<ArrayList<CollectInfoEntity>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<CollectInfoEntity>>> observable = myApiService.GetShopCollectionList(ukey);
        toSubscribe(observable, subscriber);
    }

    public void GetShopAddAddress(MySubscriber<EmptyEntity> subscriber, String ukey, String address, String name, String phone, String province, String city, String district) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetShopAddAddress(ukey, address, name, phone, province, city, district);
        toSubscribe(observable, subscriber);
    }

    public void GetShopEditaddress(MySubscriber<EmptyEntity> subscriber, String ukey, String id, String address, String name, String phone, String province, String city, String district) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetShopEditaddress(ukey, id, address, name, phone, province, city, district);
        toSubscribe(observable, subscriber);
    }

    public void GetAddress(MySubscriber<ArrayList<AddressBean>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<AddressBean>>> observable = myApiService.GetAddress(ukey);
        toSubscribe(observable, subscriber);
    }

    public void GetDelAddress(MySubscriber<EmptyEntity> subscriber, String ukey, String id, String status) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetDelAddress(ukey, id, status);
        toSubscribe(observable, subscriber);
    }

    public void GetCheckAddress(MySubscriber<EmptyEntity> subscriber, String ukey, String id, String def) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.GetCheckAddress(ukey, id, def);
        toSubscribe(observable, subscriber);
    }

    public void GetShop(MySubscriber<ArrayList<ShopIndentEntity>> subscriber, String ukey, String status, String page) {
        Observable<HttpResult<ArrayList<ShopIndentEntity>>> observable = myApiService.GetShop(ukey, status, page);
        toSubscribe(observable, subscriber);
    }

    public void GetGuanggao(MySubscriber<ArrayList<HomeAdInfoEntity>> subscriber, String ukey, int cid) {
        Observable<HttpResult<ArrayList<HomeAdInfoEntity>>> observable = myApiService.GetGuanggao(ukey, cid);
        toSubscribe(observable, subscriber);
    }

    public void GetHomeList(MySubscriber<ArrayList<HomeNewInfoEntity>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<HomeNewInfoEntity>>> observable = myApiService.GetHomeList(ukey);
        toSubscribe(observable, subscriber);
    }

    public void GetShopHomeList(MySubscriber<ArrayList<ShopHomeInfoEntity>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<ShopHomeInfoEntity>>> observable = myApiService.GetShopHomeList(ukey);
        toSubscribe(observable, subscriber);
    }

    public void GetShopCart(MySubscriber<ArrayList<ShoppingCartBean>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<ShoppingCartBean>>> observable = myApiService.GetShopCart(ukey);
        toSubscribe(observable, subscriber);
    }



    public void EditBuy(MySubscriber<EmptyEntity> subscriber, String ukey, String shopid, String num, String pid) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.EditBuy(ukey, shopid, num, pid);
        toSubscribe(observable, subscriber);
    }
    public void GetBank(MySubscriber<ArrayList<BankInfo>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<BankInfo>>> observable = myApiService.GetBank(ukey);
        toSubscribe(observable, subscriber);
    }
    public void WithDrawals(MySubscriber<EmptyEntity> subscriber, String ukey, String amount, String name, String bankid,String banknumber) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.WithDrawals(ukey, amount, name, bankid,banknumber);
        toSubscribe(observable, subscriber);
    }
    public void AddNews(MySubscriber<EmptyEntity> subscriber, String ukey, String title, String content, String cover_list) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.AddNews(ukey, title, content, cover_list);
        toSubscribe(observable, subscriber);
    }
    public void DelNews(MySubscriber<EmptyEntity> subscriber, String ukey, String nid) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.DelNews(ukey, nid);
        toSubscribe(observable, subscriber);
    }
    public void MyListNews(MySubscriber<ArrayList<CenterPosterBean>> subscriber, String ukey,String page) {
        Observable<HttpResult<ArrayList<CenterPosterBean>>> observable = myApiService.MyListNews(ukey,page);
        toSubscribe(observable, subscriber);
    }
    public void Getulist(MySubscriber<ArrayList<CirclePostsBean>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<CirclePostsBean>>> observable = myApiService.Getulist(ukey);
        toSubscribe(observable, subscriber);
    }
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


}
