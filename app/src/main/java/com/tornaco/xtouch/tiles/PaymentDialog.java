package com.tornaco.xtouch.tiles;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.tornaco.xtouch.R;


/**
 * Created by Tornaco on 2017/7/29.
 * Licensed with Apache.
 */

class PaymentDialog {

    public static void show(final Context context, int titleRes, int codeRes) {
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(codeRes);
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setCancelable(false)
                .setView(imageView)
                .setPositiveButton(R.string.done, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.save_qr_code, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.save_by_screenshot, Toast.LENGTH_LONG).show();
                    }
                })
                .create().show();
    }
}
