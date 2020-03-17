package com.jinkan.www.lsbdemo.view_model;

import android.app.Application;

import androidx.annotation.NonNull;

import com.jinkan.www.lsbdemo.model.repository.Listing;
import com.jinkan.www.lsbdemo.model.repository.by_page.base.BasePageKeyRepository;
import com.jinkan.www.lsbdemo.model.repository.by_page.base.InterfacePageRepository;
import com.jinkan.www.lsbdemo.view.MVVMListActivity;

/**
 * Created by Sampson on 2019/3/14.
 * FastAndroid
 * {@link MVVMListActivity}
 */

public abstract class ListViewModel<Key, Value> extends BaseViewModel implements InterfacePageRepository<Key, Value> {

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public Object listRequest;

    public Listing<Value> getListing(Object listRequest) {
        this.listRequest = listRequest;
        return new BasePageKeyRepository<>(this).post(setPageSize());
    }

    @NonNull
    protected abstract Integer setPageSize();
}
