package com.tornaco.xtouch.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.ImePackageProvider;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.widget.FloatView;

import org.newstand.logger.Logger;

import java.util.Observable;
import java.util.Observer;

import ezy.assist.compat.SettingsCompat;

public class EventHandlerService extends AccessibilityService implements FloatView.Callback, GlobalActionExt {

    private static final float IME_WINDOW_SIZE_SC = 2.7785f;

    private DevicePolicyManager mDevicePolicyManager;
    private Vibrator mVibrator;

    private FloatView mFloatView;

    private int actionSingleTap, actionDoubleTap, actionSwipeUp, actionSwipeDown, actionSwipeLeft, actionSwipeRight;
    private boolean vibrate, sound, mImeReposition, mRestoreIMEHidden;

    private SoundPool mSoundPool;
    private int mStartSound;

    private int mOrientation;

    private int mIMEHeight, mScreenHeight;

    private Handler h = new Handler();
    private PackageObserver packageObserver;

    private void readSettings() {
        actionSingleTap = SettingsProvider.get().getInt(SettingsProvider.Key.SINGLE_TAP_ACTION);
        actionDoubleTap = SettingsProvider.get().getInt(SettingsProvider.Key.DOUBLE_TAP_ACTION);
        actionSwipeUp = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_UP_ACTION);
        actionSwipeDown = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_DOWN_ACTION);
        actionSwipeLeft = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_LEFT_ACTION);
        actionSwipeRight = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_RIGHT_ACTION);

        sound = SettingsProvider.get().getBoolean(SettingsProvider.Key.SOUND);
        vibrate = SettingsProvider.get().getBoolean(SettingsProvider.Key.VIRBATE);
        mImeReposition = SettingsProvider.get().getBoolean(SettingsProvider.Key.IME_REPOSITION);
        mRestoreIMEHidden = SettingsProvider.get().getBoolean(SettingsProvider.Key.RESTORE_IME_HIDDEN);

        dump();
    }

    private void dump() {
        Logger.i(toString());
    }

    @Override
    public String toString() {
        return "EventHandlerService{" +
                "actionSingleTap=" + actionSingleTap +
                ", actionDoubleTap=" + actionDoubleTap +
                ", actionSwipeUp=" + actionSwipeUp +
                ", actionSwipeDown=" + actionSwipeDown +
                ", actionSwipeLeft=" + actionSwipeLeft +
                ", actionSwipeRight=" + actionSwipeRight +
                '}';
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        int density = (int) getResources().getDisplayMetrics().density;
        mIMEHeight = (int) ((float) mScreenHeight / IME_WINDOW_SIZE_SC) + 48 * density; //Offset;
        Logger.i("Screen height:%s, imr height:%s", mScreenHeight, mIMEHeight);

        ImePackageProvider.initAsync(this);

        SettingsProvider.init(this);
        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        readSettings();
                    }
                });
            }
        });

        readSettings();

        mOrientation = getResources().getConfiguration().orientation;
        mFloatView = new FloatView(this);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .build())
                .build();

        mStartSound = mSoundPool.load(this, R.raw.effect, 1);

        if (SettingsCompat.canDrawOverlays(getApplicationContext())) {
            mFloatView.attach();
        } else {
            Logger.e("Missing draw overlay permission, do not add view.");
        }

        SettingsProvider.get().putBoolean(SettingsProvider.Key.ENABLED, true);

        packageObserver = new PackageObserver();
        packageObserver.register(this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (mImeReposition && accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
            String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
            Logger.i("TYPE_WINDOW_STATE_CHANGED: %s, %s", pkgName, clz);
            if (ImePackageProvider.isIME(pkgName)) {
                Logger.i("We are in IME");
                mFloatView.repositionInIme(mScreenHeight, mIMEHeight);
            } else if (mRestoreIMEHidden) {
                mFloatView.restoreXYOnImeHiddenIfNeed();
            }
            Logger.i("attached: %s", mFloatView.isAttachedToWindow());
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Logger.i("onServiceConnected");
    }

    @Override
    public void onInterrupt() {
        Logger.i("onInterrupt");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.i("onUnbind");
        mFloatView.detach();
        SettingsProvider.get().putBoolean(SettingsProvider.Key.ENABLED, false);
        return super.onUnbind(intent);
    }

    @Override
    public void onSingleTap() {
        Logger.d("onSingleTap");
        perform(actionSingleTap);
    }

    @Override
    public void onDoubleTap() {
        Logger.d("onDoubleTap");
        perform(actionDoubleTap);
    }


    @Override
    public void onLongPress() {
        if (vibrate) mVibrator.vibrate(30);
    }

    @Override
    public void onSwipeDirection(@NonNull FloatView.SwipeDirection direction) {
        Logger.i("onSwipeDirection: %s", direction);
        switch (direction) {
            case L:
                perform(actionSwipeLeft);
                break;
            case R:
                perform(actionSwipeRight);
                break;
            case U:
                perform(actionSwipeUp);
                break;
            case D:
                perform(actionSwipeDown);
                break;
        }
    }

    @Override
    public void perform(int action) {
        Logger.i("perform: %s", action);
        switch (action) {
            case GlobalActionExt.GLOBAL_ACTION_LOCK_SCREEN:
                mDevicePolicyManager.lockNow();
                break;
            case BYPASS:
                break;
            default:
                performGlobalAction(action);
                break;
        }
        if (action != BYPASS) {
            if (sound) {
                mSoundPool.play(mStartSound, 1.0f, 1.0f, 0, 0, 1.0f);
            }
            if (vibrate) {
                mVibrator.vibrate(30);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mOrientation != newConfig.orientation) {
            mFloatView.refreshRect();
            mFloatView.reposition();
            mOrientation = newConfig.orientation;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        packageObserver.unRegister(this);
    }

    public static boolean activated(Context context) {
        for (AccessibilityServiceInfo id : ((AccessibilityManager)
                context.getSystemService(ACCESSIBILITY_SERVICE))
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)) {
            if (id.getId().contains(EventHandlerService.class.getSimpleName())) {
                return true;
            }
        }
        return false;

    }
}
