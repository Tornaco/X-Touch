package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by Tornaco on 2017/7/29.
 * Licensed with Apache.
 */

public class PayStatusTile extends QuickTile {

    public PayStatusTile(final Context context) {
        super(context);

        this.titleRes = R.string.title_pay_status_paid;
        this.iconRes = R.drawable.ic_check_circle_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.PAID));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.PAID, checked);

                if (checked) {
                    Toast.makeText(context, "✧ (≖ ‿ ≖)✧", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
