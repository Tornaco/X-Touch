package com.tornaco.xtouch.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.chrisplus.rootmanager.RootManager;
import com.tornaco.xtouch.R;
import com.tornaco.xtouch.common.ReflectionUtils;
import com.tornaco.xtouch.provider.ImePackageProvider;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.widget.FloatView;

import org.newstand.logger.Logger;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

import ezy.assist.compat.SettingsCompat;

public class EventHandlerService extends AccessibilityService implements FloatView.Callback, GlobalActionExt {

    private static final float IME_WINDOW_SIZE_SC = 2.7785f;
    public static final String ACTION_RESTORE = "action.restore";
    private static final int NOTIFICATION_RESTORE_ID = 0x1;

    private DevicePolicyManager mDevicePolicyManager;
    private Vibrator mVibrator;

    private FloatView mFloatView;

    private int actionSingleTap, actionDoubleTap, actionSwipeUp, actionSwipeDown, actionSwipeLeft, actionSwipeRight,
            actionSwipeUpLarge, actionSwipeDownLarge, actionSwipeLeftLarge, actionSwipeRightLarge, screenToL, screenToP;
    private boolean vibrate, sound, mImeReposition, mRestoreIMEHidden, mRootEnabled, mPanicDetectionEnabled;

    private SoundPool mSoundPool;
    private int mStartSound;

    private int mOrientation;

    private int mIMEHeight, mScreenHeight;

    private Handler H = new Handler();

    private PackageObserver mPackageObserver;
    private BroadcastReceiver mActionReceiver;

    private AppSwitcher mAppSwitcher;

    private AccessibilityEventListener mOnAccessibilityEventListener;

    public void setOnAccessibilityEventListener(AccessibilityEventListener listener) {
        this.mOnAccessibilityEventListener = listener;
    }

