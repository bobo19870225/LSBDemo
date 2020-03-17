package com.jinkan.www.lsbdemo.model.repository.http;


import androidx.lifecycle.LiveData;

import com.jinkan.www.lsbdemo.model.repository.http.bean.PetDetailBean;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetsBean;
import com.jinkan.www.lsbdemo.model.repository.http.live_data_call_adapter.Resource;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Sampson on 2018/4/17.
 * 网络请求接口
 */
public interface ApiService {

    @GET("pets")
    Call<List<PetsBean>> getPets();

    @GET("pet/{petId}")
    LiveData<Resource<PetDetailBean>> getPetDetail(@Path("petId") int petId);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}



