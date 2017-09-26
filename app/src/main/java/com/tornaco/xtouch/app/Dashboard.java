package com.tornaco.xtouch.app;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.tiles.AlphaTile;
import com.tornaco.xtouch.tiles.BlurTile;
import com.tornaco.xtouch.tiles.CropToCircleTile;
import com.tornaco.xtouch.tiles.EdgeTile;
import com.tornaco.xtouch.tiles.HeartbeatTile;
import com.tornaco.xtouch.tiles.IMETile;
import com.tornaco.xtouch.tiles.ImageTile;
import com.tornaco.xtouch.tiles.KeysTile;
import com.tornaco.xtouch.tiles.LockScreenPermTile;
import com.tornaco.xtouch.tiles.LockedTile;
import com.tornaco.xtouch.tiles.NSwitchAppTile;
import com.tornaco.xtouch.tiles.NoRecentsTile;
import com.tornaco.xtouch.tiles.RestoreImeHiddenTile;
import com.tornaco.xtouch.tiles.RotateTile;
import com.tornaco.xtouch.tiles.SizeTile2;
import com.tornaco.xtouch.tiles.SoundTile;
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
        settings.addTile(new IMETile(getContext()));
        settings.addTile(new RestoreImeHiddenTile(getContext()));
        settings.addTile(new LockedTile(getContext()));

        Category view = new Category();
        view.titleRes = R.string.category_view;
        view.addTile(new SizeTile2(getContext()));
        view.addTile(new TapFeedbackTile(getContext()));
        view.addTile(new HeartbeatTile(getContext()));
        view.addTile(new RotateTile(getContext()));
        view.addTile(new AlphaTile(getContext()));

        Category image = new Category();
        image.titleRes = R.string.category_image;

        image.addTile(new ImageTile(getActivity()));
        image.addTile(new CropToCircleTile(getActivity()));
        image.addTile(new BlurTile(getActivity()));

        Category key = new Category();
        key.titleRes = R.string.category_key;
        key.addTile(new KeysTile(getContext()));

        Category dev = new Category();
        dev.titleRes = R.string.summary_exp;
        dev.addTile(new NoRecentsTile(getActivity()));
        dev.addTile(new NSwitchAppTile(getActivity()));

        categories.add(def);
        if (SettingsProvider.get().getBoolean(SettingsProvider.Key.FORCE_SHOW_AD) ||
                !SettingsProvider.get().shouldSkipAd()) {
        }
        categories.add(settings);
        categories.add(key);
        categories.add(view);
        categories.add(image);
        categories.add(dev);
    }

}
