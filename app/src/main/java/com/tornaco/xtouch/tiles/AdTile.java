package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.view.View;

import com.tornaco.xtouch.R;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class AdTile extends QuickTile {

    public AdTile(final Context context) {
        super(context);
        this.titleRes = R.string.title_ad;
        this.iconRes = R.drawable.ic_rss_feed_black_24dp;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        };
    }
}
