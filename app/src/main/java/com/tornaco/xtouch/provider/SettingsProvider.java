package com.tornaco.xtouch.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewConfiguration;

import com.tornaco.xtouch.service.GlobalActionExt;

import java.util.Observable;

/**
 * Created by Tornaco on 2017/7/25.
 * Licensed with Apache.
 */
public class SettingsProvider extends Observable {

    private static SettingsProvider sMe;

    private static final String PREF_NAME = "x-touch_app_settings";


    public enum Key {
        PAID(false),
        ROOT(false),
        NO_RECENTS(false),
        SWITCH_APP_WITH_N_FEATURE(true),
        ALPHA(100),
        SIZE(52),
        EDGE(false),
        SOUND(true),
        VIRBATE(true),
        HEART_BEAT(false),
        ROTATE(false),
        FEEDBACK_ANIM(true),
        RESTORE_IME_HIDDEN(true),
        CUSTOM_IMAGE(null),
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
}
