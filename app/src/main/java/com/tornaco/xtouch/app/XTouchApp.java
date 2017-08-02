package com.tornaco.xtouch.app;

import android.app.Application;

import com.tornaco.xtouch.provider.SettingsProvider;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class XTouchApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingsProvider.init(this);
    }
}
