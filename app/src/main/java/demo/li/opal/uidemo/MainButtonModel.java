package demo.li.opal.uidemo;
/**
 * Version 1.0
 * <p/>
 * Date: 2014-04-24 11:56
 * Author: yonnielu
 * <p/>
 * Copyright © 1998-2014 Tencent Technology (Shenzhen) Company Ltd.
 */

import android.os.Build;

/**
 * 首页的每一个按钮
 */
public class MainButtonModel {
    private static final String TAG = MainButtonModel.class.getSimpleName();

    public int id;
    public int priority; // 每个button都有自己的优先级，数值越大的更靠前
    public Object image;
    public String label;
    public boolean isRecommend;
    public String recommendId;
    public String mButtonId;
    public String mActionUrl;

    public MainButtonModel(int id, int priority, Object image, String label, String buttonId) {
        this.id = id;
        this.image = image;
        this.priority = priority;
        this.label = label;
        this.mButtonId = buttonId;
    }

    /**
     * 4.2.1 以下的机器不支持透明 webp 动图，使用默认图片
     *
     * @param icUrl
     * @return
     */
    private boolean isOpIconSupported(String icUrl) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 && icUrl.endsWith(".webp")) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MainButtonModel{" +
                "id=" + id +
                ", priority=" + priority +
                ", image=" + image +
                ", label='" + label + '\'' +
                ", isRecommend=" + isRecommend +
                ", recommendId='" + recommendId + '\'' +
                ", mButtonId='" + mButtonId + '\'' +
                ", mActionUrl='" + mActionUrl + '\'' +
                '}';
    }
}
