package com.tornaco.xtouch.common;

import org.newstand.logger.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Nick@NewStand.org on 2017/3/9 13:38
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

public abstract class Closer {
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignore) {
            Logger.e(ignore, "Fail to close %s with err", closeable);
        }
    }
}
