package com.tornaco.xtouch.app;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.tiles.DoubleTapEventTile;
import com.tornaco.xtouch.tiles.LargeSlopTile;
import com.tornaco.xtouch.tiles.SingleTapEventTile;
import com.tornaco.xtouch.tiles.SwipeDownEventTile;
import com.tornaco.xtouch.tiles.SwipeDownLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeLeftEventTile;
import com.tornaco.xtouch.tiles.SwipeLeftLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeRightEventTile;
import com.tornaco.xtouch.tiles.SwipeRightLargeEventTile;
import com.tornaco.xtouch.tiles.SwipeUpEventTile;
import com.tornaco.xtouch.tiles.SwipeUpLargeEventTile;

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

        Category ges = new Category();
        ges.titleRes = R.string.category_gesture;
        ges.addTile(new SwipeLeftEventTile(getContext()));
        ges.addTile(new SwipeRightEventTile(getContext()));
        ges.addTile(new SwipeUpEventTile(getContext()));
        ges.addTile(new SwipeDownEventTile(getContext()));

        Category largeSlop = new Category();
        largeSlop.addTile(new LargeSlopTile(getContext()));

        Category gesLarge = new Category();
        gesLarge.titleRes = R.string.category_gesture_large;
        gesLarge.addTile(new SwipeLeftLargeEventTile(getContext()));
        gesLarge.addTile(new SwipeRightLargeEventTile(getContext()));
        gesLarge.addTile(new SwipeUpLargeEventTile(getContext()));
        gesLarge.addTile(new SwipeDownLargeEventTile(getContext()));

        categories.add(key);
        categories.add(ges);
        categories.add(largeSlop);
        categories.add(gesLarge);
    }
}
