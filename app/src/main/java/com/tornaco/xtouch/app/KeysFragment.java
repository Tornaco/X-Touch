package com.tornaco.xtouch.app;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.tiles.DoubleTapEventTile;
import com.tornaco.xtouch.tiles.LargeSlopTile;
import com.tornaco.xtouch.tiles.LongPressDelayTile;
import com.tornaco.xtouch.tiles.PanicDetectionTile;
import com.tornaco.xtouch.tiles.ScreenToLEventTile;
import com.tornaco.xtouch.tiles.ScreenToPEventTile;
import com.tornaco.xtouch.tiles.SingleTapEventTile;
import com.tornaco.xtouch.tiles.SwipeDownEventTile;
import com.tornaco.xtouch.tiles.SwipeDownLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeLeftEventTile;
import com.tornaco.xtouch.tiles.SwipeLeftLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeRightEventTile;
import com.tornaco.xtouch.tiles.SwipeRightLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeUpEventTile;
import com.tornaco.xtouch.tiles.SwipeUpLargeEventTile;
import com.tornaco.xtouch.tiles.TapDelayTile;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;

public class KeysFragment extends DashboardFragment {
    @Override
    protected void onCreateDashCategories(List<Category> categories) {
        super.onCreateDashCategories(categories);

        Category key = new Category();
        key.titleRes = R.string.category_key;
        key.addTile(new SingleTapEventTile(getContext()));
        key.addTile(new DoubleTapEventTile(getContext()));
        key.addTile(new TapDelayTile(getContext()));
        key.addTile(new LongPressDelayTile(getContext()));
        key.addTile(new PanicDetectionTile(getContext()));

        Category ges = new Category();
        ges.titleRes = R.string.category_gesture;
        ges.addTile(new SwipeLeftEventTile(getContext()));
        ges.addTile(new SwipeRightEventTile(getContext()));
        ges.addTile(new SwipeUpEventTile(getContext()));
        ges.addTile(new SwipeDownEventTile(getContext()));

        Category gesLarge = new Category();
        gesLarge.titleRes = R.string.category_gesture_large;
        gesLarge.addTile(new LargeSlopTile(getContext()));
        Category space = new Category();
        space.addTile(new SwipeLeftLargeEventTile(getContext()));
        space.addTile(new SwipeRightLargeEventTile(getContext()));
        space.addTile(new SwipeUpLargeEventTile(getContext()));
        space.addTile(new SwipeDownLargeEventTile(getContext()));

        Category screen = new Category();
        screen.titleRes = R.string.category_screen;
        screen.addTile(new ScreenToLEventTile(getContext()));
        screen.addTile(new ScreenToPEventTile(getContext()));

        Category others = new Category();
        others.titleRes = R.string.category_others;

        categories.add(key);
        categories.add(screen);

        categories.add(ges);
        categories.add(gesLarge);
    }
}
