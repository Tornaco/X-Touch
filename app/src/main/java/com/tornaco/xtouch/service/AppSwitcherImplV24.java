package com.tornaco.xtouch.service;

import android.accessibilityservice.AccessibilityService;

import com.chrisplus.rootmanager.RootManager;
import com.tornaco.xtouch.common.SharedExecutor;

import org.newstand.logger.Logger;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

class AppSwitcherImplV24 extends AppSwitcherImpl {


    AppSwitcherImplV24(EventHandlerService service) {
        super(service);
    }

    @Override
    public boolean switchApp(EventHandlerService service) {
        Logger.i("switchApp-V24");
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
                && service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    @Override
    public void killCurrent() {
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RootManager.getInstance().killProcessByName(mCurrentPkg);
            }
        });
    }
}
