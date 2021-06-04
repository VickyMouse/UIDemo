package demo.li.opal.uidemo.views.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.Switch
import demo.li.opal.uidemo.R

/**
 * switch button，目前先不支持设置属性，直接饮用样式 {@style SwitchButton}
 *
 * @author chenbiao
 * @since 08/31/2020
 */
class SwitchButton: Switch {
    /** unable 蒙层 */
    private var mask: UnableMask = UnableMask()
    /** 圆角半径 */
    private var radius = 0

    @JvmOverloads
    constructor(context: Context) : super(context)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setTrackResource(R.drawable.switch_button_track)
        setThumbResource(R.drawable.switch_thumb)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (height > 0) {
            radius = height / 2;
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isEnabled) {
            mask.drawMask(canvas, RectF(background.bounds), radius.toFloat())
        }
    }
}