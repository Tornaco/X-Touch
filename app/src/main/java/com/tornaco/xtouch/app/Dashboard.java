package com.tornaco.xtouch.app;

import com.tornaco.xtouch.BuildConfig;
import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.tiles.AdTile;
import com.tornaco.xtouch.tiles.AlphaTile;
import com.tornaco.xtouch.tiles.DoubleTapEventTile;
import com.tornaco.xtouch.tiles.EdgeTile;
import com.tornaco.xtouch.tiles.HeartbeatTile;
import com.tornaco.xtouch.tiles.IMETile;
import com.tornaco.xtouch.tiles.ImageTile;
import com.tornaco.xtouch.tiles.LockScreenPermTile;
import com.tornaco.xtouch.tiles.LongPressDelayTile;
import com.tornaco.xtouch.tiles.NSwitchAppTile;
import com.tornaco.xtouch.tiles.NoRecentsTile;
import com.tornaco.xtouch.tiles.RestoreImeHiddenTile;
import com.tornaco.xtouch.tiles.RootTile;
import com.tornaco.xtouch.tiles.RotateTile;
import com.tornaco.xtouch.tiles.SingleTapEventTile;
import com.tornaco.xtouch.tiles.SizeTile;
import com.tornaco.xtouch.tiles.SoundTile;
import com.tornaco.xtouch.tiles.SwipeDownEventTile;
import com.tornaco.xtouch.tiles.SwipeLeftEventTile;
import com.tornaco.xtouch.tiles.SwipeRightEventTile;
import com.tornaco.xtouch.tiles.SwipeUpEventTile;
import com.tornaco.xtouch.tiles.TapDelayTile;
import com.tornaco.xtouch.tiles.TapFeedbackTile;
import com.tornaco.xtouch.tiles.ToggleSwitchTile;
import com.tornaco.xtouch.tiles.VibrateTile;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;

public class Dashboard extends DashboardFragment {
    @Override
    protected void onCreateDashCategories(List<Category> categories) {
        super.onCreateDashCategories(categories);
        Category def = new Category();
        def.titleRes = R.string.category_status;
        def.addTile(new ToggleSwitchTile(getActivity()));
        def.addTile(new LockScreenPermTile(getActivity()));

        Category settings = new Category();
        settings.titleRes = R.string.category_settings;
        settings.addTile(new EdgeTile(getContext()));
        settings.addTile(new SoundTile(getContext()));
        settings.addTile(new VibrateTile(getContext()));
        settings.addTile(new TapDelayTile(getContext()));
        settings.addTile(new LongPressDelayTile(getContext()));
        settings.addTile(new IMETile(getContext()));
        settings.addTile(new RestoreImeHiddenTile(getContext()));

        Category ad = new Category();
        if (!BuildConfig.DEBUG && !SettingsProvider.get().getBoolean(SettingsProvider.Key.PAID)) {
            ad.titleRes = R.string.title_ad_area;
            ad.addTile(new AdTile(getContext()));
        }

        Category anim = new Category();
        anim.titleRes = R.string.category_view;
        anim.addTile(new SizeTile(getContext()));
        anim.addTile(new TapFeedbackTile(getContext()));
        anim.addTile(new HeartbeatTile(getContext()));
        anim.addTile(new RotateTile(getContext()));
        anim.addTile(new AlphaTile(getContext()));
        anim.addTile(new ImageTile(getActivity()));

        Category key = new Category();
        key.titleRes = R.string.category_key;
        key.addTile(new SingleTapEventTile(getContext()));
        key.addTile(new DoubleTapEventTile(getContext()));
        key.addTile(new SwipeLeftEventTile(getContext()));
        key.addTile(new SwipeRightEventTile(getContext()));
        key.addTile(new SwipeUpEventTile(getContext()));
        key.addTile(new SwipeDownEventTile(getContext()));

        Category dev = new Category();
        dev.titleRes = R.string.summary_exp;
        dev.addTile(new RootTile(getActivity()));
        dev.addTile(new NoRecentsTile(getActivity()));
        dev.addTile(new NSwitchAppTile(getActivity()));

        categories.add(def);
        categories.add(settings);
        categories.add(ad);
        categories.add(anim);
        categories.add(key);
        categories.add(dev);
    }
}
