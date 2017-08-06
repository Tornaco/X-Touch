package com.tornaco.xtouch.service;

/**
 * Created by guohao4 on 2017/8/2.
 */

public interface GlobalActionExt {

    int GLOBAL_ACTION_BACK = 1;
    int GLOBAL_ACTION_HOME = 2;
    int GLOBAL_ACTION_NOTIFICATIONS = 4;
    int GLOBAL_ACTION_POWER_DIALOG = 6;
    int GLOBAL_ACTION_QUICK_SETTINGS = 5;
    int GLOBAL_ACTION_RECENTS = 3;
    int GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 7;

    int GLOBAL_ACTION_LOCK_SCREEN = 100;
    int GLOBAL_ACTION_SWITCH_APP = 101;
    int GLOBAL_ACTION_KILL_CURRENT_APP = 102;
    int GLOBAL_ACTION_HIDE = 103;
    int GLOBAL_MEM_OPT = 104;


    int BYPASS = -1;

    int[][] ALL = {
            {0, GLOBAL_ACTION_BACK},
            {1, GLOBAL_ACTION_HOME},
            {2, GLOBAL_ACTION_NOTIFICATIONS},
            {3, GLOBAL_ACTION_QUICK_SETTINGS},
            {4, GLOBAL_ACTION_RECENTS},
            {5, GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN},
            {6, GLOBAL_ACTION_LOCK_SCREEN},
            {7, GLOBAL_ACTION_SWITCH_APP},
            {8, GLOBAL_ACTION_KILL_CURRENT_APP},
            {9, GLOBAL_ACTION_HIDE},
            {10, GLOBAL_MEM_OPT},
            {11, BYPASS},

    };

    void perform(int action);
}
