package com.jinkan.www.lsbdemo.model.repository.by_page.base;

import androidx.paging.PageKeyedDataSource;

import com.jinkan.www.lsbdemo.model.repository.Listing;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Sampson on 2019-04-30.
 * FastAndroid
 */
public interface InterfacePageRepository<Key, Value> {

    Call<List<Value>> setLoadInitialCall(PageKeyedDataSource.LoadInitialParams<Key> params);

    void setLoadInitialCallback(List<Value> body, PageKeyedDataSource.LoadInitialCallback<Key, Value> callback);


    Call<List<Value>> setLoadAfterCall(PageKeyedDataSource.LoadParams<Key> params);

    /**
     * @param body     ..
     * @param params   ..
     * @param callback ..
     * @return 是否成功获取数据
     */
    boolean setLoadCallback(List<Value> body,
                            PageKeyedDataSource.LoadParams<Key> params,
                            PageKeyedDataSource.LoadCallback<Key, Value> callback, Listing<Value> listing);

}
