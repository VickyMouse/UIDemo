package demo.li.opal.uidemo.Utils;
/**
 * Version 1.0
 * <p/>
 * Date: 2013-10-29 15:36
 * Author: yonnielu
 * <p/>
 * Copyright © 1998-2013 Tencent Technology (Shenzhen) Company Ltd.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import demo.li.opal.uidemo.GlobalContext;

/**
 * Preferences操作相关的常量和方法定义类
 */
public class PrefsUtils {
    private static final String TAG = PrefsUtils.class.getSimpleName();
    /**
     * Prefs name that save all info about version
     */
    /**
     * 目前使用了两个Prefs
     * 1. 默认的Prefs：跟素材没关系的Prefs记录均记录在这里
     * 2. 素材相关的Prefs：跟素材相关的Prefs记录均记录在这里，切Prefs的key命名为：M_PREFS_KEY_
     */
    // 1. 默认Prefs的key 定义
    public static final String PREF_KEY_ROOT = "pref_key_root";

    private static SharedPreferences mPrefs;
    private static SharedPreferences mFeedsPrefs;

    public static void init() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(GlobalContext.getContext());
        mFeedsPrefs = GlobalContext.getContext().getSharedPreferences("pitu_feeds", Context.MODE_PRIVATE);
    }

    /**
     * 获取默认的Prefs
     *
     * @return
     */
    public static SharedPreferences getDefaultPrefs() {
        return mPrefs;
    }

    public static SharedPreferences getFeedsPrefs() {
        return mFeedsPrefs;
    }
}
