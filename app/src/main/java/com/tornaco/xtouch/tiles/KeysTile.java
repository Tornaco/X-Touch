package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.view.View;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.app.ContainerHostActivity;
import com.tornaco.xtouch.app.ContainerHostActivityNoRecents;
import com.tornaco.xtouch.app.KeysFragment;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class KeysTile extends QuickTile {
    public KeysTile(final Context context) {
        super(context);
        this.titleRes = R.string.title_key_settings;
        this.iconRes = R.drawable.ic_gesture_black_24dp;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                boolean noRecent = SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS);
                if (noRecent) {
                    context.startActivity(ContainerHostActivityNoRecents.getIntent(context, KeysFragment.class));
                } else {
                    context.startActivity(ContainerHostActivity.getIntent(context, KeysFragment.class));
                }
            }
        };
    }
}
