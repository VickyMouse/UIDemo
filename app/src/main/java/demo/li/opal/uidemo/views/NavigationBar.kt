package demo.li.opal.uidemo.views

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.IntDef
import demo.li.opal.uidemo.views.button.CircleButton
import demo.li.opal.uidemo.views.button.CornersButton
import demo.li.opal.uidemo.R
import demo.li.opal.uidemo.Utils.FontUtils

/** 需要显示的view的标识 */
const val VIEW_LEFT_ICON = 0x01
const val VIEW_TITLE = 0x02
const val VIEW_RIGHT_ICON = 0x04
const val VIEW_RIGHT_BUTTON = 0x08

/** 导航栏主题 */
const val THEME_DARK = 1
const val THEME_LIGHT = 2

/**
 * 通用顶部导航栏组件  ~40ms
 *
 * @author chenbiao
 * @since 09/09/2020
 */
class NavigationBar : FrameLayout {
    /** 要显示的元素 */
    private var showViews = VIEW_LEFT_ICON or VIEW_RIGHT_ICON

    /** 主题：黑色or白色 */
    private var theme = THEME_DARK
    private var titleString = ""
    private var rightButtonString = ""
    private var leftIcon: CircleButton? = null
    private var rightIcon: CircleButton? = null
    private var tvTitle: TextView? = null
    private var rightButton: CornersButton? = null
    private var leftIconDrawableResource: Int = R.drawable.icon_back
    private var rightIconDrawableResource: Int = R.drawable.icon_close

    /** 扩大leftIcon点击区域 */
    private var leftIconTouchDelegate: TouchDelegate? = null
    private var rightIconTouchDelegate: TouchDelegate? = null

