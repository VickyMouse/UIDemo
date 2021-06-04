package demo.li.opal.uidemo.views.icon

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import demo.li.opal.uidemo.R

/**
 * icon textview组件，设置clickable=true，才能监听到state_pressed状态变化
 *
 * @author chenbiao
 * @since 08/26/2020
 */
open class IconTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {
    /** 多个icon的颜色状态列表 */
    private var startColorStateList: ColorStateList? = null
    private var topColorStateList: ColorStateList? = null
    private var endColorStateList: ColorStateList? = null
    private var bottomColorStateList: ColorStateList? = null
    /** 多个icon的尺寸 */
    private var startIconSize: Int = 0
    private var topIconSize: Int = 0
    private var endIconSize: Int = 0
    private var bottomIconSize: Int = 0

    init {
        attrs?.apply {
            val typeArray = context.obtainStyledAttributes(this, R.styleable.IconTextView)
            startColorStateList = typeArray.getColorStateList(R.styleable.IconTextView_icon_start_color)
            topColorStateList = typeArray.getColorStateList(R.styleable.IconTextView_icon_top_color)
            endColorStateList = typeArray.getColorStateList(R.styleable.IconTextView_icon_end_color)
            bottomColorStateList =
                typeArray.getColorStateList(R.styleable.IconTextView_icon_bottom_color)
            startIconSize = typeArray.getDimensionPixelSize(R.styleable.IconTextView_icon_start_size, 0)
            topIconSize = typeArray.getDimensionPixelSize(R.styleable.IconTextView_icon_top_size, 0)
            endIconSize = typeArray.getDimensionPixelSize(R.styleable.IconTextView_icon_end_size, 0)
            bottomIconSize =
                typeArray.getDimensionPixelSize(R.styleable.IconTextView_icon_bottom_size, 0)
            typeArray.recycle()
            setCompoundDrawablesBounds()
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        // 监听点击状态，设置不同的样式
        applyState()
    }

    /**
     * 设置left、top、right、bottom的drawable，改变drawable的尺寸为相对应icon的尺寸
     */
    private fun setCompoundDrawablesBounds() {
        // left、top、right、bottom
        val compoundDrawables = compoundDrawables
        // start、top、end、bottom
        val compoundDrawablesRelative = compoundDrawablesRelative
        // if drawableStart is null，will apply drawableLeft
        var drawableStart: Drawable? = compoundDrawablesRelative[0]?.mutate() ?: compoundDrawables[0]
        drawableStart?.takeIf { startIconSize > 0 }?.apply {
            setDrawableSize(this, startIconSize)
        }
        var drawableTop: Drawable? = compoundDrawablesRelative[1]?.mutate()
        drawableTop?.takeIf { topIconSize > 0 }?.apply {
            setDrawableSize(this, topIconSize)
        }
        // if drawableEnd is null，will apply drawableRight
        var drawableEnd: Drawable? = compoundDrawablesRelative[2]?.mutate() ?: compoundDrawables[2]
        drawableEnd?.takeIf { endIconSize > 0 }?.apply {
            setDrawableSize(this, endIconSize)
        }
        var drawableBottom: Drawable? = compoundDrawablesRelative[3]?.mutate()
        drawableBottom?.takeIf { bottomIconSize > 0 }?.apply {
            setDrawableSize(this, bottomIconSize)
        }
        setCompoundDrawablesRelative(
            drawableStart,
            drawableTop,
            drawableEnd,
            drawableBottom
        )
        applyState()
    }

    /**
     * 给不同图标配色
     */
    private fun applyState() {
        startColorStateList?.apply {
            val color = getColorForState(drawableState, Color.BLACK)
            compoundDrawablesRelative[0]?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
        topColorStateList?.apply {
            val color = getColorForState(drawableState, Color.BLACK)
            compoundDrawablesRelative[1]?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
        endColorStateList?.apply {
            val color = getColorForState(drawableState, Color.BLACK)
            compoundDrawablesRelative[2]?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
        bottomColorStateList?.apply {
            val color = getColorForState(drawableState, Color.BLACK)
            compoundDrawablesRelative[3]?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    /**
     * 设置start icon颜色
     */
    fun setStartIconColor(@ColorRes res: Int = 0, @ColorInt color: Int = 0) {
        if (res != 0) {
            startColorStateList = resources.getColorStateList(res, context.theme)
        } else if (color != 0) {
            startColorStateList = ColorStateList.valueOf(color)
        }
        applyState()
    }

    /**
     * 设置top icon颜色
     */
    fun setTopIconColor(@ColorRes res: Int = 0, @ColorInt color: Int = 0) {
        if (res != 0) {
            topColorStateList = resources.getColorStateList(res, context.theme)
        } else if (color != 0) {
            topColorStateList = ColorStateList.valueOf(color)
        }
        applyState()
    }

    /**
     * 设置end icon颜色
     */
    fun setEndIconColor(@ColorRes res: Int = 0, @ColorInt color: Int = 0) {
        if (res != 0) {
            endColorStateList = resources.getColorStateList(res, context.theme)
        } else if (color != 0) {
            endColorStateList = ColorStateList.valueOf(color)
        }
        applyState()
    }

    /**
     * 设置bottom icon颜色
     */
    fun setBottomIconColor(@ColorRes res: Int = 0, @ColorInt color: Int = 0) {
        if (res != 0) {
            bottomColorStateList = resources.getColorStateList(res, context.theme)
        } else if (color != 0) {
            bottomColorStateList = ColorStateList.valueOf(color)
        }
        applyState()
    }
}
