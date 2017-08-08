package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class NSwitchAppTile extends QuickTile {

    public NSwitchAppTile(final Context context) {
        super(context);

        this.titleRes = R.string.title_n_switch;
        this.summaryRes =R.string.summary_n_switch;
        this.iconRes = R.drawable.ic_skip_next_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.SWITCH_APP_WITH_N_FEATURE));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.SWITCH_APP_WITH_N_FEATURE, checked);
            }
        };
    }
}
