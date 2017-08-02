package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import java.util.Observable;
import java.util.Observer;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

public class AlphaTile extends QuickTile {

    public AlphaTile(@NonNull Context context) {
        super(context);
        this.titleRes = R.string.title_alpha;
        this.iconRes = R.drawable.ic_gradient_black_24dp;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                showAlphaSeeker();
            }
        };
        this.summary = String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.ALPHA));
        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg == SettingsProvider.Key.ALPHA) {
                    getTileView().getSummaryTextView().setText(
                            String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.ALPHA))
                    );
                }
            }
        });
    }

    private void showAlphaSeeker() {
        final SeekBar seekBar = new SeekBar(getContext());
        int alpha = SettingsProvider.get().getInt(SettingsProvider.Key.ALPHA);
        seekBar.setMax(100);
        seekBar.setProgress(alpha);
        new AlertDialog.Builder(getContext())
                .setView(seekBar)
                .setTitle(R.string.title_alpha)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int current = seekBar.getProgress();
                        SettingsProvider.get().putInt(SettingsProvider.Key.ALPHA, current);
                    }
                })
                .create()
                .show();
    }
}
