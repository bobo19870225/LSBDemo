package com.jinkan.www.lsbdemo.model.repository.by_page.base;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.jinkan.www.lsbdemo.model.repository.Listing;
import com.jinkan.www.lsbdemo.model.repository.NetWorkState;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Sampson on 2019/2/25.
 * FastAndroid
 */

public class BasePageKeyedDataSource<Key, Value> extends PageKeyedDataSource<Key, Value> {

    private InterfacePageRepository<Key, Value> interfacePageRepository;
    //    private ExecutorService NETWORK_IO = Executors.newFixedThreadPool(5);
    protected Listing<Value> listing;
    private Function0 function;


    public BasePageKeyedDataSource(InterfacePageRepository<Key, Value> interfacePageRepository, Listing<Value> listing) {
        this.interfacePageRepository = interfacePageRepository;
        this.listing = listing;
    }

    public void reTry() {
        ExecutorService NETWORK_IO = Executors.newFixedThreadPool(2);
        Function0 function0 = function;
        function = null;
        if (function0 != null) {
            NETWORK_IO.execute(function0::invoke);
        }
        NETWORK_IO.shutdown();
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Key, Value> callback) {
        listing.networkState.postValue(NetWorkState.loading());
        Call<List<Value>> call = interfacePageRepository.setLoadInitialCall(params);
        if (call != null) {
            try {
                Response<List<Value>> response = call.execute();
                List<Value> body = response.body();
                if (body != null) {
                    function = null;
//                    if (body.getHeader().getCode() == 0) {
//                        listing.networkState.postValue(NetWorkState.loaded());
//                        interfacePageRepository.setLoadInitialCallback(body, callback);
//                    } else if (body.getHeader().getCode() == 10000) {
//                        listing.networkState.postValue(NetWorkState.needLogin());
//                    } else {
//                        listing.networkState.postValue(NetWorkState.error(body.getHeader().getMsg()));
//                    }
                    listing.networkState.postValue(NetWorkState.loaded());
                    interfacePageRepository.setLoadInitialCallback(body, callback);

                }
            } catch (IOException e) {
                function = () -> {
                    loadInitial(params, callback);
                    return Unit.INSTANCE;
                };
                listing.networkState.postValue(NetWorkState.error(e.toString()));
            }
        }

    }


    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        Call<List<Value>> call = interfacePageRepository.setLoadAfterCall(params);
        if (call != null) {
            try {
                Response<List<Value>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Value> body = response.body();
                    if (body != null) {
                        function = null;
                        if (interfacePageRepository.setLoadCallback(body, params, callback, listing))
                            listing.networkState.postValue(NetWorkState.loaded());
                    }
                }

            } catch (IOException e) {
                function = () -> {
                    loadAfter(params, callback);
                    return Unit.INSTANCE;
                };
                listing.networkState.postValue(NetWorkState.error(e.toString()));
            }
        }
    }

}
