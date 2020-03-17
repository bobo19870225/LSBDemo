package com.jinkan.www.lsbdemo;

import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jinkan.www.lsbdemo.databinding.ActivityMainBinding;
import com.jinkan.www.lsbdemo.view.MVVMListActivity;
import com.jinkan.www.lsbdemo.view.PetsAdapter;
import com.jinkan.www.lsbdemo.view.PetsDetailActivity;
import com.jinkan.www.lsbdemo.view_model.MainViewModel;
import com.jinkan.www.lsbdemo.view_model.ViewModelFactory;

import javax.inject.Inject;

import kotlin.jvm.functions.Function0;
import kr.co.namee.permissiongen.PermissionGen;

public class MainActivity extends MVVMListActivity<MainViewModel, ActivityMainBinding, PetsAdapter> {

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected int setToolBarMenu() {
        return 0;
    }

    @Override
    protected String setToolBarTitle() {
        return "宠物列表";
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    protected RecyclerView setRecyclerView() {
        return mViewDataBinding.list;
    }

    @NonNull
    @Override
    protected PetsAdapter setAdapter(Function0 reTry) {
        PermissionGen.needPermission(MainActivity.this,
                1001,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        PetsAdapter petsAdapter = new PetsAdapter(reTry);
        petsAdapter.setOnItemClick((view, itemObject, position) -> skipTo(PetsDetailActivity.class, itemObject.getId()));
        return petsAdapter;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults[0] == -1) {
                Toast.makeText(this, "拒绝开启权限，退出", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected SwipeRefreshLayout setSwipeRefreshLayout() {
        return mViewDataBinding.swipeRefresh;
    }

    @NonNull
    @Override
    protected MainViewModel createdViewModel() {
        return ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }
}
