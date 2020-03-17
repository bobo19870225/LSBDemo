package com.jinkan.www.lsbdemo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.jinkan.www.lsbdemo.R;
import com.jinkan.www.lsbdemo.SystemParameter;
import com.jinkan.www.lsbdemo.ToDoApplication;
import com.jinkan.www.lsbdemo.databinding.ActivityPetDetailBinding;
import com.jinkan.www.lsbdemo.model.repository.http.ApiService;
import com.jinkan.www.lsbdemo.model.repository.http.bean.PetDetailBean;
import com.jinkan.www.lsbdemo.model.repository.http.library.DownloadListener;
import com.jinkan.www.lsbdemo.model.repository.http.library.DownloadUtil;
import com.jinkan.www.lsbdemo.model.repository.http.library.InputParameter;
import com.jinkan.www.lsbdemo.view_model.PetsDetailVM;
import com.jinkan.www.lsbdemo.view_model.ViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;

import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Sampson on 2020-03-17.
 * LSBDemo
 */
public class PetsDetailActivity extends MVVMActivity<PetsDetailVM, ActivityPetDetailBinding> {
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ApiService apiService;
    private String zipURL;
    private String desFilePath;
    private List<BitmapDrawable> list = new ArrayList<>();
    private AnimationDrawable frameAnim = new AnimationDrawable();

    @NonNull
    @Override
    protected PetsDetailVM createdViewModel() {
        return ViewModelProviders.of(this, viewModelFactory).get(PetsDetailVM.class);
    }

    @Override
    protected void setView() {
        mViewModel.getPetDetail((int) transferData).observe(this, petDetailBeanResource -> {
            if (petDetailBeanResource.isSuccess()) {
                PetDetailBean resource = petDetailBeanResource.getResource();
                if (null != resource) {
                    String image = resource.getImage();
                    Glide.with(mViewDataBinding.image).load(image).into(mViewDataBinding.image);
                    mViewDataBinding.name.setText(resource.getName());
                    mViewDataBinding.like.setText(String.valueOf(resource.getLikeCount()));
                    mViewDataBinding.use.setText(String.valueOf(resource.getUseCount()));
                    zipURL = resource.getZip();
                    desFilePath = Environment.getExternalStorageDirectory() + File.separator + resource.getId() + ".zip";
                    mViewModel.setZipURL(resource.getZip());
                }
            }
        });

        mViewModel.action.observe(this, s -> {
            mViewDataBinding.set.setEnabled(false);
            getPetDetail();
        });
    }

    private void showDialogAndIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

    private void showDialog() {
        for (int i = 0; i < list.size(); i++) {
            frameAnim.addFrame(list.get(i), 100);
        }
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        ToDoApplication application = (ToDoApplication) getApplication();
        WindowManager manager = application.getManager();
        View view = application.getView();
        ImageView imageView;
        if (null == view) {
            imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_dialog, null);
            application.setView(imageView);
        } else {
            imageView = (ImageView) application.getView();
        }
        imageView.setImageDrawable(frameAnim);
        WindowManager mWm;
        if (null == manager) {
            mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            application.setManager(mWm);
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            mWm.getDefaultDisplay().getMetrics(localDisplayMetrics);
            localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            if (Build.VERSION.SDK_INT > 24) {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            if (Build.VERSION.SDK_INT > 26) {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
            localLayoutParams.format = 1;
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mWm.addView(imageView, localLayoutParams);
        }
        frameAnim.stop();
        frameAnim.start();
        mViewDataBinding.set.setEnabled(true);
    }


    @PermissionSuccess(requestCode = 1001)
    private void getPetDetail() {
        showDialogAndIntent();
        if (list.size() > 0) {
            showDialog();
        } else {
            File file = new File(desFilePath);
            if (file.exists()) {
                upZip(file);
            } else {
                DownloadUtil.getInstance()
                        .downloadFile(new InputParameter.Builder(SystemParameter.baseUrl, zipURL, desFilePath)
                                .setCallbackOnUiThread(true)
                                .build(), new DownloadListener() {
                            @Override
                            public void onFinish(final File file) {
                                mViewDataBinding.set.setEnabled(true);
                                upZip(file);

                            }

                            @Override
                            public void onProgress(int progress, long downloadedLengthKb, long totalLengthKb) {
                                mViewDataBinding.progressBar.setText(String.format("文件文件下载进度：%d%s \n\n已下载:%sKB | 总长:%sKB", progress, "%", downloadedLengthKb + "", totalLengthKb + ""));

                            }

                            @Override
                            public void onFailed(String errMsg) {
                                mViewDataBinding.set.setEnabled(true);
                            }
                        });
            }
        }

    }

    private void upZip(File file) {

        try {
            ZipFile zfile = new ZipFile(file);
            Enumeration zList = zfile.entries();
            ZipEntry ze;

            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();

                if (ze.isDirectory()) {
                    String dirstr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ze.getName();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    File f = new File(dirstr);
                    continue;
                }
                InputStream is = zfile.getInputStream(ze);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                list.add(bd);
                is.close();
            }
            showDialog();
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int setToolBarMenu() {
        return 0;
    }

    @Override
    protected String setToolBarTitle() {
        return "宠物详情";
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_pet_detail;
    }
}
