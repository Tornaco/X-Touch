package com.tornaco.xtouch.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.service.EventHandlerService;
import com.tornaco.xtouch.tiles.ImageTile;
import com.tornaco.xtouch.util.BitmapUtil;

import org.newstand.logger.Logger;

public class MainActivity extends ContainerHostActivity {

    @Override
    Fragment onCreateFragment() {
        return new Dashboard();
    }

    @Override
    protected void showHomeAsUp() {
        // Hooked.
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Send broadcast to show float view.
        Intent intent = new Intent(EventHandlerService.ACTION_RESTORE);
        sendBroadcast(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shop) {
            boolean noRecent = SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS);
            if (noRecent) {
                startActivity(ContainerHostActivityNoRecents.getIntent(this, ShopFragment.class));
            } else {
                startActivity(ContainerHostActivity.getIntent(this, ShopFragment.class));
            }
        }

        if (item.getItemId() == R.id.open_source) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(SettingsProvider.OPEN_SOURCE_GIT_URL);
            intent.setData(content_url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.help) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.help)
                    .setMessage(R.string.message_help)
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageTile.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            SharedExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String path = BitmapUtil.compress(getApplicationContext(),
                                BitmapUtil.decodeUri(getApplicationContext(), data.getData()));
                        Logger.i("Using custom image:%s", path);
                        SettingsProvider.get().putString(SettingsProvider.Key.CUSTOM_IMAGE, path);
                    } catch (Throwable e) {
                        Logger.e(e, "Fail compress.");
                    }
                }
            });
        }
    }
}
