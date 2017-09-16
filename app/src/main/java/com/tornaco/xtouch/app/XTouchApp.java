package com.tornaco.xtouch.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.tornaco.xtouch.BuildConfig;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.xiaomi.ad.AdSdk;

import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class XTouchApp extends Application {

    public static final String AD_APP_ID = "2882303761517616845";
    public static final String AD_SPLASH_ID = "6f9bbf504639eaca720b697bdb685e7b";

    @Override
    public void onCreate() {
        super.onCreate();

        // For AD start.
        AdSdk.setDebugOn();
        AdSdk.initialize(this, AD_APP_ID);
        // For AD end.

        Logger.config(Settings.builder().tag("XTouchApp").logLevel(
                BuildConfig.DEBUG ? Logger.LogLevel.ALL : Logger.LogLevel.WARN
        ).build());

        SettingsProvider.init(this);

        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                if (o == SettingsProvider.Key.NO_RECENTS) {
                    boolean noRecent = SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS);

                    String[] clzToDisable, clzToEnable;

                    if (!noRecent) {
                        clzToDisable = new String[]{
                                MainActivityNoRecents.class.getName(),
                                ContainerHostActivityNoRecents.class.getName(),

                        };
                        clzToEnable = new String[]{
                                MainActivity.class.getName(),
                                ContainerHostActivity.class.getName(),
                        };
                    } else {
                        clzToDisable = new String[]{
                                MainActivity.class.getName(),
                                ContainerHostActivity.class.getName(),
                        };
                        clzToEnable = new String[]{
                                MainActivityNoRecents.class.getName(),
                                ContainerHostActivityNoRecents.class.getName(),

                        };
                    }

                    PackageManager pm = getPackageManager();

                    for (String clz : clzToEnable) {
                        Logger.i("Enabling: %s", clz);
                        ComponentName componentName = new ComponentName(getPackageName(), clz);
                        pm.setComponentEnabledSetting(componentName,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);
                    }

                    for (String clz : clzToDisable) {
                        Logger.i("Disabling: %s", clz);
                        ComponentName componentName = new ComponentName(getPackageName(), clz);
                        pm.setComponentEnabledSetting(componentName,
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                    }
                }
            }
        });
    }
}
