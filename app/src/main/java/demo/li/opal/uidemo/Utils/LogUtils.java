package demo.li.opal.uidemo.Utils;

import android.content.res.Configuration;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

public class LogUtils {

    public static boolean isLogcatEnable = false;

    public static void setLogcatEnable(boolean enable) {
        isLogcatEnable = enable;
    }

    private static final Configuration sConfiguration = new Configuration();

    public static void v(String tag, String msg) {
        if (isLogcatEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = "(" + Thread.currentThread().getName() + ")" + stackTrace.getClassName() + "(" + stackTrace.getLineNumber() + ")[" + stackTrace.getMethodName() + "]";
            String all = fileInfo + ": " + msg;
            Log.v(tag, all);
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.v(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (isLogcatEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = "(" + Thread.currentThread().getName() + ")" + stackTrace.getClassName() + "(" + stackTrace.getLineNumber() + ")[" + stackTrace.getMethodName() + "]";
            String all = fileInfo + ": " + msg;
            Log.i(tag, all);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.i(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (isLogcatEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = "(" + Thread.currentThread().getName() + ")" + stackTrace.getClassName() + "(" + stackTrace.getLineNumber() + ")[" + stackTrace.getMethodName() + "]";
            String all = fileInfo + ": " + msg;
            Log.d(tag, all);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.d(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (isLogcatEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = "(" + Thread.currentThread().getName() + ")" + stackTrace.getClassName() + "(" + stackTrace.getLineNumber() + ")[" + stackTrace.getMethodName() + "]";
            String all = fileInfo + ": " + msg;
            Log.w(tag, all);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.w(tag, msg, tr);
        }
    }

    public static void e(Throwable tr) {
        e("", tr.getMessage());
    }

    public static void e(String tag, Throwable tr) {
        if (isLogcatEnable) {
            Log.e(tag, "", tr);
        }
    }

    public static void e(String tag, String msg) {
        if (isLogcatEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = "(" + Thread.currentThread().getName() + ")" + stackTrace.getClassName() + "(" + stackTrace.getLineNumber() + ")[" + stackTrace.getMethodName() + "]";
            String all = fileInfo + ": " + msg;
            Log.e(tag, all);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr, Object... args) {
        if (isLogcatEnable) {
            msg = getFormatMessage(tag, msg, args);
            Log.e(tag, msg, tr);
        }
    }

    private static long mLastTime;
    private static long mInitTime;

    private static LruCache<String, Long> mTimeStampMap = new LruCache<>(1000);

    public static void printTime(String key, String msg) {
        if (!isLogcatEnable || TextUtils.isEmpty(key) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (msg.contains("BEGIN")) {
            long currentTimeMillis = System.currentTimeMillis();
            mTimeStampMap.put(key, currentTimeMillis);
            LogUtils.i("LOG_PERFORMANCE_" + key, msg);
        } else if (msg.contains("END")) {
            Long beginTimeObj = mTimeStampMap.get(key);
            long beginTime;
            if (beginTimeObj != null) {
                beginTime = beginTimeObj;
            } else {
                beginTime = System.currentTimeMillis();
            }
            long currentTimeMillis = System.currentTimeMillis();
            long timeCost = currentTimeMillis - beginTime;
            LogUtils.i("LOG_PERFORMANCE_" + key, msg + " = " + timeCost);
        } else {
            LogUtils.i("LOG_PERFORMANCE_" + key, msg);
        }
    }

    // 初始化记录消耗的系统初始时间
    private static long initTime() {
        mLastTime = SystemClock.currentThreadTimeMillis();
        mInitTime = mLastTime;
        return mLastTime;
    }

    private static String getFormatMessage(String tag, String msg, Object... args) {
        if (args != null) {
            try {
                msg = String.format(sConfiguration.locale, msg, args);
            } catch (Exception e) {
                // ignore
            }
        }
        return generateLogPrefix(tag) + msg;
    }

    /**
     * 生成Log日志的前缀信息。如下格式：
     * 当前线程名+文件名+行号+方法名
     *
     * @param simpleClassName
     * @return
     */
    private static String generateLogPrefix(String simpleClassName) {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return "";
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().endsWith(simpleClassName)) {
                return "(" + Thread.currentThread().getName() + ")" + st.getClassName() + "(" + st.getLineNumber() + ")[" + st.getMethodName() + "]: ";
            }
        }
        return "";
    }
}
