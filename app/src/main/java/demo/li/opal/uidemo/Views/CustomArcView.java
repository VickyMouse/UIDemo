package demo.li.opal.uidemo.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class CustomArcView extends View {

    private Paint mPaint, mPaint2;
    private int[] mGradientColor = new int[]{Color.parseColor("#ffffff"), Color.parseColor("#f90051")};
    /**
     * 圆的宽度
     */
    private int mCircleWidth = 3;

    public CustomArcView(Context context) {
        this(context, null);
    }

    public CustomArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint2 = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 这是一个居中的圆
         */
        float x = (getWidth() - getHeight() / 2) / 2;
        float y = getHeight() / 4;
        RectF oval = new RectF(x - 30, y - 30,
                getWidth() - x + 30, getHeight() - y + 30);

        float startAngle = 0f, sweepAngle = 140f;
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mCircleWidth);
//        mPaint.setColor(Color.CYAN);
        SweepGradient mSweepGradient = new SweepGradient(canvas.getWidth() / 2,
                canvas.getHeight() / 2, //以圆弧中心作为扫描渲染的中心以便实现需要的效果
                mGradientColor, //这是我定义好的颜色数组，包含2个颜色：#35C3D7、#2894DD
                new float[]{startAngle, sweepAngle / 360});
        mPaint.setShader(mSweepGradient);
        canvas.drawArc(oval, startAngle, sweepAngle, true, mPaint);

        float startAngle2 = 820, sweepAngle2 = -200;
        mPaint2.setAntiAlias(true);//取消锯齿
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setStrokeWidth(10);
//        mPaint2.setColor(Color.BLUE);
        SweepGradient mSweepGradient2 = new SweepGradient(canvas.getWidth() / 2,
                canvas.getHeight() / 2, //以圆弧中心作为扫描渲染的中心以便实现需要的效果
                mGradientColor, //这是我定义好的颜色数组，包含2个颜色：#35C3D7、#2894DD
                new float[]{0, Math.abs(sweepAngle2)/360});   // 必须从比较小的值开始（非单调的话会有奇怪的结果），其实就是整个颜色变化的区域（相对于整个圆，如为 null，就是一整圈以后颜色渐变完结）
//                new float[]{(startAngle2 + sweepAngle2) % 360/360, startAngle2 % 360/360});   // 必须从比较小的那个的角度开始
        mPaint2.setShader(mSweepGradient2);
        // 更新第一个颜色的其实位置（默认从 3 点位置开始）
        Matrix matrix = new Matrix();
        matrix.setRotate(startAngle2 + sweepAngle2, canvas.getWidth() / 2, canvas.getHeight() / 2);
        mSweepGradient2.setLocalMatrix(matrix);


        RectF oval2 = new RectF(x, y,
                getWidth() - x, getHeight() - y);
        canvas.drawArc(oval2, startAngle2, sweepAngle2, true, mPaint2);
    }
}