package com.tornaco.xtouch.tiles;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.service.AdminReceiver;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class LockScreenPermTile extends QuickTile {

    public LockScreenPermTile(final Context context) {
        super(context);

        this.titleRes = R.string.title_lock_perm;
        this.summaryRes = R.string.summary_lock_perm;

        this.iconRes = R.drawable.ic_verified_user_black_24dp;

        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(AdminReceiver.isActivated(context));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);

                if (checked) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, AdminReceiver.name(context));
                    context.startActivity(intent);
                } else {
                    setChecked(true);
                    Snackbar.make(this, R.string.title_turn_off_admin, Snackbar.LENGTH_LONG).show();
                }
            }
        };
    }
}
