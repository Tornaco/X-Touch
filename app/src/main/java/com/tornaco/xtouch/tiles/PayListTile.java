package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.view.View;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.app.ContainerHostActivity;
import com.tornaco.xtouch.app.PayListBrowserFragment;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class PayListTile extends QuickTile {

    public PayListTile(final Context context) {
        super(context);

        this.iconRes = R.drawable.ic_shopping_cart_white_24dp;
        this.titleRes = R.string.title_pay_list;

        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                context.startActivity(ContainerHostActivity.getIntent(context, PayListBrowserFragment.class));
            }
        };


    }
}
