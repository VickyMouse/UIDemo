package demo.li.opal.uidemo.Utils;


import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 华为刘海屏适配，Android P之前使用厂商提供接口
 */
public class NotchInScreenUtils {

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
            LogUtils.e(e);
        } catch (NoSuchMethodException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
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
            LogUtils.e(e);
        } catch (NoSuchMethodException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return ret[1];
    }

    /**
     * 谷歌标准，刘海高度 <= statusBarHeight.
     * 如果厂商没有提供特殊接口，可以使用getStatusBarHeight。
     */
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
            LogUtils.e(e);
        } catch (NoSuchMethodException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            return ret;
        }
    }

    /**
     * Mi 判断是否有刘海
     */
    public static boolean hasNotchInScreenAtXM() {
        return "1".equals(getSystemProperties("ro.miui.notch"));
    }

    /**
     *      * Get the value for the given key.
     *      * @return an empty string if the key isn't found
     *      * @throws IllegalArgumentException if the key exceeds 32 characters
     *     
     */
    public static String getSystemProperties(String key) {
        String result = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            result = (String) get.invoke(c, key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *      * Set the value for the given key.
     *      * @throws IllegalArgumentException if the key exceeds 32 characters
     *      * @throws IllegalArgumentException if the value exceeds 92 characters
     *     
     */
    public static void setSystemProperties(String key, String val) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, val);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
