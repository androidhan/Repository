package com.hanshao.repository.api;

import android.content.Context;
import android.support.annotation.NonNull;


import com.hanshao.repository.components.HttpLoggingInterceptor;
import com.hanshao.repository.utils.NetWorkUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/8
 * ACTION:Base Api
 */

public  abstract class BaseApi <T>  implements Api<T> {


    public static final int NICE = 0;
    public static final int MID = 1;
    public static final int BAD =2;
    private static final long NICETIME = 10;
    private static final long MIDTIME = 15;
    private static final long BADTIME =20;


    protected OkHttpClient mOkHttpClient; //OkHttp3
    protected Context mContext;
    private Retrofit mNiceRetrofit;
    private Retrofit mMidRetrofit;
    private Retrofit mBadRetrofit;
    private  GsonConverterFactory mGsonConverterFactory;
    private  RxJavaCallAdapterFactory mRxJavaCallAdapterFactory;
    private Class<?> mServiceClazz;
    private String mBaseUrl;

    public BaseApi(Context context) {
        this.mOkHttpClient = buildOkHttpClient();
        this.mContext = context;
        this.mServiceClazz = providerServiceClazz();
        this.mBaseUrl = providerBaseUrl();

    }

    protected abstract Class<?> providerServiceClazz();

    protected abstract String providerBaseUrl();

    private void initConverterFactory() {
        if(mGsonConverterFactory == null){
            mGsonConverterFactory = GsonConverterFactory.create();
        }
        if(mRxJavaCallAdapterFactory == null){
            mRxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
        }
    }


    @Override
    public T getService(){
        int status = currentNetStatus();
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        Retrofit retrofit = null;
        if (status == NICE) {
                retrofit = getRetrofit(builder,NICETIME,mNiceRetrofit,mBaseUrl);
        } else if (status == MID) {
                retrofit = getRetrofit(builder,MIDTIME,mMidRetrofit,mBaseUrl);
        } else if (status == BAD) {
                retrofit = getRetrofit(builder,BADTIME,mBadRetrofit,mBaseUrl);
        }
        return (T) retrofit.create(mServiceClazz);
    }

    @Override
    public OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        return builder.build();
    }

    /**
     * 获取Retrofit
     * @param builder Ok3构建器
     * @param timeout 超时时间
     * @return
     */
    @NonNull
    private Retrofit getRetrofit(OkHttpClient.Builder builder,long timeout,Retrofit statusRetrofit,String baseUrl) {
        Retrofit retrofit;
        OkHttpClient okHttpClient = builder.connectTimeout(MIDTIME, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
        initConverterFactory();

        if(statusRetrofit == null){
            statusRetrofit = new Retrofit.Builder().addConverterFactory(mGsonConverterFactory)
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(mRxJavaCallAdapterFactory)
                    .build();
        }
        retrofit = statusRetrofit;
        return retrofit;
    }


    /**
     * 当前网络状态
     * @return
     */
    private int currentNetStatus() {
        int networkState = NetWorkUtil.getNetworkState(mContext);
        int state = NICE ;
        if(networkState ==NetWorkUtil.NETWORN_2G || networkState== NetWorkUtil.NETWORN_NONE ){
            state = BAD;
        }else if(networkState == NetWorkUtil.NETWORN_3G){
            state = MID;
        }else if(networkState == NetWorkUtil.NETWORN_4G || networkState ==NetWorkUtil.NETWORN_WIFI  || networkState ==NetWorkUtil.NETWORN_MOBILE){
            state = NICE;
        }
        return state;
    }


}
