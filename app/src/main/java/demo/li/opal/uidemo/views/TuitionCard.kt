package demo.li.opal.uidemo.views

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import demo.li.opal.uidemo.R
import demo.li.opal.uidemo.Utils.FontUtils
import demo.li.opal.uidemo.views.button.CornersButton


/**
 * 首页家教服务卡片
 *
 * @author opalli
 * @since 06/03/2021
 */
class TuitionCard : RelativeLayout {

    private var state = STATE_NO_CLASS

    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvTime: TextView
    private lateinit var btEntrance: CornersButton
    private var classNotStartedBg: Int = R.drawable.bg_class_not_started
    private var classStartedBg: Int = R.drawable.bg_class_started

    /** 扩大按钮点击区域 */
    private var btEntranceTouchDelegate: TouchDelegate? = null

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
        initView()
        attrs?.apply {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.TuitionCard)
            state = ta.getInt(R.styleable.TuitionCard_tc_state, state)
            ta.getString(R.styleable.TuitionCard_tc_title)?.apply {
                tvTitle.text = this
            }
            ta.getString(R.styleable.TuitionCard_tc_desc)?.apply {
                tvDesc.text = this
            }
            ta.getString(R.styleable.TuitionCard_tc_timetable)?.apply {
                tvTime.text = this
            }
            ta.getString(R.styleable.TuitionCard_tc_btn_text)?.apply {
                btEntrance.text = this
            }
            ta.recycle()
        }
    }


    private fun initView() {
        inflate(context, R.layout.tuition_card_layout, this)
        tvTitle = findViewById(R.id.tuition_title)
        tvDesc = findViewById(R.id.tuition_desc)
        tvTime = findViewById(R.id.tuition_time)
        btEntrance = findViewById(R.id.tuition_btn)
    }

    /**
     * 扩大 icon 的点击热区
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (btEntrance != null && btEntrance!!.visibility == View.VISIBLE && btEntrance!!.width > 0) {
            if (btEntranceTouchDelegate == null) {
                btEntranceTouchDelegate = TouchDelegate(extendIconHitRect(btEntrance!!), btEntrance)
            }
            if (btEntranceTouchDelegate!!.onTouchEvent(event)) {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 获取扩展后的 icon 点击区域
     */
    private fun extendIconHitRect(icon: CornersButton): Rect {
        val hitRect = Rect()
        icon.getHitRect(hitRect)
        val length = resources.getDimensionPixelSize(R.dimen.dp_32)
        hitRect.left -= length
        hitRect.top -= length
        hitRect.right += length
        hitRect.bottom += length
        return hitRect
    }

    /**
     * （依次）切换卡片状态
     *
     * state STATE_NO_CLASS, STATE_NOT_STARTED, STATE_STARTED
     */
    fun switchCardState() {
        setCardStateAndUpdateViews(if (state == STATE_STARTED) STATE_NO_CLASS else state + 1)
    }

    /**
     * 设置卡片状态，并显示/隐藏所有的组件 View
     *
     * @param state STATE_NO_CLASS, STATE_NOT_STARTED, STATE_STARTED
     */
    fun setCardStateAndUpdateViews(@TuitionCardStates state: Int = this.state) {
        this.state = state
        updateViewsByState()
    }


    /**
     * 根据卡片状态更新所有的组件 View
     */
    private fun updateViewsByState() {
        when (state) {
            STATE_NO_CLASS ->
                visibility = View.GONE
            STATE_NOT_STARTED -> {
                updateCard(false)
                updateTitle(false)
                updateTime(false)
            }
            STATE_STARTED -> {
                updateCard(true)
                updateTitle(true)
                updateTime(true)
            }
        }
    }

    private fun updateCard(started: Boolean) {
        val lp = layoutParams as LayoutParams
        if (started) {
            lp.height = resources.getDimensionPixelSize(R.dimen.dp_181)
            setBackgroundResource(classStartedBg)
            visibility = View.VISIBLE
            tvDesc.visibility = View.VISIBLE
            btEntrance.visibility = View.VISIBLE
        } else {
            lp.height = resources.getDimensionPixelSize(R.dimen.dp_80)
            setBackgroundResource(classNotStartedBg)
            visibility = View.VISIBLE
            tvDesc.visibility = View.GONE
            btEntrance.visibility = View.GONE
        }
    }

    private fun updateTitle(started: Boolean) {
        val lp = tvTitle.layoutParams as LayoutParams
        if (started) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
            tvTitle.textSize = 22f
            tvTitle.setTypeface(FontUtils.getTypeface("Bytedance-FZLanTingHei-H-GBK"), Typeface.NORMAL)
            lp.topMargin = resources.getDimensionPixelSize(R.dimen.dp_36)
            lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_20)
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_02))
            tvTitle.textSize = 18f
            tvTitle.setTypeface(FontUtils.getTypeface("Bytedance-FZLanTingHei-H-GBK"), Typeface.NORMAL)
            lp.topMargin = resources.getDimensionPixelSize(R.dimen.dp_27)
            lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_72)
        }
    }

    private fun updateTime(started: Boolean) {
        val lp = tvTime.layoutParams as LayoutParams
        if (started) {
            tvTime.setTextColor(ContextCompat.getColor(context, R.color.white))
            tvTime.setTypeface(FontUtils.getTypeface("Bytedance-FZLanTingHeiS-DB1-GB"), Typeface.NORMAL)
            lp.topMargin = resources.getDimensionPixelSize(R.dimen.dp_125)
            lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_20)
        } else {
            tvTime.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
            tvTime.setTypeface(FontUtils.getTypeface("Bytedance-FZLanTingHei-R-GBK"), Typeface.NORMAL)
            lp.topMargin = resources.getDimensionPixelSize(R.dimen.dp_31)
            lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dp_173)
        }
    }

    /**
     * 设置时间
     */
    fun setTimeTable(duration: String) {
        tvTime.text = duration
    }

    /**
     * 按钮点击事件
     */
    fun setButtonClickListener(l: OnClickListener) {
        btEntrance?.setOnClickListener(l)
    }
}

/** 家教服务状态：
 * 0：未开通家教服务
 * 1：开通家教服务，但不在家教服务时间段
 * 2：开通家教服务，并在家教服务时间段
 */
@IntDef(STATE_NO_CLASS, STATE_NOT_STARTED, STATE_STARTED)
@Retention(AnnotationRetention.SOURCE)
annotation class TuitionCardStates

const val STATE_NO_CLASS = 0
const val STATE_NOT_STARTED = 1
const val STATE_STARTED = 2