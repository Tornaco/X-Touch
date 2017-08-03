package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/3.
 * Email: Tornaco@163.com
 */

public class TapFeedbackTile extends QuickTile {

    public TapFeedbackTile(Context context) {
        super(context);

        this.titleRes = R.string.title_tap_feedback;
        this.iconRes = R.drawable.ic_flash_on_black_24dp;

        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.FEEDBACK_ANIM));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.FEEDBACK_ANIM, checked);
            }
        };
    }
}
