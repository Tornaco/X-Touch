package com.tornaco.xtouch.tiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.service.EventHandlerService;

import java.util.Observable;
import java.util.Observer;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;
import ezy.assist.compat.SettingsCompat;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class ToggleSwitchTile extends QuickTile {

    public ToggleSwitchTile(final Context context) {
        super(context);
        this.titleRes = R.string.title_enabled;
        this.iconRes = R.drawable.ic_check_circle_black_24dp;
        this.tileView = new SwitchTileView(context) {

            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                this.setChecked(EventHandlerService.activated(context));

                if (!SettingsCompat.canDrawOverlays(context)) {
                    new MaterialStyledDialog.Builder(context)
                            .setTitle(R.string.title_need_drawable_overlay)
                            .setDescription(R.string.summary_need_drawable_overlay)
                            .setIcon(R.drawable.ic_hdr_weak_white_24dp)
                            .withDarkerOverlay(false)
                            .setCancelable(false)
                            .setPositiveText(android.R.string.ok)
                            .setNegativeText(android.R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    SettingsCompat.manageDrawOverlays(context);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Activity a = (Activity) context;
                                    a.finish();
                                }
                            })
                            .show();
                }

                SettingsProvider.get().addObserver(new Observer() {
                    @Override
                    public void update(Observable observable, Object o) {
                        if (o == SettingsProvider.Key.ENABLED) {
                            if (isAttachedToWindow()) {
                                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.ENABLED));
                            }
                        }
                    }
                });
            }

            @Override
            protected void onCheckChanged(final boolean checked) {
                super.onCheckChanged(checked);

                if (!SettingsCompat.canDrawOverlays(context)) {
                    new MaterialStyledDialog.Builder(context)
                            .setTitle(R.string.title_need_drawable_overlay)
                            .setDescription(R.string.summary_need_drawable_overlay)
                            .setIcon(R.drawable.ic_hdr_weak_white_24dp)
                            .withDarkerOverlay(false)
                            .setCancelable(false)
                            .setPositiveText(android.R.string.ok)
                            .setNegativeText(android.R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    SettingsCompat.manageDrawOverlays(context);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Activity a = (Activity) context;
                                    a.finish();
                                }
                            })
                            .show();

                    setChecked(false);
                    return;
                }

                new MaterialStyledDialog.Builder(context)
                        .setTitle(R.string.title_need_access_service_enabled)
                        .setDescription(checked ? R.string.summary_need_access_service_enabled
                                : R.string.summary_need_access_service_disbaled)
                        .setIcon(R.drawable.ic_accessibility_white_24dp)
                        .withDarkerOverlay(false)
                        .setCancelable(false)
                        .setPositiveText(android.R.string.ok)
                        .setNegativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                context.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                setChecked(!checked);
                            }
                        })
                        .show();
            }
        };
    }
}
