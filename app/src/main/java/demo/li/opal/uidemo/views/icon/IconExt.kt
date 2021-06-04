package demo.li.opal.uidemo.views.icon

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/**
 * 扩展Drawable方法，返回被着色的Drawable
 *
 * @param color      色值
 */
fun Drawable.coloredDrawable(@ColorInt color: Int): Drawable {
    val d = this.mutate()
    d.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    return d
}

/**
 * 设置drawable的bounds
 *
 * @param drawable source drawable
 * @param size 尺寸
 */
fun setDrawableSize(drawable: Drawable?, size: Int) {
    drawable?.let {
        it.setBounds(0, 0, size, size)
    }
}