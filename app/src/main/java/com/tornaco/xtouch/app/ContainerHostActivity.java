package com.tornaco.xtouch.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tornaco.xtouch.R;

import org.newstand.logger.Logger;

/**
 * Created by Tornaco on 2017/7/27.
 * Licensed with Apache.
 */

public class ContainerHostActivity extends TransitionSafeActivity {

    public static final String EXTRA_FRAGMENT_CLZ = "extra.fr.clz";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_container_with_appbar_template);
        setupToolbar();
        showHomeAsUp();
        replaceV4(R.id.container, onCreateFragment(), null, false);
    }

    public static Intent getIntent(Context context, Class<? extends Fragment> clz) {
        Intent i = new Intent(context, ContainerHostActivity.class);
        i.putExtra(ContainerHostActivity.EXTRA_FRAGMENT_CLZ, clz.getName());
        return i;
    }

    Fragment onCreateFragment() {
        Intent intent = getIntent();
        String clz = intent.getStringExtra(EXTRA_FRAGMENT_CLZ);
        Logger.i("Extra clz:%s", clz);
        if (PayListBrowserFragment.class.getName().equals(clz)) {
            return new PayListBrowserFragment();
        }
        if (ShopFragment.class.getName().equals(clz)) {
            return new ShopFragment();
        }
        return null;
    }
}
