package demo.li.opal.uidemo.views.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.Checkable
import androidx.annotation.ColorInt
import demo.li.opal.uidemo.R
import demo.li.opal.uidemo.views.BackgroundDrawable
import demo.li.opal.uidemo.views.icon.IconTextView

private val CHECKED_STATE_SET: IntArray = intArrayOf(
    android.R.attr.state_checked
)
/**
 * 圆角矩形button，设置背景色、文字和icon
 * 不设置圆角大小，则圆角默认高度一半
 *
 * @author chenbiao
 * @since 08/27/2020
 */
class CornersButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : IconTextView(context, attrs, defStyle), Checkable {
    /** 是否已经被初始化完 */
    private var isBgAdded = false
    /** unable 蒙层 */
    private var mask: UnableMask = UnableMask()
    /** 用于独立控制蒙层是否显示*/
    private var mMaskEnable: Boolean = false

    /** 选中状态 */
    private var mChecked: Boolean = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        attrs?.apply {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CornersButton)
            isChecked = ta.getBoolean(R.styleable.CornersButton_android_checked, mChecked)
            ta.recycle()
        }
        background = BackgroundDrawable(context, attrs, false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isBgAdded && background is BackgroundDrawable && height > 0) {
            isBgAdded = true
            val bg = background as BackgroundDrawable
            // 主动设置了圆角大小的情况下不重新设置圆角，没有设置则默认设为高度的一半，即：半圆圆角
            if (bg.getCornerRadiusTL() > 0 || bg.getCornerRadiusTR() > 0 || bg.getCornerRadiusBL() > 0 || bg.getCornerRadiusBR() > 0) {
                bg.invalidate()
            } else {
                bg.setCornerRadius((height / 2).toFloat()).invalidate()
            }
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        isChecked = !isChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            refreshDrawableState()
            mOnCheckedChangeListener?.onCheckedChanged(this, isChecked)
        }
    }

    /**
     * 设置选中态变化监听
     */
    fun setOnCheckedChangeListener(l: OnCheckedChangeListener?) {
        mOnCheckedChangeListener = l
    }

    /**
     * 设置背景色值
     */
    fun setBgColor(@ColorInt color: Int) {
        if (background is BackgroundDrawable) {
            (background as BackgroundDrawable).setBgColor(color).invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if ((!isEnabled || mMaskEnable) && background is BackgroundDrawable) {
            mask.drawMask(canvas, RectF(background.bounds), (background as BackgroundDrawable).getCornerRadius())
        }
    }

    /**
     * 按钮样式设置为不可用，但仍可响应点击事件
     * example: 点击不可用按钮时触发埋点、Toast提示等
     */
    fun setMaskEnable(enable: Boolean) {
        mMaskEnable = enable
        invalidate()
    }
}
/**
 * 选中态变化监听
 */
interface OnCheckedChangeListener {
    fun onCheckedChanged(button: CornersButton, isChecked: Boolean)
}