package demo.li.opal.uidemo.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import java.util.Collection;

import demo.li.opal.uidemo.GlobalContext;

public class CommonUtils {
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
    public static float getSpSize(float size) {
        Context c = GlobalContext.getContext();
        Resources r;

        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
    }
}
