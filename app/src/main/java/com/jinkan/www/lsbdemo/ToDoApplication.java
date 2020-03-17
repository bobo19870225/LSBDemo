package com.jinkan.www.lsbdemo;

import android.app.Application;
import android.view.View;
import android.view.WindowManager;

import com.jinkan.www.lsbdemo.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class ToDoApplication extends DaggerApplication {
    private WindowManager manager;
    private View view;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    public WindowManager getManager() {
        return manager;
    }

    public void setManager(WindowManager manager) {
        this.manager = manager;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
//    /**
//     * Our Espresso tests need to be able to get an instance of the {@link TasksRepository}
//     * so that we can delete all tasks before running each test
//     */
//    @VisibleForTesting
//    public TasksRepository getTasksRepository() {
//        return tasksRepository;
//    }
}
