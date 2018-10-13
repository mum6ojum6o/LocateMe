package com.mum6ojumbo.locateme.room;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutor implements Executor {
    private  static final Object LOCK = new Object();
    private static AppExecutor appExecutorInstance;
    Executor diskIO;
    Executor networkIO;
    public AppExecutor(Executor diskIO,Executor networkIO){
        this.diskIO=diskIO;
        this.networkIO = networkIO;
    }

    public static AppExecutor getInstance(){
        if(appExecutorInstance == null){
            synchronized (LOCK){ //review this.
                appExecutorInstance = new AppExecutor(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3));
            }
        }
        return appExecutorInstance;
    }

    public Executor getDiskIO(){return diskIO;}
    public Executor getNetworkIO(){return networkIO;}
    @Override
    public void execute(@NonNull Runnable runnable) {

    }
}
