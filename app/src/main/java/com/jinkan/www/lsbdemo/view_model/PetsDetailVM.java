package com.jinkan.www.lsbdemo.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.jinkan.www.lsbdemo.model.SingleLiveEvent;
import com.jinkan.www.lsbdemo.model.repository.http.ApiService;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetDetailBean;
import com.jinkan.www.lsbdemo.model.repository.http.live_data_call_adapter.Resource;

/**
 * Created by Sampson on 2020-03-17.
 * LSBDemo
 * {@link com.jinkan.www.lsbdemo.view.PetsDetailActivity }
 */
public class PetsDetailVM extends BaseViewModel {
    private ApiService apiService;
    private String zipURL = null;
    public final SingleLiveEvent<String> action = new SingleLiveEvent<>();

    public PetsDetailVM(@NonNull Application application, ApiService apiService) {
        super(application);
        this.apiService = apiService;
    }

    @Override
    public void init(Object data) {

    }

    public void disPlay() {
        if (null != zipURL) {
            action.call();
        }
    }

    public LiveData<Resource<PetDetailBean>> getPetDetail(int transferData) {
        return apiService.getPetDetail(transferData);
    }

    public void setZipURL(String zip) {
        zipURL = zip;
    }
}
