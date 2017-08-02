package com.tornaco.xtouch.app;

import android.os.Bundle;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.tiles.PayListTile;
import com.tornaco.xtouch.tiles.PayStatusTile;
import com.tornaco.xtouch.tiles.ShopAliPayCodeTile;
import com.tornaco.xtouch.tiles.ShopIntroTile;
import com.tornaco.xtouch.tiles.ShopWechatCodeTile;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;


/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class ShopFragment extends DashboardFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.donate);
    }

    @Override
    protected void onCreateDashCategories(List<Category> categories) {
        super.onCreateDashCategories(categories);

        Category about = new Category();
        about.addTile(new ShopIntroTile(getContext()));

        Category payment = new Category();
        payment.titleRes = R.string.title_pay_ment_type;
        payment.addTile(new ShopAliPayCodeTile(getActivity()));
        payment.addTile(new ShopWechatCodeTile(getActivity()));

        Category thanks = new Category();
        thanks.titleRes = R.string.title_thanks;
        thanks.addTile(new PayListTile(getActivity()));
        thanks.addTile(new PayStatusTile(getActivity()));

        categories.add(about);
        categories.add(payment);
        categories.add(thanks);
    }
}