    private void readSettings() {
        actionSingleTap = SettingsProvider.get().getInt(SettingsProvider.Key.SINGLE_TAP_ACTION);
        actionDoubleTap = SettingsProvider.get().getInt(SettingsProvider.Key.DOUBLE_TAP_ACTION);

        actionSwipeUp = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_UP_ACTION);
        actionSwipeDown = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_DOWN_ACTION);
        actionSwipeLeft = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_LEFT_ACTION);
        actionSwipeRight = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_RIGHT_ACTION);

        actionSwipeUpLarge = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_UP_LARGE_ACTION);
        actionSwipeDownLarge = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_DOWN_LARGE_ACTION);
        actionSwipeLeftLarge = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_LEFT_LARGE_ACTION);
        actionSwipeRightLarge = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_RIGHT_LARGE_ACTION);

        screenToL = SettingsProvider.get().getInt(SettingsProvider.Key.SCREEN_TO_L_ACTION);
        screenToP = SettingsProvider.get().getInt(SettingsProvider.Key.SCREEN_TO_P_ACTION);

        mPanicDetectionEnabled = SettingsProvider.get().getBoolean(SettingsProvider.Key.PANIC_DETECTION);

        sound = SettingsProvider.get().getBoolean(SettingsProvider.Key.SOUND);
        vibrate = SettingsProvider.get().getBoolean(SettingsProvider.Key.VIRBATE);
        mImeReposition = SettingsProvider.get().getBoolean(SettingsProvider.Key.IME_REPOSITION);
        mRestoreIMEHidden = SettingsProvider.get().getBoolean(SettingsProvider.Key.RESTORE_IME_HIDDEN);
        mRootEnabled = SettingsProvider.get().getBoolean(SettingsProvider.Key.ROOT);

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
        LauncherPackageProvider.initAsync(this);

        mAppSwitcher = new AppSwitcherCompat(this);

        SettingsProvider.init(this);
        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                H.post(new Runnable() {
                    @Override
                    public void run() {
                        readSettings();

                        // Now we should update FlowView policy.
                        if (mFloatView != null) {
                            mFloatView.setDoubleTapEnabled(actionDoubleTap != BYPASS);
                        }
                    }
                });
            }
        });

        // This happen before FloatView initAsync.
        readSettings();

        mOrientation = getResources().getConfiguration().orientation;

        mFloatView = new FloatView(this);
        mFloatView.setDoubleTapEnabled(actionDoubleTap != BYPASS);

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

        mPackageObserver = new PackageObserver();
        mPackageObserver.register(this);

        mActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_RESTORE.equals(intent.getAction())) {
                    Logger.i("Restore action received.");
                    mFloatView.show();
                    NotificationManagerCompat.from(getApplicationContext()).cancel(NOTIFICATION_RESTORE_ID);
                }
            }
        };

        IntentFilter filter = new IntentFilter(ACTION_RESTORE);
        registerReceiver(mActionReceiver, filter);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    interface AccessibilityEventListener {
        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Logger.d("onAccessibilityEvent: %s", accessibilityEvent.getEventType());
        if (mOnAccessibilityEventListener != null)
            mOnAccessibilityEventListener.onAccessibilityEvent(accessibilityEvent);
        if (mImeReposition && (accessibilityEvent.getEventType()
                == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)) {
            String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
            String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
            Logger.i("ES: onAccessibilityEvent: %s, %s", pkgName, clz);
            if (ImePackageProvider.isIME(pkgName)) {
                Logger.i("We are in IME: %s", pkgName);
                mFloatView.repositionInIme(mScreenHeight, mIMEHeight);
            } else if (mRestoreIMEHidden) {
                Logger.i("We are out of IME");
                mFloatView.restoreXYOnImeHiddenIfNeed();
            }
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

    private int clickTimes = 0;
    private static final int PANIC_DETECTION_TIMES = 3;
    private static final int PANIC_DETECTION_TIME_MILLS = 3 * 100;

    private Handler h = new Handler(Looper.getMainLooper());

    @Override
    public void onSingleTap() {
        Logger.d("onSingleTap");

        // Check if panic.
        if (mPanicDetectionEnabled && clickTimes >= PANIC_DETECTION_TIMES) {
            Logger.i("Panic!!! Let's go home! ---@%s", clickTimes);
            clickTimes = 0;
            perform(AccessibilityService.GLOBAL_ACTION_HOME);
            return;
        }

        perform(actionSingleTap);

        // Reset.
        if (mPanicDetectionEnabled) {
            clickTimes++;
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickTimes = 0;
                }
            }, PANIC_DETECTION_TIME_MILLS);
        }
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
    public void onSwipeDirectionLargeDistance(@NonNull FloatView.SwipeDirection direction) {
        Logger.i("onSwipeDirectionLargeDistance: %s", direction);
        switch (direction) {
            case L:
                perform(actionSwipeLeftLarge);
                break;
            case R:
                perform(actionSwipeRightLarge);
                break;
            case U:
                perform(actionSwipeUpLarge);
                break;
            case D:
                perform(actionSwipeDownLarge);
                break;
        }
    }

    @Override
    public void perform(int action) {
        perform(action, true);
    }

    public void perform(int action, boolean feedback) {
        Logger.i("perform: %s", action);
        switch (action) {
            case GlobalActionExt.GLOBAL_ACTION_LOCK_SCREEN:
                if (mRootEnabled) {
                    ScreenLockerRoot.lock();
                } else if (AdminReceiver.isActivated(getApplicationContext())) {
                    mDevicePolicyManager.lockNow();
                }
                break;
            case GLOBAL_ACTION_SWITCH_APP:
                mAppSwitcher.switchApp(this);
                break;
            case GLOBAL_ACTION_KILL_CURRENT_APP:
                mAppSwitcher.killCurrent();
                break;
            case GLOBAL_ACTION_HIDE:
                buildNotification();
                mFloatView.hide();
                break;
            case GLOBAL_ACTION_RESTORE:
                NotificationManagerCompat.from(getApplicationContext()).cancel(NOTIFICATION_RESTORE_ID);
                mFloatView.show();
                break;
            case BYPASS:
                break;
            default:
                performGlobalAction(action);
                break;
        }
        if (feedback && action != BYPASS) {
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

            // Get L or P.
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // L To L
                perform(screenToL, false);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                perform(screenToP, false);
            }

            mFloatView.refreshRect();
            mFloatView.exchangeXY();
            mOrientation = newConfig.orientation;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ACTION_RESTORE.equals(intent.getAction())) {
            mFloatView.show();
            NotificationManagerCompat.from(getApplicationContext()).cancel(NOTIFICATION_RESTORE_ID);
        }

        return START_STICKY;
    }

    private void buildNotification() {
        NotificationManagerCompat.from(getApplicationContext()).cancel(NOTIFICATION_RESTORE_ID);
        NotificationManagerCompat.from(getApplicationContext())
                .notify(NOTIFICATION_RESTORE_ID,
                        createNotificationBuilder().build());
    }


    private Notification.Builder createNotificationBuilder() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_app)
                .setContentTitle(getString(R.string.title_hidden));
        Intent restoreIntent = new Intent(ACTION_RESTORE);
        builder.setOngoing(true);
        builder.setContentIntent(PendingIntent.getBroadcast(this, 0, restoreIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        return builder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPackageObserver.unRegister(this);
        unregisterReceiver(mActionReceiver);
    }

    // FIXME Need Xposed.
    private static class InputManagerCompat {
        static void lock() {
            InputManager manager;
            Method mGet = ReflectionUtils.findMethod(InputManager.class, "getInstance");
            mGet.setAccessible(true);
            manager = (InputManager) ReflectionUtils.invokeMethod(mGet, null);
            Logger.i("InputManager instance:%s", manager);
            Method mInject = ReflectionUtils.findMethod(InputManager.class, "injectInputEvent", InputEvent.class, int.class);
            mInject.setAccessible(true);
            final long eventTime = SystemClock.uptimeMillis();
            ReflectionUtils.invokeMethod(mInject, manager, new KeyEvent(eventTime - 50, eventTime - 50, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER, 0), 0);
            ReflectionUtils.invokeMethod(mInject, manager, new KeyEvent(eventTime - 50, eventTime - 50, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_POWER, 0), 0);
        }
    }

    public static class ScreenLockerRoot {
        static void lock() {
            RootManager.getInstance().runCommand("input keyevent 26");
        }
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
