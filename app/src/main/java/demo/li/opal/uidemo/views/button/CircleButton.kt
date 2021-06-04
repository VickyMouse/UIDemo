package demo.li.opal.uidemo.views.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import demo.li.opal.uidemo.R
import demo.li.opal.uidemo.views.BackgroundDrawable
import demo.li.opal.uidemo.views.icon.IconImageView


/**
 * 圆形button，支持icon size、设置背景色
 *
 * @author chenbiao
 * @since 08/27/2020
 */
class CircleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : IconImageView(context, attrs, defStyle) {
    /** 是否已经被初始化完 */
    private var isBgAdded = false

    /** unable 蒙层 */
    private var mask: UnableMask = UnableMask()

    init {
        attrs?.apply {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleButton)
            isEnabled = ta.getBoolean(R.styleable.CircleButton_android_enabled, true)
            ta.recycle()
        }
        background = BackgroundDrawable(context, attrs, false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isBgAdded && background is BackgroundDrawable && height > 0) {
            (background as BackgroundDrawable).setCornerRadius((height / 2).toFloat()).invalidate()
        }
    }

    /**
     * 设置背景色值
     */
    fun setBgColor(@ColorInt color: Int) {
        if (background is BackgroundDrawable) {
            (background as BackgroundDrawable).setBgColor(color).invalidate()
        }
    }

    /**
     * 设置背景色值
     */
    fun setBgColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        if (background is BackgroundDrawable) {
            (background as BackgroundDrawable).setBgColors(startColor, endColor).invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isEnabled && background is BackgroundDrawable) {
            mask.drawMask(
                canvas,
                RectF(background.bounds),
                (background as BackgroundDrawable).getCornerRadius()
            )
        }
    }
}