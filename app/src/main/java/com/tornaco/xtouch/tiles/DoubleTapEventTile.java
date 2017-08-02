package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;

import org.newstand.logger.Logger;

import java.util.List;

import dev.nick.tiles.tile.DropDownTileView;
import dev.nick.tiles.tile.QuickTile;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class DoubleTapEventTile extends QuickTile {

    private List<String> descList;

    private boolean firstHook = true;

    public DoubleTapEventTile(Context context) {
        super(context);

        descList = EventRes.getDescriptions(context.getResources());

        this.titleRes = R.string.title_double_tap;
        this.iconRes = R.drawable.ic_touch_app_black_24dp;

        final int event = SettingsProvider.get().getInt(SettingsProvider.Key.DOUBLE_TAP_ACTION);
        this.summaryRes = EventRes.getDescriptionRes(event);

        this.tileView = new DropDownTileView(context) {

            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setSelectedItem(EventRes.getIndex(event), true);
            }

            @Override
            protected List<String> onCreateDropDownList() {
                return descList;
            }

            @Override
            protected void onItemSelected(int position) {
                super.onItemSelected(position);

                if (firstHook) {
                    firstHook = false;
                    return;
                }

                Logger.i("onItemSelected:" + position);

                int event = EventRes.getEvent(position);
                SettingsProvider.get().putInt(SettingsProvider.Key.DOUBLE_TAP_ACTION, event);

                getTileView().getSummaryTextView().setText(descList.get(position));
            }
        };
    }
}
