package com.tornaco.xtouch.service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import com.chrisplus.rootmanager.RootManager;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.provider.ImePackageProvider;

import org.newstand.logger.Logger;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

class AppSwitcherImpl implements AppSwitcher {

    private PackageManager packageManager;

    protected String mCurrentPkg;
    private String mPreviousPkg;

    AppSwitcherImpl(EventHandlerService service) {
        Logger.i("AppSwitcherImpl init");
        this.packageManager = service.getPackageManager();
        service.setOnAccessibilityEventListener(new EventHandlerService.AccessibilityEventListener() {
            @Override
            public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
                    String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
                    Logger.i("AppSwitcherImpl: onAccessibilityEvent: %s, %s", pkgName, clz);
                    if (ignore(pkgName, clz)) return;
                    mPreviousPkg = mCurrentPkg;
                    mCurrentPkg = pkgName;
                }
            }
        });
    }

    private boolean ignore(String pkg, String clz) {
        return
                TextUtils.isEmpty(pkg)
                        || pkg.equals(mCurrentPkg)
                        || ImePackageProvider.isIME(pkg)
                        || LauncherPackageProvider.isLauncherApp(pkg)
                        || pkg.equals("android")
                        || pkg.contains("incall")
                        || pkg.contains("andorid.systemui");
    }

    @Override
    public boolean switchApp(EventHandlerService service) {
        if (TextUtils.isEmpty(mPreviousPkg)) return false;

        Intent pkgIntent = packageManager.getLaunchIntentForPackage(mPreviousPkg);
        if (pkgIntent != null) {
            pkgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(pkgIntent);
            return true;
        }

        return false;
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
