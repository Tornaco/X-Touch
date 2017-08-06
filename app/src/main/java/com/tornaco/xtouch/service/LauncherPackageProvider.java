package com.tornaco.xtouch.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.tornaco.xtouch.common.SharedExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

public class LauncherPackageProvider {

    private static final Set<String> LAUNCHERS = new HashSet<>();

    public static void initAsync(final Context context) {
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LAUNCHERS.add(getLauncherPackageName(context));
            }
        });
    }

    public static boolean isLauncherApp(String pkg) {
        return pkg != null && LAUNCHERS.contains(pkg);
    }

    private static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        return res.activityInfo.packageName;
    }
}
