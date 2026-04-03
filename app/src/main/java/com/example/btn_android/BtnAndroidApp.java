package com.example.btn_android;

import android.app.Application;

import com.example.btn_android.data.seed.FruitDataProvider;

public class BtnAndroidApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FruitDataProvider.init(this);
    }
}