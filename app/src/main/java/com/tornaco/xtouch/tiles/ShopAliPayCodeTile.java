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

public class ShopAliPayCodeTile extends QuickTile {

    public ShopAliPayCodeTile(final Context context) {
        super(context);

        this.iconRes = R.drawable.ic_alipay;
        this.titleRes = R.string.title_alipay;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                PaymentDialog.show(context, R.string.title_alipay, R.drawable.qr_alipay);
            }

            @Override
            protected boolean useStaticTintColor() {
                return false;
            }
        };


    }
}
