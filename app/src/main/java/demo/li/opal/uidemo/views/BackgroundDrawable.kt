package demo.li.opal.uidemo.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import demo.li.opal.uidemo.R

/**
 * 通用的用于绘制背景的drawable，支持圆角、描边、渐变、press状态、选中态
 *
 * @author chenbiao
 * @since 08/26/2020
 */
class BackgroundDrawable : StateListDrawable {
    /** background和backgroundPressed分别用于常态和按压态背景 */
    private val background = GradientDrawable()
    private val backgroundPressed = GradientDrawable()
    /** 选中态背景 */
    private val backgroundChecked = GradientDrawable()
    /** 背景色 */
    @ColorInt
    private var bgColor: Int = 0
    /** 背景起始色-》结束色 */
    @ColorInt
    private var bgStartColor: Int = 0
    @ColorInt
    private var bgEndColor: Int = 0
    @ColorInt
    private var bgPressColor: Int = 0
    /** 按压态下的背景起始色-》结束色 */
    @ColorInt
    private var bgPressStartColor: Int = 0
    @ColorInt
    private var bgPressEndColor: Int = 0
    @ColorInt
    private var bgCheckedColor: Int = 0
    /** 选中态下的背景起始色-》结束色 */
    @ColorInt
    private var bgCheckedStartColor: Int = 0
    @ColorInt
    private var bgCheckedEndColor: Int = 0
    /** 边框色 */
    @ColorInt
    private var strokeColor: Int = 0
    @ColorInt
    private var strokePressColor: Int = 0
    @ColorInt
    private var strokeCheckedColor: Int = 0
    /** 渐变色方向 1or2 */
    private var colorOrientation: Int = 2
    /** 圆角大小 */
    private var cornerRadius: Float = 0f
    /** 以下分别是左上、右上、左下、右下圆角大小，在cornerRadius>0时不生效 */
    private var cornerRadiusTL: Float = cornerRadius
    private var cornerRadiusTR: Float = cornerRadius
    private var cornerRadiusBL: Float = cornerRadius
    private var cornerRadiusBR: Float = cornerRadius
    /** 边框宽度 */
    private var strokeWidth: Int = 0
    /** background 是否已经被添加到list */
    private var isBackgroundAdded = false
    /** pressed background 是否已经被添加到list */
    private var isPressedBackgroundAdded = false
    /** checked background 是否已经被添加到list */
    private var isCheckedBackgroundAdded = false

    constructor() {
    }

    constructor(context: Context, attrs: AttributeSet?, immediately: Boolean = true) {
        initAttributes(context, attrs, immediately)
    }

    /**
     * 初始化属性值
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?, immediately: Boolean) {
        attrs?.apply {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.BackgroundDrawable)
            // color
            bgColor = ta.getColor(R.styleable.BackgroundDrawable_bg_color, bgColor)
            bgStartColor = ta.getColor(R.styleable.BackgroundDrawable_bg_startColor, bgColor)
            bgEndColor = ta.getColor(R.styleable.BackgroundDrawable_bg_endColor, bgColor)
            bgPressColor = ta.getColor(R.styleable.BackgroundDrawable_bg_pressColor, bgPressColor)
            bgPressStartColor = ta.getColor(R.styleable.BackgroundDrawable_bg_pressStartColor, bgPressColor)
            bgPressEndColor = ta.getColor(R.styleable.BackgroundDrawable_bg_pressEndColor, bgPressColor)
            bgCheckedColor = ta.getColor(R.styleable.BackgroundDrawable_bg_checkedColor, bgCheckedColor)
            bgCheckedStartColor = ta.getColor(R.styleable.BackgroundDrawable_bg_checkedStartColor, bgCheckedColor)
            bgCheckedEndColor = ta.getColor(R.styleable.BackgroundDrawable_bg_checkedEndColor, bgCheckedColor)

            strokeColor = ta.getColor(R.styleable.BackgroundDrawable_bg_strokeColor, strokeColor)
            strokePressColor = ta.getColor(R.styleable.BackgroundDrawable_bg_strokePressColor, strokePressColor)
            strokeCheckedColor = ta.getColor(R.styleable.BackgroundDrawable_bg_strokeCheckedColor, strokeCheckedColor)

            colorOrientation = ta.getInt(R.styleable.BackgroundDrawable_bg_colorOrientation, colorOrientation)
            // size
            strokeWidth = ta.getDimensionPixelSize(R.styleable.BackgroundDrawable_bg_strokeWidth, 0)
            cornerRadius = ta.getDimension(R.styleable.BackgroundDrawable_bg_cornerRadius, cornerRadius)
            cornerRadiusTL = ta.getDimension(R.styleable.BackgroundDrawable_bg_cornerRadius_TL, cornerRadius)
            cornerRadiusTR = ta.getDimension(R.styleable.BackgroundDrawable_bg_cornerRadius_TR, cornerRadius)
            cornerRadiusBL = ta.getDimension(R.styleable.BackgroundDrawable_bg_cornerRadius_BL, cornerRadius)
            cornerRadiusBR = ta.getDimension(R.styleable.BackgroundDrawable_bg_cornerRadius_BR, cornerRadius)

            ta.recycle()
            if (immediately) {
                makeDrawable()
            }
        }
    }

    /**
     * 构造drawable
     */
    private fun makeDrawable() {
        makeBackgroundDrawable()
        makePressedBackgroundDrawable()
        makeCheckedBackgroundDrawable()
    }

