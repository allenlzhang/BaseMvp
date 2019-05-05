package com.carlt.basemvp.network;

import com.blankj.utilcode.util.SPUtils;
import com.carlt.basemvp.constant.ConstantKey;
import com.carlt.basemvp.network.cookies.CookiesManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 11:49
 */
public class ApiRetrofit implements IApiService {
    private static final   String       TAG           = "ApiRetrofit===>";
    public static volatile ApiRetrofit  sApiRetrofit;
    public static final    String       BASE_URL      = "http://test.linewin.cc:8888/app/";
    //        public static final    String       BASE_URL      = "http://www.wanandroid.com/";
    public static final    String       TEST_ACCESSID = "18644515396614518644";   //autoGo 测试
    private                Retrofit     mRetrofit;
    private                OkHttpClient mHttpClient;
    private                ApiService   mApiService;
    private static         Interceptor  mInterceptor  = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

//            HttpUrl oldUrl = request.url();
//            HttpUrl baseUrl = HttpUrl.parse(URLConfig.getAutoGoUrl());
//            HttpUrl newBuilder = oldUrl.newBuilder()
//                    .scheme(baseUrl.scheme())
//                    .host(baseUrl.host())
//                    .port(baseUrl.port())
//                    .build();

            Request.Builder newRequest = request.newBuilder()
//                    .url(newBuilder)
                    .header("Carlt-Access-Id", TEST_ACCESSID)
                    .header("Content-Type", "application/json")
                    .header("Carlt-Token", SPUtils.getInstance().getString(ConstantKey.TOKEN))
                    .method(request.method(), request.body());
//            String token = SPUtils.getInstance().getString(ConstantKey.TOKEN);
//            LogUtils.e(token);

            //            long startTime = System.currentTimeMillis();
            //
            //            Response response = chain.proceed(newRequest.build());
            //            long endTime = System.currentTimeMillis();
            //            long duration = endTime - startTime;
            //            String content = response.body().string();
            //            Log.e(TAG, "----------Request Start----------------");
            //            Log.e(TAG, "| " + request.toString() + request.headers());
            //            Log.e(TAG, "| Response:" + content);
            //            Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
            return chain.proceed(newRequest.build());
        }
    };

    public static ApiRetrofit getInstance() {
        if (sApiRetrofit == null) {
            synchronized (Object.class) {
                if (sApiRetrofit == null) {
                    sApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return sApiRetrofit;
    }

    private static HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (!BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }

    private ApiRetrofit() {


        mHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .addInterceptor(getLogInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mHttpClient)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }

    @Override
    public <T> T getService(Class<T> service) {
        return mRetrofit.create(service);
    }
}
