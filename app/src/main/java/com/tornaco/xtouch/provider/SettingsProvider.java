package com.tornaco.xtouch.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewConfiguration;

import com.tornaco.xtouch.BuildConfig;
import com.tornaco.xtouch.service.GlobalActionExt;

import org.newstand.logger.Logger;

import java.util.Observable;

/**
 * Created by Tornaco on 2017/7/25.
 * Licensed with Apache.
 */
public class SettingsProvider extends Observable {

    private static SettingsProvider sMe;

    private static final String PREF_NAME = "x-touch_app_settings";

    public static final String OPEN_SOURCE_GIT_URL = "https://github.com/Tornaco/X-Touch";


    public enum Key {
        PAID(false),
        AD_CLICKED_TIME_MILLS(0L),
        FORCE_SHOW_AD(false),
        ROOT(false),
        NO_RECENTS(false),
        SWITCH_APP_WITH_N_FEATURE(true),
        ALPHA(100),
        SIZE(48),
        EDGE(false),
        SOUND(true),
        VIRBATE(true),
        HEART_BEAT(false),
        ROTATE(false),
        FEEDBACK_ANIM(true),
        RESTORE_IME_HIDDEN(true),
        CUSTOM_IMAGE(null),
        CROP_TO_CIRCLE(true),
        BLUR(true),
        LOCKED(false),
        SWIPE_SLOT(50),
        TAP_DELAY(200),
        LARGE_SLOP(50),
        LONG_PRESS_TIMEOUT(ViewConfiguration.getLongPressTimeout()),

        SINGLE_TAP_ACTION(GlobalActionExt.GLOBAL_ACTION_BACK),
        DOUBLE_TAP_ACTION(GlobalActionExt.GLOBAL_ACTION_HOME),
        SWIPE_UP_ACTION(GlobalActionExt.GLOBAL_ACTION_RECENTS),
        SWIPE_DOWN_ACTION(GlobalActionExt.GLOBAL_ACTION_NOTIFICATIONS),
        SWIPE_LEFT_ACTION(GlobalActionExt.GLOBAL_ACTION_NOTIFICATIONS),
        SWIPE_RIGHT_ACTION(GlobalActionExt.GLOBAL_ACTION_NOTIFICATIONS),

        SWIPE_UP_LARGE_ACTION(GlobalActionExt.BYPASS),
        SWIPE_DOWN_LARGE_ACTION(GlobalActionExt.BYPASS),
        SWIPE_LEFT_LARGE_ACTION(GlobalActionExt.BYPASS),
        SWIPE_RIGHT_LARGE_ACTION(GlobalActionExt.BYPASS),

        SCREEN_TO_L_ACTION(GlobalActionExt.BYPASS),
        SCREEN_TO_P_ACTION(GlobalActionExt.BYPASS),


        PANIC_DETECTION(true),

        IME_REPOSITION(true),
        ENABLED(false);

        Object defValue;

        Key(Object defValue) {
            this.defValue = defValue;
        }

        public Object getDefValue() {
            return defValue;
        }
    }

    private SharedPreferences pref;

    public SharedPreferences getPref() {
        return pref;
    }

    public static SettingsProvider get() {
        return sMe;
    }

    private SettingsProvider(Context context) {
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (sMe == null) sMe = new SettingsProvider(context);
    }

    public String toPrefKey(Key key) {
        return key.name().toLowerCase();
    }

    public boolean getBoolean(Key key) {
        return getPref().getBoolean(toPrefKey(key), (Boolean) key.getDefValue());
    }

    public void putBoolean(Key key, boolean value) {
        getPref().edit().putBoolean(toPrefKey(key), value).apply();
        setChanged();
        notifyObservers(key);
    }

    public long getLong(Key key) {
        return getPref().getLong(toPrefKey(key), (Long) key.getDefValue());
    }

    public void putLong(Key key, long value) {
        getPref().edit().putLong(toPrefKey(key), value).apply();
        setChanged();
        notifyObservers(key);
    }

    public int getInt(Key key) {
        return getPref().getInt(toPrefKey(key), (Integer) key.getDefValue());
    }

    public void putInt(Key key, int value) {
        getPref().edit().putInt(toPrefKey(key), value).apply();
        setChanged();
        notifyObservers(key);
    }

    public String getString(Key key) {
        return getPref().getString(toPrefKey(key), (String) key.getDefValue());
    }

    public void putString(Key key, String value) {
        getPref().edit().putString(toPrefKey(key), value).apply();
        setChanged();
        notifyObservers(key);
    }

    public boolean shouldSkipAd() {
        if (BuildConfig.DEBUG) return false;
        if (getBoolean(Key.FORCE_SHOW_AD)) return false;
        long adTime = getLong(Key.AD_CLICKED_TIME_MILLS);
        long timeDis = System.currentTimeMillis() - adTime;
        long limit = 24 * 60 * 60 * 1000;
        Logger.i("timeDis: %s, limit %s, skip %s", timeDis, limit, timeDis < limit);
        return timeDis < limit;
    }
}
