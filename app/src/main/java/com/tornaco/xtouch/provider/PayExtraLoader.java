package com.tornaco.xtouch.provider;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.tornaco.xtouch.common.Files;
import com.tornaco.xtouch.common.SharedExecutor;
import com.tornaco.xtouch.model.PayExtra;

import org.newstand.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tornaco on 2017/7/29.
 * Licensed with Apache.
 */

public class PayExtraLoader {

    public interface Callback {
        void onError(Throwable e);

        void onSuccess(List<PayExtra> extras);
    }

    public void loadAsync(final String from, final Callback callback) {
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                load(from, callback);
            }
        });
    }

    public void load(String from, final Callback callback) {
        String tmpDir = com.google.common.io.Files.createTempDir().getPath();
        final String fileName = tmpDir + File.separator + "pays";

        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet(from),
                fileName,
                new AsyncHttpClient.FileCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, File result) {
                        Logger.i("onCompleted %s, %s", e, result);
                        if (e == null) {

                            String content = Files.readString(result.getPath());
                            if (TextUtils.isEmpty(content)) {
                                callback.onError(new Exception("Empty content"));
                                return;
                            }

                            try {
                                final ArrayList<PayExtra> payExtras = new Gson().fromJson(content,
                                        new TypeToken<ArrayList<PayExtra>>() {
                                        }.getType());
                                callback.onSuccess(payExtras);
                            } catch (Throwable w) {
                                Logger.e("Fail to json %s", Logger.getStackTraceString(w));
                                callback.onError(w);
                            }


                        } else {
                            callback.onError(e);
                        }
                    }
                });

    }
}
