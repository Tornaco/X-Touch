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

public class SizeTile2 extends QuickTile {

    public SizeTile2(@NonNull Context context) {
        super(context);
        this.titleRes = R.string.title_size;
        this.iconRes = R.drawable.ic_adjust_black_24dp;
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                showSeeker();
            }
        };
        this.summary = String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.SIZE));
        SettingsProvider.get().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg == SettingsProvider.Key.SIZE) {
                    getTileView().getSummaryTextView().setText(
                            String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.SIZE))
                    );
                }
            }
        });
    }

    private void showSeeker() {
        final SeekBar seekBar = new SeekBar(getContext());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SettingsProvider.get().putInt(SettingsProvider.Key.SIZE, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int size = SettingsProvider.get().getInt(SettingsProvider.Key.SIZE);
        seekBar.setMax(100);
        seekBar.setProgress(size);
        new AlertDialog.Builder(getContext())
                .setView(seekBar)
                .setTitle(R.string.title_size)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int current = seekBar.getProgress();
                        SettingsProvider.get().putInt(SettingsProvider.Key.SIZE, current);
                    }
                })
                .create()
                .show();
    }
}
