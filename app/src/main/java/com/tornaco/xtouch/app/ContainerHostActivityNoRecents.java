package com.tornaco.xtouch.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

public class ContainerHostActivityNoRecents extends ContainerHostActivity {
    public static Intent getIntent(Context context, Class<? extends Fragment> clz) {
        Intent i = new Intent(context, ContainerHostActivityNoRecents.class);
        i.putExtra(ContainerHostActivityNoRecents.EXTRA_FRAGMENT_CLZ, clz.getName());
        return i;
    }
}
