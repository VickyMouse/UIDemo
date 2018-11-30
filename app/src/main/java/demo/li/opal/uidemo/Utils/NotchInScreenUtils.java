package demo.li.opal.uidemo.Utils;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 华为刘海屏适配，Android P之前使用厂商提供接口
 */
public class NotchInScreenUtils {
    private static final String TAG = NotchInScreenUtils.class.getSimpleName();

    public static boolean hasNotchInScreen(Context context) {
        return hasNotchInScreenHw(context)
                || hasNotchInScreenAtOppo(context)
                || hasNotchInScreenAtVivo(context)
                || hasNotchInScreenAtXM();
    }

    /**
     * HuaWei 判断是否有刘海
     */
    public static boolean hasNotchInScreenHw(Context context) {
        boolean ret = false;
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class HwNotchSizeUtilClass = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method hasNotchInScreenMethod = HwNotchSizeUtilClass.getMethod("hasNotchInScreen");
            ret = (boolean) hasNotchInScreenMethod.invoke(HwNotchSizeUtilClass);
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return ret;
    }

    /**
     * HuaWei 获取刘海高度
     */
    public static int getNotchHeightHw(Context context) {
        int[] ret = {0, 0};
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class HwNotchSizeUtilClass = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method getNotchSizeMethod = HwNotchSizeUtilClass.getMethod("getNotchSize");
            ret = (int[]) getNotchSizeMethod.invoke(HwNotchSizeUtilClass);
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return ret[1];
    }

    /**
     * 谷歌标准，刘海高度 <= statusBarHeight.
     * 如果厂商没有提供特殊接口，可以使用getStatusBarHeight。
     * */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Oppo 判断是否有刘海
     */
    public static boolean hasNotchInScreenAtOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * Vivo 判断是否有刘海
     */
    public static final int NOTCH_IN_SCREEN_VIVO = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VIVO = 0x00000008;//是否有圆角

    public static boolean hasNotchInScreenAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VIVO);
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            LogUtils.e(TAG, e.getMessage());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        } finally {
            return ret;
        }
    }

    /**
     * XIAOMI 判断是否有刘海
     */
    public static boolean hasNotchInScreenAtXM() {
        String property = System.getProperty("ro.miui.notch");

        if (TextUtils.isEmpty(property))
            return false;

        int ret = 0;
        try {
            ret = Integer.parseInt(property);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return ret == 1;
    }
}
