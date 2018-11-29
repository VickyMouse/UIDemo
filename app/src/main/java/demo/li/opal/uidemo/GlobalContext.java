package demo.li.opal.uidemo;
/**
 * Version 1.0
 *
 * Date: 2013-12-19 21:43
 * Author: yonnielu
 *
 * Copyright © 1998-2013 Tencent Technology (Shenzhen) Company Ltd.
 *
 */

import android.content.Context;

/**
 * 全局Context类，在 {@code Application} 初始化的时候注入Application的Context实例
 */
public class GlobalContext {
    private static Context sContext;

    /**
     * 请不要在其他任何地方调用setContext的方法,已经在Application初始化的时候调用了该方法
     *
     * @param context
     */
    public static void setContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
