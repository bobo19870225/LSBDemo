package com.jinkan.www.lsbdemo.model.repository;

import com.jinkan.www.lsbdemo.BuildConfig;
import com.jinkan.www.lsbdemo.SystemParameter;
import com.jinkan.www.lsbdemo.model.repository.http.ApiService;
import com.jinkan.www.lsbdemo.model.repository.http.live_data_call_adapter.LiveDataCallAdapterFactory;
import com.jinkan.www.lsbdemo.model.repository.http.live_data_call_adapter.LiveDataResponseBodyConverterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Sampson on 2019/2/25.
 * FastAndroid
 */
@Module
public class RepositoryModule {

    @Singleton
    @Provides
    static ApiService provideApiService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(SystemParameter.baseUrl)
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(LiveDataResponseBodyConverterFactory.create())//有顺序的
                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                .client(client)
                .build().create(ApiService.class);
    }


}
