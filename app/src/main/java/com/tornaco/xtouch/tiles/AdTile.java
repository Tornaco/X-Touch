package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.xiaomi.ad.adView.BannerAd;
import com.xiaomi.ad.common.pojo.AdEvent;

import org.newstand.logger.Logger;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.TileView;

/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class AdTile extends QuickTile {

    public static final String TAG = "AD-AdTile";

    private BannerAd mBannerAd;

    private static final String BANNER_POS_ID = "c2eeee2badf84552d2dc7346ddba3acf";

    public AdTile(final Context context) {
        super(context);
        this.tileView = new TileView(context) {
            @Override
            protected void onViewInflated(View view) {
                ViewGroup container = view.findViewById(R.id.container);
                initAd(context, container);
            }

            @Override
            public void setDividerVisibility(boolean visible) {
                // Hooked.
            }

            @Override
            protected int getLayoutId() {
                return R.layout.banner_container;
            }
        };
    }

    private void initAd(Context context, ViewGroup container) {
        mBannerAd = new BannerAd(context, container, new BannerAd.BannerListener() {
            @Override
            public void onAdEvent(AdEvent adEvent) {
                if (adEvent.mType == AdEvent.TYPE_CLICK) {
                    Log.i(TAG, "ad has been clicked!");
                    SettingsProvider.get().putLong(SettingsProvider.Key.AD_CLICKED_TIME_MILLS, System.currentTimeMillis());
                } else if (adEvent.mType == AdEvent.TYPE_SKIP) {
                    Log.i(TAG, "x button has been clicked!");
                } else if (adEvent.mType == AdEvent.TYPE_VIEW) {
                    Log.i(TAG, "ad has been showed!");
                }
            }
        });
        try {
            mBannerAd.show(BANNER_POS_ID);
        } catch (Exception ignored) {
        }
    }

    public void recycle() {
        mBannerAd.recycle();
        Logger.i("recycle@mBannerAd");
    }
}
