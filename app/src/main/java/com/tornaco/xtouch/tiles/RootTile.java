package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.chrisplus.rootmanager.RootManager;
import com.tornaco.xtouch.R;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/3.
 * Email: Tornaco@163.com
 */

public class RootTile extends QuickTile {

    public RootTile(Context context) {
        super(context);

        this.titleRes = R.string.title_root_enabled;
        this.summaryRes = R.string.summary_root_enabled;
        this.iconRes = R.drawable.ic_extension_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.ROOT));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.ROOT, checked);
                if (checked) {
                    SharedExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (!RootManager.getInstance().obtainPermission()) {
                                SettingsProvider.get().putBoolean(SettingsProvider.Key.ROOT, false);
                            }
                        }
                    });
                }
            }
        };
    }
}
