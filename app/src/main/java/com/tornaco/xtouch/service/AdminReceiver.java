package com.tornaco.xtouch.service;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.Keep;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */
@Keep
public class AdminReceiver extends DeviceAdminReceiver {

    public static ComponentName name(Context context) {
        return new ComponentName(context, AdminReceiver.class);
    }

    public static boolean isActivated(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = name(context);
        return devicePolicyManager.isAdminActive(componentName);
    }
}
