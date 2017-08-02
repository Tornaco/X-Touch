package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class AdTile extends QuickTile {

    private int clicked = 0;

    private long startTime;

    private Toast toast;

    public AdTile(final Context context) {
        super(context);
        this.titleRes = R.string.title_ad;
        this.iconRes = R.drawable.ic_rss_feed_black_24dp;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                if (toast != null) toast.cancel();
                toast = Toast.makeText(context, String.valueOf(clicked), Toast.LENGTH_SHORT);
                toast.show();
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                clicked++;
                if (clicked > 128) {
                    SettingsProvider.get().putBoolean(SettingsProvider.Key.PAID, true);
                    long takeTime = System.currentTimeMillis() - startTime;
                    Toast.makeText(context, ":+" + takeTime + "ms", Toast.LENGTH_LONG).show();
                    clicked = 0;
                }
            }
        };
    }
}
