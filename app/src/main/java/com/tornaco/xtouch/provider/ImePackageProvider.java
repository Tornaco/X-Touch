package com.tornaco.xtouch.provider;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import org.newstand.logger.Logger;

import java.util.HashSet;
import java.util.List;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class ImePackageProvider {

    static final HashSet<String> IMES = new HashSet<>();

    public static void initAsync(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                init(context);
            }
        }).start();
    }

    public static boolean isIME(String pkg) {
        return IMES.contains(pkg);
    }

    public static void init(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            packages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        } else {
            packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        for (PackageInfo packageInfo : packages) {
            String pkgName = packageInfo.packageName;
            if (!IMES.contains(pkgName)) {
                if (isInputMethodApp(context, pkgName)) {
                    IMES.add(pkgName);
                    Logger.i("Adding IME: %s", pkgName);
                }
            }
        }
    }

    public static boolean isInputMethodApp(Context context, String strPkgName) {

        PackageManager pkm = context.getPackageManager();
        boolean isIme = false;
        PackageInfo pkgInfo;
        try {
            pkgInfo = pkm.getPackageInfo(strPkgName, PackageManager.GET_SERVICES);
            ServiceInfo[] servicesInfos = pkgInfo.services;
            if (null != servicesInfos) {
                for (ServiceInfo sInfo : servicesInfos) {
                    if (null != sInfo.permission && sInfo.permission.equals("android.permission.BIND_INPUT_METHOD")) {
                        isIme = true;
                        break;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return isIme;
    }
}
