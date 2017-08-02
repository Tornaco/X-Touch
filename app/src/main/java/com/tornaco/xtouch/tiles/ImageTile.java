package com.tornaco.xtouch.tiles;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import org.newstand.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.nick.tiles.tile.DropDownTileView;
import dev.nick.tiles.tile.QuickTile;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class ImageTile extends QuickTile {

    public static final int REQUEST_CODE_PICK_IMAGE = 0x1;

    private boolean mHook = true;

    public ImageTile(final Activity context) {
        super(context);

        this.titleRes = R.string.title_image;
        this.iconRes = R.drawable.ic_photo_black_24dp;
        String customImage = SettingsProvider.get().getString(SettingsProvider.Key.CUSTOM_IMAGE);
        final boolean exist = !TextUtils.isEmpty(customImage) && new File(customImage).exists();

        this.summaryRes = !exist ? R.string.summary_def : R.string.summary_custom;

        this.tileView = new DropDownTileView(context) {

            @Override
            protected List<String> onCreateDropDownList() {
                String def = context.getString(R.string.summary_def);
                String cust = context.getString(R.string.summary_custom);
                List<String> list = new ArrayList<>();
                list.add(def);
                list.add(cust);
                return list;
            }

            @Override
            protected int getInitialSelection() {
                return exist ? 1 : 0;
            }

            @Override
            protected void onItemSelected(int position) {
                super.onItemSelected(position);

                Logger.i("onItemSelected: %s, hook: %s", position, mHook);

                if (mHook) {
                    mHook = false;
                    return;
                }


                if (position == 0) {
                    SettingsProvider.get().putString(SettingsProvider.Key.CUSTOM_IMAGE, null);
                } else {
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    context.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                }

                getSummaryTextView().setText(position == 0 ? R.string.summary_def : R.string.summary_custom);
            }
        };
    }
}
