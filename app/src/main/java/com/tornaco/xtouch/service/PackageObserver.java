package com.tornaco.xtouch.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tornaco.xtouch.provider.ImePackageProvider;

import org.newstand.logger.Logger;

/**
 * Created by guohao4 on 2017/8/3.
 * Email: Tornaco@163.com
 */

public class PackageObserver extends BroadcastReceiver {

    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        context.registerReceiver(this, intentFilter);
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            // They send us bad action~
            return;
        }

        switch (action) {
            case Intent.ACTION_PACKAGE_ADDED:
            case Intent.ACTION_PACKAGE_REPLACED:
                String packageName = intent.getData().getSchemeSpecificPart();
                Logger.i("Received action %s for %s", action, packageName);
                if (packageName == null) return;

                ImePackageProvider.scanPackage(context, packageName);
                break;
        }

    }
}
