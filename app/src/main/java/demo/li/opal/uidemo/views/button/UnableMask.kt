package demo.li.opal.uidemo.views.button

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * 统一绘制不可用状态下的蒙层
 *
 */
internal class UnableMask {
    /** 绘制不可用状态下的蒙层 */
    private var maskPaint: Paint? = null

    /**
     * 绘制蒙层
     */
    fun drawMask(canvas: Canvas?, bounds: RectF, corner: Float) {
        if (maskPaint == null) {
            maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            maskPaint!!.color = Color.parseColor("#66ffffff")
        }
        canvas?.drawRoundRect(
            bounds,
            corner,
            corner,
            maskPaint!!
        )
    }
}