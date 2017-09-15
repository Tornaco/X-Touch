package com.tornaco.xtouch.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.xiaomi.ad.SplashAdListener;
import com.xiaomi.ad.adView.SplashAd;

import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

@RuntimePermissions
public class VerticalSplashAdActivity extends Activity {
    private static final String TAG = "VerticalSplash";
    private static final String POSITION_ID = XTouchApp.AD_SPLASH_ID;
    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashad);
        mContainer = findViewById(R.id.splash_ad_container);
        VerticalSplashAdActivityPermissionRequester.showAdChecked(this);
    }

    @RequiresPermission({"android.permission.ACCESS_WIFI_STATE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.READ_PHONE_STATE",
            "android.permission.GET_TASKS"})
    @RequiresPermission.OnDenied("onPermissionNotGrant")
    void showAd() {
        SplashAd splashAd = new SplashAd(this, mContainer, R.drawable.default_splash,
                new SplashAdListener() {
                    @Override
                    public void onAdPresent() {
                        Log.i(TAG, "onAdPresent");
                    }

                    @Override
                    public void onAdClick() {
                        Log.i(TAG, "onAdClick");
                    }

                    @Override
                    public void onAdDismissed() {
                        Log.i(TAG, "onAdDismissed");
                        goToMain();
                    }

                    @Override
                    public void onAdFailed(String s) {
                        Log.i(TAG, "onAdFailed, message: " + s);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goToMain();
                            }
                        }, 1500);
                    }
                });
        splashAd.requestAd(POSITION_ID);
    }

    void goToMain() {
        boolean noRecent = SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS);
        if (!noRecent)
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, MainActivityNoRecents.class));
        finish();
    }

    void onPermissionNotGrant() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        VerticalSplashAdActivityPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mContainer.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}