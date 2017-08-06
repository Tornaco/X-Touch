package com.tornaco.xtouch.service;

/**
 * Created by guohao4 on 2017/8/4.
 * Email: Tornaco@163.com
 */

public interface AppSwitcher {
    boolean switchApp(EventHandlerService service);
    void killCurrent();
}
