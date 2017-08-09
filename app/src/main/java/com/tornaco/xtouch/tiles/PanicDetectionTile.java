package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/9.
 * Email: Tornaco@163.com
 */

public class PanicDetectionTile extends QuickTile {
    public PanicDetectionTile(Context context) {
        super(context);

        this.titleRes = R.string.title_panic_detection;
        this.summaryRes = R.string.summary_panic_detection;
        this.iconRes = R.drawable.ic_mood_bad_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.PANIC_DETECTION));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.PANIC_DETECTION, checked);
            }
        };
    }
}
