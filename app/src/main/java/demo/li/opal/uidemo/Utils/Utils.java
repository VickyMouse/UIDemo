package demo.li.opal.uidemo.Utils;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Collection of utility functions used in this package.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static final String RES_PREFIX_ASSETS = "assets://";
    public static final String RES_PREFIX_STORAGE = "/";
    public static final String RES_PREFIX_HTTP = "http://";
    public static final String RES_PREFIX_HTTPS = "https://";

    //@Kesen:因为在BrowserActivity会出现load同一张图的情况(key相同)，根据水印相机之前遇到的情况，有可能出现问题，故加前缀进行区分
    //public static final String PREFIX_FOR_LOADER = "SELECT_KEY";

    public static final int JPEG_QUALITY = 99;

    public static final int APP_TYPE_RELEASE = 0;     // 正式发布
    public static final int APP_TYPE_RDM = 1;     // 内部测试
    public static final int APP_TYPE_ALPHA = 2;     // 外团
    public static final int APP_TYPE_HDBM = 3;     // 码包灰度

    public static boolean isEmpty(@Nullable Object object) {
        return null == object || (object instanceof Collection ? ((Collection) object).isEmpty() : object.toString().isEmpty());
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

    /**
     * Convert px to sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * Convert sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 文字sp单位转换成px单位
     *
     * @param size
     * @return
     */
    public static float getSpSize(Context ctx, float size) {
        Resources r;

        if (ctx == null) {
            r = Resources.getSystem();
        } else {
            r = ctx.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
    }

    /**
     * 根据QUA获取当前发布的版本类型
     *
     * @param qua
     * @return
     */
    public static int getAppType(String qua) {
        if (!TextUtils.isEmpty(qua)) {
            qua = qua.toLowerCase();
            if (qua.contains("_rdm")) {
                return APP_TYPE_RDM;
            } else if (qua.contains("_alpha")) {
                return APP_TYPE_ALPHA;
            } else if (qua.contains("_hdbm")) {
                return APP_TYPE_HDBM;
            }
        }
        return APP_TYPE_RELEASE;
    }

    /**
     * 是否测试版本
     *
     * @param qua
     * @return
     */
    public static boolean isTestVersion(String qua) {
        int appType = getAppType(qua);
        switch (appType) {
            case Utils.APP_TYPE_HDBM:
            case Utils.APP_TYPE_ALPHA:
            case Utils.APP_TYPE_RDM:
                return true;
        }
        return false;
    }

    /**
     * 获得去除 {@link #RES_PREFIX_ASSETS} 前缀的资源路径
     *
     * @param path
     * @return
     */
    public static String getRealPath(String path) {
        return TextUtils.isEmpty(path) ? path : path.startsWith(RES_PREFIX_ASSETS) ? path.substring(RES_PREFIX_ASSETS.length()) : path;
    }

    /**
     * 获取app是否已安装
     *
     * @param context
     * @param pakName 包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String pakName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pakName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            LogUtils.e(e);
        }
        return packageInfo != null;
    }

    public static double calDistance(PointF a, PointF b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 人脸83点转90点，参照底层facecheck_jni.cpp编写
    public static int[][] convert83PointsTo90Points(int[][] points83) {
        int[][] points90 = new int[90][2];
        if (points83 == null || points83.length < 83) {
            return points90;
        }
        for (int i = 0; i < 83; ++i) {
            points90[i][0] = points83[i][0];
            points90[i][1] = points83[i][1];
        }
        points90[83][0] = (points83[55][0] + points83[63][0]) / 2;
        points90[83][1] = (points83[55][1] + points83[63][1]) / 2;
        points90[84][0] = (points83[23][0] + points83[31][0]) / 2;
        points90[84][1] = (points83[23][1] + points83[31][1]) / 2;
        points90[85][0] = (points83[59][0] + points83[77][0]) / 2;
        points90[85][1] = (points83[59][1] + points83[77][1]) / 2;
        points90[86][0] = points83[35][0] * 2 - points83[6][0];
        points90[86][1] = points83[35][1] * 2 - points83[6][1];
        points90[87][0] = (int) (2.4 * points83[64][0] - 1.4 * points83[9][0]);
        points90[87][1] = (int) (2.4 * points83[64][1] - 1.4 * points83[9][1]);
        points90[88][0] = points83[45][0] * 2 - points83[12][0];
        points90[88][1] = points83[45][1] * 2 - points83[12][1];
        points90[89][0] = points90[83][0] * 2 - points83[59][0];
        points90[89][1] = points90[83][1] * 2 - points83[59][1];
        return points90;
    }

    public interface Condition<T> {
        boolean is(T t);
    }

    public static <T> T find(Collection<T> list, Condition<T> action) {
        if (list == null || list.isEmpty() || action == null) {
            return null;
        }
        for (T t : list) {
            if (action.is(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> boolean contains(Collection<T> list, Condition<T> action) {
        return find(list, action) != null;
    }

    public static <K, V> Map.Entry<K, V> find(Map<K, V> map, Condition<Map.Entry<K, V>> action) {
        if (map == null || map.isEmpty() || action == null) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (action.is(entry)) {
                return entry;
            }
        }
        return null;
    }

    public static <K, V> boolean contains(Map<K, V> map, Condition<Map.Entry<K, V>> action) {
        return find(map, action) != null;
    }

    public static <T> int indexOf(List<T> list, Condition<T> action) {
        if (list == null || list.isEmpty() || action == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (action.is(t)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> T remove(List<T> list, Condition<T> action) {
        if (list == null || list.isEmpty() || action == null) {
            return null;
        }
        int index = indexOf(list, action);
        if (index >= 0) {
            return list.remove(index);
        }
        return null;
    }

    public static <T> T get(List<T> list, int index) {
        if (list == null
                || index < 0
                || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    public static List<Float> arrayToList(float[] points) {
        List<Float> list = new ArrayList<>();
        if (isEmpty(points)) {
            return list;
        }
        for (float point : points) {
            list.add(point);
        }
        return list;
    }
}
