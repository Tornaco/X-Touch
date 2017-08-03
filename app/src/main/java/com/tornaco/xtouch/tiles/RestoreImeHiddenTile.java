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

public class RestoreImeHiddenTile extends QuickTile {
    public RestoreImeHiddenTile(Context context) {
        super(context);
        this.titleRes = R.string.title_restore_on_ime_hidden;
        this.summaryRes = R.string.summary_restore_on_ime_hidden;
        this.iconRes = R.drawable.ic_settings_backup_restore_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.RESTORE_IME_HIDDEN));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.RESTORE_IME_HIDDEN, checked);
            }
        };
    }
}
