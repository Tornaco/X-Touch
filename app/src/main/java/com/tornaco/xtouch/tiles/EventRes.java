package com.tornaco.xtouch.tiles;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.service.GlobalActionExt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class EventRes {

    public static
    @StringRes
    int getDescriptionRes(int action) {
        switch (action) {
            case GlobalActionExt.GLOBAL_ACTION_BACK:
                return R.string.action_back;
            case GlobalActionExt.GLOBAL_ACTION_HOME:
                return R.string.action_home;
            case GlobalActionExt.GLOBAL_ACTION_RECENTS:
                return R.string.action_recent;
            case GlobalActionExt.GLOBAL_ACTION_NOTIFICATIONS:
                return R.string.action_notification;
            case GlobalActionExt.GLOBAL_ACTION_LOCK_SCREEN:
                return R.string.action_lock_screen;
            case GlobalActionExt.GLOBAL_ACTION_QUICK_SETTINGS:
                return R.string.action_quick_settings;
            case GlobalActionExt.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN:
                return R.string.action_spli_screen;
        }
        return R.string.noop;
    }

    public static
    @NonNull
    List<String> getDescriptions(Resources resources) {
        int[][] all = GlobalActionExt.ALL;
        List<String> descList = new ArrayList<>(all.length);
        for (int[] es : all) {
            int strRes = getDescriptionRes(es[1]);
            descList.add(resources.getString(strRes));
        }
        return descList;
    }

    public static int getIndex(int action) {
        int[][] all = GlobalActionExt.ALL;
        for (int[] es : all) {
            if (es[1] == action) return es[0];
        }
        return 0;
    }

    public static int getEvent(int index) {
        int[][] all = GlobalActionExt.ALL;
        for (int[] es : all) {
            if (es[0] == index) return es[1];
        }
        return GlobalActionExt.GLOBAL_ACTION_BACK;
    }
}
