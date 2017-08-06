package com.tornaco.xtouch.tiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class NoRecentsTile extends QuickTile {

    public NoRecentsTile(final Context context) {
        super(context);

        this.titleRes = R.string.title_no_recents;
        this.iconRes = R.drawable.ic_menu_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.NO_RECENTS, checked);

                ProgressDialog p = new ProgressDialog(context);
                p.setMessage(context.getString(R.string.message_wait_app_to_restart));
                p.setCancelable(false);
                p.setIndeterminate(true);
                p.show();

                SharedExecutor.runOnUIThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                }, 10 * 1000);
            }
        };
    }
}