    /**
     * 构造常态下的drawable
     */
    private fun makeBackgroundDrawable() {
        if (isBgColorValid()) {
            background.colors = intArrayOf(bgStartColor, bgEndColor)
            background.orientation = getOrientationEnum()
            if (hasRadius()) {
                // 判断是否所有圆角值相等，相等则使用cornerRadius api设置圆角大小，否则需要使用cornerRadii api设置圆角大小
                if (allRadiusIsEqual()) {
                    background.cornerRadius = cornerRadiusTL
                } else {
                    background.cornerRadii = floatArrayOf(
                        cornerRadiusTL,
                        cornerRadiusTL,
                        cornerRadiusTR,
                        cornerRadiusTR,
                        cornerRadiusBR,
                        cornerRadiusBR,
                        cornerRadiusBL,
                        cornerRadiusBL
                    )
                }
            }
            if (strokeWidth > 0 && strokeColor != 0) {
                background.setStroke(strokeWidth, strokeColor)
            }
            if (!isBackgroundAdded) {
                isBackgroundAdded = true
                addState(intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_checked), background)
            }
        }
    }

    /**
     * 构造按压态下的drawable
     */
    private fun makePressedBackgroundDrawable() {
        if (!isBgColorValid()) {
            return
        }
        // 按压态色值不为空再初始化
        if ((bgPressStartColor != 0 && bgPressEndColor != 0) || strokePressColor != 0) {
            backgroundPressed.colors =
                intArrayOf(
                    if (bgPressStartColor == 0) bgStartColor else bgPressStartColor,
                    if (bgPressEndColor == 0) bgEndColor else bgPressEndColor
                )
            backgroundPressed.orientation = getOrientationEnum()
            if (hasRadius()) {
                if (allRadiusIsEqual()) {
                    backgroundPressed.cornerRadius = cornerRadiusTL
                } else {
                    backgroundPressed.cornerRadii = floatArrayOf(
                        cornerRadiusTL,
                        cornerRadiusTL,
                        cornerRadiusTR,
                        cornerRadiusTR,
                        cornerRadiusBR,
                        cornerRadiusBR,
                        cornerRadiusBL,
                        cornerRadiusBL
                    )
                }
            }
            val strokeColor = if (strokePressColor == 0) strokeColor else strokePressColor
            if (strokeWidth > 0 && strokeColor != 0) {
                backgroundPressed.setStroke(strokeWidth, strokeColor)
            }
            if (!isPressedBackgroundAdded) {
                isPressedBackgroundAdded = true
                addState(intArrayOf(android.R.attr.state_pressed), backgroundPressed)
            }
        }
    }

    /**
     * 构造选中态下的drawable
     */
    private fun makeCheckedBackgroundDrawable() {
        if (!isBgColorValid()) {
            return
        }
        // 选中态色值不为空再初始化
        if ((bgCheckedStartColor != 0 && bgCheckedEndColor != 0) || strokeCheckedColor != 0) {
            backgroundChecked.colors =
                intArrayOf(
                    if (bgCheckedStartColor == 0) bgStartColor else bgCheckedStartColor,
                    if (bgCheckedEndColor == 0) bgEndColor else bgCheckedEndColor
                )
            backgroundChecked.orientation = getOrientationEnum()
            if (hasRadius()) {
                if (allRadiusIsEqual()) {
                    backgroundChecked.cornerRadius = cornerRadiusTL
                } else {
                    backgroundChecked.cornerRadii = floatArrayOf(
                        cornerRadiusTL,
                        cornerRadiusTL,
                        cornerRadiusTR,
                        cornerRadiusTR,
                        cornerRadiusBR,
                        cornerRadiusBR,
                        cornerRadiusBL,
                        cornerRadiusBL
                    )
                }
            }
            val strokeColor = if (strokeCheckedColor == 0) strokeColor else strokeCheckedColor
            if (strokeWidth > 0 && strokeColor != 0) {
                backgroundChecked.setStroke(strokeWidth, strokeColor)
            }
            if (!isCheckedBackgroundAdded) {
                isCheckedBackgroundAdded = true
                addState(intArrayOf(android.R.attr.state_checked), backgroundChecked)
            }
        }
    }

    /**
     * 检查bgColor是否有效，即：业务方有没有设置背景色
     */
    private fun isBgColorValid(): Boolean {
        return bgStartColor != 0 && bgEndColor != 0
    }

    /**
     * 是否有圆角
     */
    private fun hasRadius(): Boolean {
        return cornerRadiusTL > 0 || cornerRadiusTR > 0 || cornerRadiusBR > 0 || cornerRadiusBL > 0
    }

    /**
     * 判断是否所有的圆角都相等
     */
    private fun allRadiusIsEqual(): Boolean {
        return cornerRadiusTL == cornerRadiusTR &&
                cornerRadiusTL == cornerRadiusBR &&
                cornerRadiusTL == cornerRadiusBL
    }

    /**
     * 换算渐变方向
     */
    private fun getOrientationEnum(): GradientDrawable.Orientation {
        return when (colorOrientation) {
            1 -> GradientDrawable.Orientation.LEFT_RIGHT
            3 -> GradientDrawable.Orientation.TL_BR
            4 -> GradientDrawable.Orientation.BR_TL
            5 -> GradientDrawable.Orientation.TR_BL
            6 -> GradientDrawable.Orientation.BL_TR
            else -> GradientDrawable.Orientation.TOP_BOTTOM
        }
    }

    /**
     * 设置纯色背景色值，返回当前实例，需要调用invalidate() 方法才能生效
     */
    fun setBgColor(@ColorInt color: Int): BackgroundDrawable {
        bgColor = color
        bgStartColor = color
        bgEndColor = color
        return this
    }

    /**
     * 设置背景色值，返回当前实例，需要调用invalidate() 方法才能生效
     */
    fun setBgColors(@ColorInt startColor: Int , @ColorInt endColor: Int): BackgroundDrawable {
        bgColor = startColor
        bgStartColor = startColor
        bgEndColor = endColor
        return this
    }

    /**
     * 设置圆角大小，需要调用invalidate() 方法才能生效
     */
    fun setCornerRadius(radius: Float): BackgroundDrawable {
        cornerRadius = radius
        cornerRadiusTL = radius
        cornerRadiusTR = radius
        cornerRadiusBL = radius
        cornerRadiusBR = radius
        return this
    }

    /**
     * getter 获取圆角大小
     */
    fun getCornerRadius(): Float {
        return cornerRadius
    }

    fun getCornerRadiusTL(): Float {
        return cornerRadiusTL
    }

    fun getCornerRadiusTR(): Float {
        return cornerRadiusTR
    }

    fun getCornerRadiusBL(): Float {
        return cornerRadiusBL
    }

    fun getCornerRadiusBR(): Float {
        return cornerRadiusBR
    }

    /**
     * 更新drawable，在设置完属性之后，必须调用该方法主动刷新才能生效
     */
    fun invalidate() {
        makeDrawable()
        invalidateSelf()
    }
}
