package demo.li.opal.uidemo.views.icon

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import demo.li.opal.uidemo.R
import kotlin.math.min
import kotlin.math.round

/**
 * icon image组件，设置clickable=true，才能监听到state_pressed状态变化
 *
 * @author chenbiao
 * @since 08/25/2020
 */
open class IconImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {
    /** 颜色状态列表 */
    private var colorStateList: ColorStateList? = null
    /** icon的尺寸 */
    private var iconSize: Float = 0f

    init {
        attrs?.apply {
            val typeArray = context.obtainStyledAttributes(this, R.styleable.IconImageView)
            colorStateList = typeArray.getColorStateList(R.styleable.IconImageView_icon_color)
            iconSize = typeArray.getDimension(R.styleable.IconImageView_icon_size, 0f)
            typeArray.recycle()
            applyState()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initMatrix()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        // 监听点击状态，设置不同的样式
        applyState()
    }

    /**
     * 设置icon颜色
     *
     * @param res color resource
     * @param color color int
     */
    fun setIconColor(@ColorRes res: Int = 0, @ColorInt color: Int = 0) {
        if (res != 0) {
            colorStateList = resources.getColorStateList(res, context.theme)
        } else if (color != 0) {
            colorStateList = ColorStateList.valueOf(color)
        }
        applyState()
    }

    /**
     * 设置icon大小
     *
     * @param size
     */
    fun setIconSize(size: Float) {
        if (iconSize != size) {
            iconSize = size
            initMatrix()
        }
    }

    /**
     * 设置imageMatrix，以支持iconSize属性
     */
    private fun initMatrix() {
        if (drawable == null || iconSize == 0f || measuredWidth <= 0) {
            return
        }
        val dWidth = drawable.intrinsicWidth
        val dHeight = drawable.intrinsicHeight
        if (dWidth <= 0 || dHeight <= 0) {
            return
        }
        // 设置scaleType为MATRIX，可以自定义样式
        if (scaleType != ScaleType.MATRIX) {
            scaleType = ScaleType.MATRIX
        }
        val rWidth = min(iconSize, measuredWidth.toFloat())
        val rHeight = min(iconSize, measuredHeight.toFloat())
        val scaleX = rWidth / dWidth
        val scaleY = rHeight / dHeight
        val dx = round((measuredWidth - rWidth) * 0.5f)
        val dy = round((measuredHeight - rHeight) * 0.5f)
        val matrix = Matrix()
        matrix.setScale(scaleX, scaleY)
        matrix.postTranslate(dx, dy)
        imageMatrix = matrix
    }

    /**
     * 给图标配色
     */
    private fun applyState() {
        colorStateList?.apply {
            val color = getColorForState(drawableState, Color.BLACK)
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}
