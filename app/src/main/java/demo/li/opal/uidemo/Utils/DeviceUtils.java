package demo.li.opal.uidemo.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static class MEMORY_CLASS {
        public static final int IN_B = 0; // 计算单位为byte
        public static final int IN_KB = 1; // 计算单位为Kb
        public static final int IN_MB = 2; // 计算单位为Mb
    }

    public static final int MIN_STORAGE_SIZE = 50 * 1024 * 1024; // 50MB

    public static final int MOBILE_NETWORK_2G = 1;
    public static final int MOBILE_NETWORK_3G = 2;
    public static final int MOBILE_NETWORK_4G = 3;
    public static final int MOBILE_NETWORK_UNKNOWN = 4;
    public static final int MOBILE_NETWORK_DISCONNECT = 5;

    /**
     * 网络环境
     */
    public static final int NET_NONE = 0; // 无网
    public static final int NET_WIFI = 1; // WIFI
    public static final int NET_2G = 2; // 2G
    public static final int NET_3G = 3; // 3G
    public static final int NET_4G = 4; // 4G
    public static final int NET_OTHER = 5; // 其他

    private static long sMaxCpuFreq = 0;
    private static int sCpuCount = 0;

    public static int getScreenWidth(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.heightPixels;
    }

    // always return value that is less than 1.0
    public static float getScreenRatio(Context context) {
        int width = DeviceUtils.getScreenWidth(context);
        int height = DeviceUtils.getScreenHeight(context);
        int screenLongSide = Math.max(width, height);
        int screenShortSide = Math.min(width, height);
        if (NotchInScreenUtils.hasNotchInScreenHw(context)) {
            screenLongSide -= NotchInScreenUtils.getNotchHeightHw(context);
        }

        return screenShortSide * 1.0f / screenLongSide;
    }

    /**
     * 获取当前手机系统的最大的HeapSize(Mb)
     *
     * @return
     */
    public static long getHeapMaxSizeInMb(Context context) {
        long memoryClass = getRuntimeMaxMemory(MEMORY_CLASS.IN_MB);
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            LogUtils.v(TAG, "getHeapMaxSizeInMb(), heap size(Mb) = " + activityManager.getMemoryClass());
            memoryClass = activityManager.getMemoryClass();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return memoryClass;
    }

    /**
     * 获取当前手机系统的最大的HeapSize(Kb)
     *
     * @return
     */
    public static long getHeapMaxSizeInKb(Context context) {
        long memoryClass = getRuntimeMaxMemory(MEMORY_CLASS.IN_KB);
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            LogUtils.v(TAG, "getHeapMaxSizeInKb(), heap size(Mb) = " + activityManager.getMemoryClass());
            memoryClass = activityManager.getMemoryClass() * 1024;
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return memoryClass;
    }


    public static long getLargeHeapMaxSizeInKb(Context context) {
        long memoryClass = getRuntimeMaxMemory(MEMORY_CLASS.IN_KB);
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            LogUtils.v(TAG, "getLargeHeapMaxSizeInKb(), heap size(Mb) = " + activityManager.getLargeMemoryClass());
            memoryClass = activityManager.getLargeMemoryClass() * 1024;
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return memoryClass;
    }

    /**
     * 获取整个heap里面除去已经allocated的heap还可以再分配的heap大小
     *
     * @return
     */
    public static long getHeapRemainSizeInKb(Context context) {
        long remainSize = getHeapMaxSizeInKb(context) - getHeapAllocatedSizeInKb();
        LogUtils.v(TAG, "getHeapRemainSizeInKb(), remainSize = " + remainSize / 1024f + "(Mb), " + remainSize + "(Kb)");
        return remainSize;
    }


    public static long getLargeHeapRemainSizeInKb(Context context) {
        long remainSize = getLargeHeapMaxSizeInKb(context) - getHeapAllocatedSizeInKb();
        LogUtils.v(TAG, "getLargeHeapRemainSizeInKb(), remainSize = " + remainSize / 1024f + "(Mb), " + remainSize + "(Kb)");
        return remainSize;
    }

    /**
     * 获取当前已经allocated的堆栈的大小
     *
     * @return
     */
    public static long getHeapAllocatedSizeInKb() {
        long heapAllocated = getRuntimeTotalMemory(MEMORY_CLASS.IN_KB) - getRuntimeFreeMemory(MEMORY_CLASS.IN_KB);
        LogUtils.v(TAG, "getHeapAllocatedSizeInKb(), heapAllocated = " + heapAllocated / 1024f + "(Mb), " + heapAllocated + "(Kb)");
        return heapAllocated;
    }

    /**
     * 获取当前已经allocated的堆栈占总堆栈的比例
     *
     * @return
     */
    public static float getHeapAllocatedPercent(Context context) {
        long heapAllocated = getHeapAllocatedSizeInKb();
        long heapMax = getHeapMaxSizeInKb(context);
        float percent = heapAllocated * 1.0f / heapMax;
        LogUtils.v(TAG, "getHeapAllocatedPercent(), percent = " + percent);
        return percent;
    }

    /**
     * 获取运行时JVM理论上能够使用的最大内存：MaxMemory
     *
     * @param memoryClass MEMORY_CLASS
     * @return
     */
    private static long getRuntimeMaxMemory(int memoryClass) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        switch (memoryClass) {
            case MEMORY_CLASS.IN_B:
                maxMemory = Runtime.getRuntime().maxMemory();
                break;
            case MEMORY_CLASS.IN_KB:
                maxMemory = Runtime.getRuntime().maxMemory() / 1024;
                break;
            case MEMORY_CLASS.IN_MB:
                maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
                break;
            default:
                break;
        }
        LogUtils.v(TAG, "[getRuntimeMaxMemory] maxMemory = " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "(Mb), " + Runtime.getRuntime().maxMemory() / 1024 + "(Kb)");
        return maxMemory;
    }

    public static long getRuntimeRemainSize(int memoryClass) {
        long remainMemory = Runtime.getRuntime().maxMemory() - getHeapAllocatedSizeInKb() * 1024;
        switch (memoryClass) {
            case MEMORY_CLASS.IN_B:
                break;
            case MEMORY_CLASS.IN_KB:
                remainMemory /= 1024;
                break;
            case MEMORY_CLASS.IN_MB:
                remainMemory /= (1024 * 1024);
                break;
            default:
                break;
        }
        LogUtils.v(TAG, "[getRuntimeRemainSize] remainMemory = " + remainMemory + " " + memoryClass);
        return remainMemory;
    }

    /**
     * 获取运行时已经分别的内存大小：totalMemory = allocated + free
     *
     * @param memoryClass MEMORY_CLASS
     * @return
     */
    private static long getRuntimeTotalMemory(int memoryClass) {
        long totalMemory;
        switch (memoryClass) {
            case MEMORY_CLASS.IN_B:
                totalMemory = Runtime.getRuntime().totalMemory();
                break;
            case MEMORY_CLASS.IN_KB:
                totalMemory = Runtime.getRuntime().totalMemory() / 1024;
                break;
            case MEMORY_CLASS.IN_MB:
                totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
                break;
            default:
                totalMemory = Runtime.getRuntime().totalMemory();
                break;
        }
        LogUtils.v(TAG, "[getRuntimeTotalMemory] totalMemory = " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "(Mb), " + Runtime.getRuntime().totalMemory() / 1024 + "(Kb)");
        return totalMemory;
    }

    /**
     * 获取运行时程序以及申请但是还没有使用的空间：freeMemory
     *
     * @param memoryClass MEMORY_CLASS
     * @return
     * @kesenhu: 请注意，理论上这个值没有什么参考意义，请谨慎使用该值作为判断依据
     */
    private static long getRuntimeFreeMemory(int memoryClass) {
        long freeMemory = 0;
        switch (memoryClass) {
            case MEMORY_CLASS.IN_B:
                freeMemory = Runtime.getRuntime().freeMemory();
                break;
            case MEMORY_CLASS.IN_KB:
                freeMemory = Runtime.getRuntime().freeMemory() / 1024;
                break;
            case MEMORY_CLASS.IN_MB:
                freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
                break;
            default:
                freeMemory = Runtime.getRuntime().freeMemory();
                break;
        }
        LogUtils.v(TAG, "[getRuntimeFreeMemory] freeMemory = " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "(Mb), " + Runtime.getRuntime().freeMemory() / 1024 + "(Kb)");
        return freeMemory;
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        if (sCpuCount > 0) {
            return sCpuCount;
        }
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            if (files != null) {
                sCpuCount = files.length;
            } else {
                sCpuCount = 1;
            }
        } catch (Throwable e) {
            LogUtils.e(TAG, e.getMessage());
            sCpuCount = 1;
        }
        LogUtils.v("DeviceUtils", "sCpuCount:" + sCpuCount);
        return sCpuCount;
    }

    //手机CPU主频大小
    public static long getMaxCpuFreq() {
        if (sMaxCpuFreq > 0) {
            return sMaxCpuFreq;
        }
        ProcessBuilder cmd;
        String cpuFreq = "";
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                cpuFreq = cpuFreq + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            LogUtils.e(TAG, ex.getMessage());
            cpuFreq = "";
        }
        cpuFreq = cpuFreq.trim();
        if (cpuFreq == null || cpuFreq.length() == 0) {
            // 某些机器取到的是空字符串，如：OPPO U701
            sMaxCpuFreq = 1;
        } else {
            try {
                sMaxCpuFreq = Long.parseLong(cpuFreq);
            } catch (NumberFormatException e) {
                sMaxCpuFreq = 1;

            }
        }
        LogUtils.v("DeviceUtils", "sMaxCpuFreq:" + sMaxCpuFreq);
        return sMaxCpuFreq;
    }


    public static String getCpuName() {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            if (text != null) {
                String[] array = text.split(":\\s+", 2);
                for (int i = 0; i < array.length; i++) {
                }
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取IMEI号 add by zhenhaiwu
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                LogUtils.i(TAG, "[getImei] IMEI: " + imei);
                return imei;
            }
        } catch (Throwable e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return "";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getTotalSize(StatFs statFs) {
        long availableBytes;
        if (ApiHelper.hasJellyBeanMR2()) {
            availableBytes = statFs.getTotalBytes();
        } else {
            availableBytes = (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
        }
        return availableBytes;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableSize(StatFs statFs) {
        long availableBytes;
        if (ApiHelper.hasJellyBeanMR2()) {
            availableBytes = statFs.getAvailableBytes();
        } else {
            availableBytes = (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }
        return availableBytes;
    }

    /**
     * 返回存储是否可用
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        try {
            //1)java.lang.NullPointerException: Attempt to invoke interface method 'android.os.storage.StorageVolume[] android.os.storage.IMountService.getVolumeList()' on a null object reference
            //2)当禁用写SD卡权限后，下面的语句会抛异常，此时也应该认为SD卡不可用
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回剩余存储容量
     * @return
     */
    public static long getExternalStorageAvailableSize() {
        File sdcard = Environment.getExternalStorageDirectory();
        try {
            StatFs statFs = new StatFs(sdcard.getAbsolutePath());
            return getAvailableSize(statFs);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * The external storage empty size is enough
     *
     * @param fileSize
     * @return
     */
    public static boolean isExternalStorageSpaceEnough(long fileSize) {
        File sdcard = Environment.getExternalStorageDirectory();
        try {
            StatFs statFs = new StatFs(sdcard.getAbsolutePath());
            return !(getAvailableSize(statFs) <= fileSize);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the external app files directory.
     *
     * @param context The context to use
     * @return The external files dir
     */
    // 使用下面的getExternalFilesDir()
    public static File getExternalFilesDir(Context context) {
        File file = context.getExternalFilesDir(null);

        // Before Froyo we need to construct the external cache dir ourselves
        if (file == null) {
            final String filesDir = "/Android/data/" + context.getPackageName() + "/files/";
            file = new File(Environment.getExternalStorageDirectory().getPath() + filesDir);
        }
        return file;
    }

    public static File getExternalFilesDir(Context context, String folder) {
        String path = null;
        //外部存储可写并且有足够的空间
        if (isExternalStorageAvailable() && isExternalStorageSpaceEnough(DeviceUtils.MIN_STORAGE_SIZE)) {
            path = getExternalFilesDir(context).getPath();

        }

        File file = new File(path + File.separator + folder);
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }

            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {

        }

        return file;
    }

    /**
     * 获取app vesioncode add by zhenhaiwu
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getVersionNameCode(Context context) {
        String temp[] = getVersionName(context).split("\\.");
        String versionCode = "";
        if (temp.length >= 3) {
            versionCode = "" + temp[0] + temp[1] + temp[2];
        }
        try {
            return Integer.parseInt(versionCode);
        } catch (Throwable e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return 0;
    }

    /**
     * 获取app vesionName add by zhenhaiwu
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBuildVersionName(Context context) {
        String versionName = getVersionName(context);
        if (TextUtils.isEmpty(versionName)) {
            return "";
        }
        return versionName.substring(versionName.lastIndexOf(".") + 1);
    }


    /**
     * 获取系统号 add by zhenhaiwu
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        final NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    private static boolean mIsAllUnusable = false;
    private static boolean mIsOpenGlEsValid = true;
    private static final int MIN_OPENGL_ES_VERSION = 0x20000; // 支持的最小OPENGL ES版本号，现在设为2
    /**
     * 判断该应用是否合法，是否可以启动
     *
     * @return valid?
     */
    public static boolean isValid(Context context) {
//        if (!mIsLibLegal) {
//            error.append(getResources().getString(R.string.alert_msg_libs));
//            return false;
//        }
//        if (!isL()) {
//            error.append(getResources().getString(R.string.alert_msg_signed));
//            return false;
//        }
        mIsOpenGlEsValid = (getOpenGlEsVersion(context) >= MIN_OPENGL_ES_VERSION);
        //mIsAllUnusable = BlacklistUtils.isAllUnusable();
        mIsAllUnusable = false;
        //LogUtils.v(TAG, "MemoryManager.getInstance().postJob, mIsAllUnusable = " + mIsAllUnusable);
        if (mIsAllUnusable || !mIsOpenGlEsValid) {
            return false;
        }
        return true;
    }

    /**
     * 获取OPenGL ES 版本
     *
     * @param context
     * @return 版本号
     */
    public static int getOpenGlEsVersion(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        if (configurationInfo != null) {
            return configurationInfo.reqGlEsVersion;
        }
        return 0x10000;
    }

    public static int getScreenHeightWithNavigationBar(Activity activity) {
        int height = getScreenHeight(activity);
        int barHeight = getNavigationBarHeight(activity);
        return height + barHeight;
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)) return 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static String getScreenSize(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return "" + display.widthPixels + "*" + display.heightPixels;
    }

    public static boolean veryLargeScreen(Context context) {
        int mut = DeviceUtils.getScreenHeight(context) * DeviceUtils.getScreenWidth(context);
        return mut >= 1920 * 1080;
    }

    /**
     * 获取本地ip地址 add by zhenhaiwu
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            if (NetworkInterface.getNetworkInterfaces() != null) {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    if (en == null) {
                        continue;
                    }
                    NetworkInterface intf = en.nextElement();
                    if (intf != null) {
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            try {
                                if (!inetAddress.isLoopbackAddress()
                                        && inetAddress instanceof Inet4Address && Inet4Address.getByName(inetAddress.getHostAddress()) != null) {
                                    if (!inetAddress.getHostAddress().equals("null")
                                            && inetAddress.getHostAddress() != null) {
                                        return inetAddress.getHostAddress().trim();
                                    }
                                }
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
        return "";
    }

    /**
     * 获取局域网中真实ip
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalRealIpAddress() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces != null && netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            if (!TextUtils.isEmpty(netip)) {
                return netip;
            }
            netip = getExternalLocalIpAddress();
            if (!TextUtils.isEmpty(netip)) {
                return netip;
            } else {
                return localip;

            }
        }

    }

    /**
     * 返回是否为2/3G的移动网络
     *
     * @return
     */
    public static boolean isMobileNetwork(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }

        NetworkInfo mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile != null && mobile.isAvailable() && mobile.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isWifiNetwork(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isAvailable() && wifi.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 返回网络类型
     *
     * @return
     */
    public static int getNetworkType(Context context) {
        int net = NET_OTHER;
        try {
            if (!DeviceUtils.isNetworkAvailable(context)) {
                // 当前未联网
                net = NET_NONE;
            } else if (DeviceUtils.isWifiNetwork(context)) {
                // 当前为WIFI
                net = NET_WIFI;
            } else {
                switch (DeviceUtils.getMobileNetworkType(context)) {
                    case DeviceUtils.MOBILE_NETWORK_2G:
                        net = NET_2G;
                        break;
                    case DeviceUtils.MOBILE_NETWORK_3G:
                        net = NET_3G;
                        break;
                    case DeviceUtils.MOBILE_NETWORK_4G:
                        net = NET_4G;
                        break;
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return net;
    }

    public static int getMobileNetworkType(Context context) {
        if (context == null) {
            return MOBILE_NETWORK_UNKNOWN;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return MOBILE_NETWORK_UNKNOWN;
        }

        NetworkInfo mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile != null && mobile.isAvailable() && mobile.isConnected()) {
            int subType = mobile.getSubtype();
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return MOBILE_NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return MOBILE_NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return MOBILE_NETWORK_4G;
                default:
                    return MOBILE_NETWORK_UNKNOWN;
            }
        } else {
            return MOBILE_NETWORK_DISCONNECT;
        }
    }


    public static String getNetworkTypeName(Context context) {
        switch (getNetworkType(context)) {
            case NET_NONE:
                return "none";
            case NET_2G:
                return "2G";
            case NET_3G:
                return "3G";
            case NET_4G:
                return "4G";
            case NET_WIFI:
                return "wifi";
            default:
                return "unknow";
        }
    }

    public static String getExternalLocalIpAddress() {

        String returnedhtml = fetchExternalIpProviderHTML("http://checkip.dyndns.org/");

        if (returnedhtml == null) {
            return null;
        } else {
            return parse(returnedhtml);
        }
    }

    /**
     * 从外网提供者处获得包含本机外网地址的字符串
     * 从http://checkip.dyndns.org返回的字符串如下
     * <html><head><title>Current IP Check</title></head><body>Current IP Address: 123.147.226.222</body></html>
     *
     * @param externalIpProviderUrl
     * @return
     */
    private static String fetchExternalIpProviderHTML(String externalIpProviderUrl) {
        // 输入流
        InputStream in = null;

        // 到外网提供者的Http连接
        HttpURLConnection httpConn = null;

        try {
            // 打开连接
            URL url = new URL(externalIpProviderUrl);
            httpConn = (HttpURLConnection) url.openConnection();

            // 连接设置
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

            // 获取连接的输入流
            in = httpConn.getInputStream();
            byte[] bytes = new byte[1024];// 此大小可根据实际情况调整

            // 读取到数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // 将字节转化为为UTF-8的字符串
            String receivedString = new String(bytes, "UTF-8");

            // 返回
            return receivedString;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 出现异常则返回空
        return null;
    }

    /**
     * 使用正则表达式解析返回的HTML文本,得到本机外网地址
     *
     * @param html
     */
    private static String parse(String html) {
        Pattern pattern = Pattern.compile("(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /**
     * 以尝试写入的方式判断一个目录是否可写
     */
    public static boolean canWriteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        String dirPath = filePath;
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath.substring(0, dirPath.lastIndexOf(File.separator));
        }
        File temp = new File(dirPath + File.separator + "test_temp.txt");
        try {
            if (temp.exists()) {
                temp.delete();
            }
            temp.createNewFile();
            temp.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp.delete();
        return false;
    }

    public static float getWindowScreenBrightness(Window window){
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        return localLayoutParams.screenBrightness;
    }

    public static void setWindowScreenBrightness(Window window, float brightness){
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = brightness;
        window.setAttributes(localLayoutParams);
    }

    public static int getSystemScreenBrightness(ContentResolver resolver) {
        int nowBrightnessValue = 0;
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return nowBrightnessValue;
    }

    public static int getSystemScreenMode(ContentResolver resolver) {
        int mode = -1;
        try {
            mode = Settings.System.getInt(resolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return mode;
    }

    public static void setSystemScreenMode(ContentResolver resolver, int mode) {
        try {
            Settings.System.putInt(resolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
            Uri uri = Settings.System
                    .getUriFor("screen_brightness_mode");
            resolver.notifyChange(uri, null);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }

    public static void setSystemScreenBrightness(ContentResolver resolver, int brightness){
        try{
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp(像素)
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static double calDistance(PointF a, PointF b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
