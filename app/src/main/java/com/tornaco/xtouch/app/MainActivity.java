package com.tornaco.xtouch.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tornaco.xtouch.R;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.provider.SettingsProvider;
import com.tornaco.xtouch.service.EventHandlerService;
import com.tornaco.xtouch.tiles.ImageTile;
import com.tornaco.xtouch.util.BitmapUtil;

import org.newstand.logger.Logger;

import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

@RuntimePermissions
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
    protected void showFragment() {
        MainActivityPermissionRequester.onShowFragmentChecked(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_TASKS,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE})
    @RequiresPermission.OnDenied("onPermissionNotGrant")
    protected void onShowFragment() {
        super.onShowFragment();
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

    @Keep
    void onPermissionNotGrant() {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
        finish();
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
