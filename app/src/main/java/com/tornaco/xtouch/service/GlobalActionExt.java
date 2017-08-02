package com.tornaco.xtouch.service;

/**
 * Created by guohao4 on 2017/8/2.
 */

public interface GlobalActionExt {
    int GLOBAL_ACTION_BACK = 1;
    int GLOBAL_ACTION_HOME = 2;
    int GLOBAL_ACTION_NOTIFICATIONS = 4;
    int GLOBAL_ACTION_QUICK_SETTINGS = 5;
    int GLOBAL_ACTION_RECENTS = 3;
    int GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 7;
    int GLOBAL_ACTION_LOCK_SCREEN = 8;
    int BYPASS = -1;

    int[][] ALL = {
            {0, GLOBAL_ACTION_BACK},
            {1, GLOBAL_ACTION_HOME},
            {2, GLOBAL_ACTION_NOTIFICATIONS},
            {3, GLOBAL_ACTION_QUICK_SETTINGS},
            {4, GLOBAL_ACTION_RECENTS},
            {5, GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN},
            {6, GLOBAL_ACTION_LOCK_SCREEN},
            {7, BYPASS},

    };

    void perform(int action);
}
