package com.mum6ojumbo.locateme.room;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutor implements Executor {
    private  static final Object LOCK = new Object();
    private static AppExecutor appExecutorInstance;
    Executor diskIO;
    public AppExecutor(Executor diskIO){
        this.diskIO=diskIO;
    }

    public static AppExecutor getInstance(){
        if(appExecutorInstance == null){
            synchronized (LOCK){ //review this.
                appExecutorInstance = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return appExecutorInstance;
    }

    public Executor getDiskIO(){return diskIO;}
    @Override
    public void execute(@NonNull Runnable runnable) {

    }
}
