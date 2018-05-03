package com.likeits.sanyou.network.entity;



import com.likeits.sanyou.network.ResponseConvertFactory;
import com.likeits.sanyou.network.api_service.MyApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public abstract class BaseHttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;
    protected Retrofit retrofit;
    protected MyApiService myApiService;

    //构造方法私有
    public BaseHttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(MyApiService.BASE_URL)
                .build();

        myApiService = retrofit.create(MyApiService.class);
    }


}
