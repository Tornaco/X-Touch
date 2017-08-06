package com.tornaco.xtouch.service;

import android.os.Build;

import com.tornaco.xtouch.provider.SettingsProvider;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

public class AppSwitcherCompat implements AppSwitcher {

    private AppSwitcher mImpl;

    public AppSwitcherCompat(final EventHandlerService service) {

        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                if (o == SettingsProvider.Key.SWITCH_APP_WITH_N_FEATURE) {
                    getDelegate(service);
                }
            }
        });

        getDelegate(service);
    }

    private void getDelegate(EventHandlerService service) {
        boolean useN = SettingsProvider.get().getBoolean(SettingsProvider.Key.SWITCH_APP_WITH_N_FEATURE);
        if (useN)
            this.mImpl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? new AppSwitcherImplV24(service)
                    : new AppSwitcherImpl(service);
        else this.mImpl = new AppSwitcherImpl(service);
    }

    @Override
    public boolean switchApp(EventHandlerService service) {
        return mImpl.switchApp(service);
    }

    @Override
    public void killCurrent() {
        mImpl.killCurrent();
    }
}
