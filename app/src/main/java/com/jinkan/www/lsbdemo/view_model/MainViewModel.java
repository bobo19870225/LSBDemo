package com.jinkan.www.lsbdemo.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.jinkan.www.lsbdemo.model.repository.Listing;
import com.jinkan.www.lsbdemo.model.repository.http.ApiService;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetsBean;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Sampson on 2020-03-17.
 * LSBDemo
 * {@link com.jinkan.www.lsbdemo.MainActivity}
 */
public class MainViewModel extends ListViewModel<Integer, PetsBean> {
    private ApiService apiService;

    public MainViewModel(@NonNull Application application, ApiService apiService) {
        super(application);
        this.apiService = apiService;
    }

    @NonNull
    @Override
    protected Integer setPageSize() {
        return 10;
    }


    @Override
    public Call<List<PetsBean>> setLoadInitialCall(PageKeyedDataSource.LoadInitialParams<Integer> params) {
        return apiService.getPets();
    }

    @Override
    public void setLoadInitialCallback(List<PetsBean> body, PageKeyedDataSource.LoadInitialCallback<Integer, PetsBean> callback) {
        callback.onResult(body, 1, 2);
    }

    @Override
    public Call<List<PetsBean>> setLoadAfterCall(PageKeyedDataSource.LoadParams<Integer> params) {
        return null;
    }


    @Override
    public boolean setLoadCallback(List<PetsBean> body, PageKeyedDataSource.LoadParams<Integer> params, PageKeyedDataSource.LoadCallback<Integer, PetsBean> callback, Listing<PetsBean> listing) {
        return false;
    }


    @Override
    public void init(Object data) {

    }
}