    @JvmOverloads
    constructor(context: Context) : this(context, null)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.apply {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar)
            showViews = ta.getInt(R.styleable.NavigationBar_nv_showViews, showViews)
            theme = ta.getInt(R.styleable.NavigationBar_nv_theme, theme)
            leftIconDrawableResource = ta.getResourceId(R.styleable.NavigationBar_nv_left_icon, leftIconDrawableResource)
            rightIconDrawableResource = ta.getResourceId(R.styleable.NavigationBar_nv_right_icon, rightIconDrawableResource)
            ta.getString(R.styleable.NavigationBar_nv_title)?.apply {
                titleString = this
            }
            ta.getString(R.styleable.NavigationBar_nv_rightButtonText)?.apply {
                rightButtonString = this
            }
            ta.recycle()
        }
        showOrHideViews()
    }

    /**
     * 扩大两个icon的点击热区
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (leftIcon != null && leftIcon!!.visibility == View.VISIBLE && leftIcon!!.width > 0) {
            if (leftIconTouchDelegate == null) {
                leftIconTouchDelegate = TouchDelegate(extendIconHitRect(leftIcon!!), leftIcon)
            }
            if (leftIconTouchDelegate!!.onTouchEvent(event)) {
                return true
            }
        }
        if (rightIcon != null && rightIcon!!.visibility == View.VISIBLE && rightIcon!!.width > 0) {
            if (rightIconTouchDelegate == null) {
                rightIconTouchDelegate = TouchDelegate(extendIconHitRect(rightIcon!!), rightIcon)
            }
            if (rightIconTouchDelegate!!.onTouchEvent(event)) {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 获取扩展后的icon点击区域
     */
    private fun extendIconHitRect(icon: CircleButton): Rect {
        val hitRect = Rect()
        icon.getHitRect(hitRect)
        val length = resources.getDimensionPixelSize(R.dimen.dp_12)
        hitRect.left -= length
        hitRect.top -= length
        hitRect.right += length
        hitRect.bottom += length
        return hitRect
    }

    /**
     * 显示or隐藏所有的组件
     */
    private fun showOrHideViews() {
        // 如果需要显示title，则新建title，否则隐藏
        if ((showViews and VIEW_TITLE) != 0) {
            showTitle()
        } else {
            tvTitle?.visibility = View.GONE
        }
        if ((showViews and VIEW_LEFT_ICON) != 0) {
            showLeftIcon()
        } else {
            leftIcon?.visibility = View.GONE
        }
        if ((showViews and VIEW_RIGHT_ICON) != 0) {
            showRightIcon()
        } else {
            rightIcon?.visibility = View.GONE
        }
        if ((showViews and VIEW_RIGHT_BUTTON) != 0) {
            showRightButton()
        } else {
            rightButton?.visibility = View.GONE
        }
    }

    private fun showLeftIcon() {
        if (leftIcon != null) {
            leftIcon!!.visibility = View.VISIBLE
            return
        }
        leftIcon = CircleButton(context).apply {
            id = R.id.nav_left_icon
            setImageResource(leftIconDrawableResource)
            initIconAttrs(this)
        }
        val lp = LayoutParams(
            resources.getDimensionPixelSize(R.dimen.dp_36),
            resources.getDimensionPixelSize(R.dimen.dp_36)
        )
        lp.gravity = Gravity.CENTER_VERTICAL
        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_12)
        leftIcon!!.layoutParams = lp
        addView(leftIcon!!)
    }

    private fun showRightIcon() {
        if (rightIcon != null) {
            rightIcon!!.visibility = View.VISIBLE
            return
        }
        rightIcon = CircleButton(context).apply {
            id = R.id.nav_right_icon
            setImageResource(rightIconDrawableResource)
            initIconAttrs(this)
        }
        val lp = LayoutParams(
            resources.getDimensionPixelSize(R.dimen.dp_36),
            resources.getDimensionPixelSize(R.dimen.dp_36)
        )
        lp.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dp_12)
        rightIcon!!.layoutParams = lp
        addView(rightIcon!!)
    }

    /**
     * 初始化 icon 通用属性
     */
    private fun initIconAttrs(icon: CircleButton) {
        icon.setIconColor(color = resources.getColor(R.color.white))
        icon.setIconSize(resources.getDimension(R.dimen.dp_24))
        if (theme == THEME_DARK) {
            icon.setBgColor(Color.parseColor("#4D000820"))
        } else {
            icon.setBgColor(Color.parseColor("#4DFFFFFF"))
        }
    }

    private fun showTitle() {
        if (tvTitle != null) {
            tvTitle!!.visibility = View.VISIBLE
            return
        }
        tvTitle = TextView(context)
        tvTitle!!.id = R.id.nav_title
        tvTitle!!.textSize = 22f
        if (!isInEditMode) {
            tvTitle!!.typeface = FontUtils.getTypeface("Bytedance-FZLanTingHeiS-DB1-GB")
        }
        if (theme == THEME_DARK) {
            tvTitle!!.setTextColor(resources.getColor(R.color.gray_01))
        } else {
            tvTitle!!.setTextColor(resources.getColor(R.color.white))
        }
        tvTitle!!.gravity = Gravity.CENTER
        tvTitle!!.maxLines = 1
        tvTitle!!.ellipsize = TextUtils.TruncateAt.END
        val lp =
            LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.dp_60))
        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_60)
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dp_60)
        tvTitle!!.includeFontPadding = false
        tvTitle!!.layoutParams = lp
        tvTitle!!.text = titleString
        addView(tvTitle!!)
    }

    private fun showRightButton() {
        if (rightButton != null) {
            rightButton!!.visibility = View.VISIBLE
            return
        }
        rightButton = CornersButton(context)
        rightButton!!.id = R.id.nav_right_button
        rightButton!!.textSize = 18f
        if (!isInEditMode) {
            rightButton!!.typeface = FontUtils.getTypeface("Bytedance-FZLanTingHeiS-DB1-GB")
        }
        rightButton!!.setTextColor(resources.getColor(R.color.white))
        if (theme == THEME_DARK) {
            rightButton!!.setBgColor(Color.parseColor("#4D000820"))
        } else {
            rightButton!!.setBgColor(Color.parseColor("#4DFFFFFF"))
        }
        rightButton!!.gravity = Gravity.CENTER
        rightButton!!.maxLines = 1
        rightButton!!.setPadding(resources.getDimensionPixelSize(R.dimen.dp_16), 0, resources.getDimensionPixelSize(R.dimen.dp_16), 0)
        val lp =
            LayoutParams(LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.dp_38))
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dp_12)
        lp.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        rightButton!!.includeFontPadding = false
        rightButton!!.layoutParams = lp
        rightButton!!.text = rightButtonString
        addView(rightButton!!)
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        titleString = title
        if ((showViews and VIEW_TITLE) != 0) {
            if (tvTitle == null) {
                showTitle()
            } else {
                tvTitle!!.text = title
            }
        }
    }

    /**
     * 设置右边按钮的文案
     */
    fun setRightButtonText(text: String) {
        rightButtonString = text
        if ((showViews and VIEW_RIGHT_BUTTON) != 0) {
            if (rightButton == null) {
                showRightButton()
            } else {
                rightButton!!.text = text
            }
        }
    }

    /**
     * 设置右边按钮的图标
     */
    fun setRightIconImage(imageResource: Int) {
        rightIcon?.setImageResource(imageResource)
    }

    /**
     * 设置需要显示的view，没有设置显示的view会被隐藏
     *
     * @param views VIEW_LEFT_ICON | VIEW_TITLE | VIEW_RIGHT_ICON | VIEW_RIGHT_BUTTON
     */
    fun setShowViews(views: Int) {
        this.showViews = views
        showOrHideViews()
    }

    /**
     * 设置主题和要显示的view
     *
     * @param theme THEME_DARK | THEME_LIGHT
     * @param views VIEW_LEFT_ICON | VIEW_TITLE | VIEW_RIGHT_ICON | VIEW_RIGHT_BUTTON
     */
    fun setThemeAndViews(@NavigationBarTheme theme: Int = this.theme, views: Int = showViews) {
        this.theme = theme
        this.showViews = views
        showOrHideViews()
    }

    /**
     * 左icon点击事件
     */
    fun setLeftIconClickListener(l: OnClickListener) {
        leftIcon?.setOnClickListener(l)
    }

    /**
     * 右icon点击事件
     */
    fun setRightIconClickListener(l: OnClickListener) {
        rightIcon?.setOnClickListener(l)
    }

    /**
     * title点击事件
     */
    fun setTitleClickListener(l: OnClickListener) {
        tvTitle?.setOnClickListener(l)
    }

    /**
     * 右边按钮点击事件
     */
    fun setRightButtonClickListener(l: OnClickListener) {
        rightButton?.setOnClickListener(l)
    }
}

@IntDef(THEME_DARK, THEME_LIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationBarTheme