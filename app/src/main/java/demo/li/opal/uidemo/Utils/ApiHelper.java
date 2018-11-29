/*
 * Copyright (C) 2012 The Android Open Source Project
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

package demo.li.opal.uidemo.Utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.hardware.Camera;
import android.os.Build;

import java.lang.reflect.Field;

public class ApiHelper {
    public static class VERSION_CODES {
        // These value are copied from Build.VERSION_CODES
        public static final int BASE = 1;
        public static final int BASE_1_1 = 2;
        public static final int CUPCAKE = 3;
        public static final int DONUT = 4;
        public static final int ECLAIR = 5;
        public static final int ECLAIR_0_1 = 6;
        public static final int ECLAIR_MR1 = 7;
        public static final int FROYO = 8;
        public static final int GINGERBREAD = 9;
        // February 2011: Android 2.3.3.
        public static final int GINGERBREAD_MR1 = 10;
        public static final int HONEYCOMB = 11;
        public static final int HONEYCOMB_MR1 = 12;
        public static final int HONEYCOMB_MR2 = 13;
        public static final int ICE_CREAM_SANDWICH = 14;
        // 天天P图从15开始支持，@kesenhu @2016/04/18
        // December 2011: Android 4.0.3.
        public static final int ICE_CREAM_SANDWICH_MR1 = 15;
        // June 2012: Android 4.1.
        public static final int JELLY_BEAN = 16;
        // November 2012: Android 4.2, Moar jelly beans!
        public static final int JELLY_BEAN_MR1 = 17;
        // July 2013: Android 4.3, the revenge of the beans.
        public static final int JELLY_BEAN_MR2 = 18;
        // October 2013: Android 4.4, KitKat, another tasty treat.
        public static final int KITKAT = 19;
        // Android 4.4W: KitKat for watches, snacks on the run.
        public static final int KITKAT_WATCH = 20;
        // Lollipop.  A flat one with beautiful shadows.  But still tasty.
        public static final int LOLLIPOP = 21;
        // Lollipop with an extra sugar coating on the outside!
        public static final int LOLLIPOP_MR1 = 22;
        //
        public static final int NOUGAT = 24;
        public static final int O = 26;
        public static final int O_MR1 = 27;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isExactlyKitkat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.NOUGAT;
    }

    public static boolean isAndroid_8_1() {
        return Build.VERSION.SDK_INT == VERSION_CODES.O_MR1;
    }

    public static final boolean HAS_MEDIA_COLUMNS_WIDTH_AND_HEIGHT = Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;

    //public static final boolean HAS_SURFACE_TEXTURE = Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;

    public static final boolean HAS_AUTO_FOCUS_MOVE_CALLBACK = Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;

    public static final boolean HAS_CAMERA_FOCUS_AREA = Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;

    public static final boolean HAS_CAMERA_METERING_AREA = Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;

    public static final boolean HAS_CAMERA_HDR = Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1;

    public static final boolean HAS_CAMERA_HDR_PLUS = Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;

    public static final boolean HAS_DISPLAY_LISTENER = Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1;

    public static final boolean CAN_START_PREVIEW_IN_JPEG_CALLBACK = Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;

    public static final boolean HAS_FACE_DETECTION;
    static {
        boolean hasFaceDetection = false;
        try {
            Class<?> listenerClass = Class.forName(
                    "android.hardware.Camera$FaceDetectionListener");
            hasFaceDetection =
                    hasMethod(Camera.class, "setFaceDetectionListener", listenerClass) &&
                            hasMethod(Camera.class, "startFaceDetection") &&
                            hasMethod(Camera.class, "stopFaceDetection") &&
                            hasMethod(Camera.Parameters.class, "getMaxNumDetectedFaces");
        } catch (Throwable t) {

        }
        HAS_FACE_DETECTION = hasFaceDetection;
    }

    public static final boolean HAS_GET_CAMERA_DISABLED =
            Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH ?
                    hasMethod(DevicePolicyManager.class, "getCameraDisabled", ComponentName.class) : false;

    public static int getIntFieldIfExists(Class<?> klass, String fieldName, Class<?> obj, int defaultVal) {
        try {
            Field f = klass.getDeclaredField(fieldName);
            return f.getInt(obj);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static boolean hasField(Class<?> klass, String fieldName) {
        try {
            klass.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private static boolean hasMethod(String className, String methodName, Class<?>... parameterTypes) {
        try {
            Class<?> klass = Class.forName(className);
            klass.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean hasMethod(Class<?> klass, String methodName, Class<?>... paramTypes) {
        try {
            klass.getDeclaredMethod(methodName, paramTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
