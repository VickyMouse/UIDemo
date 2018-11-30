package demo.li.opal.uidemo.swipeCard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Queue;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;


/**
 * Created by opalli on 2018/11/29.
 */

// 一叠卡片的 Layout
public class SwipeCardLayout extends RelativeLayout {

    private Paint paint;

    private final static int SWIPE_SLOP = 150;   // 前后两张卡片的比例差
    private final static float SCALE_RATE = 0.9f;   // 前后两张卡片的比例差
    private final static float MAX_ROTATE = 7;   // 前后两张卡片的比例差
    private final static int FRONT_INDEX = 1;   // 最上面的卡片的 index
    private final static int BACK_INDEX = 0;   // 第二张卡片的 index
    int mTranslate;
    private float mDy;
    private int mHeight;
    private int mWidth;
    private CardAdapter adapter;
    private RectF rect;
    private float round;
    private float density;

    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    private OnSwipeListener onSwipeListener;
    private Paint paint_s;

    public SwipeCardLayout(Context context) {
        super(context);
        init();
    }

    public SwipeCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // ViewGroup 默认不会调用 onDraw()，要重写该方法的话，需要 setWillNotDraw(false)
        setWillNotDraw(false);

        setClipToPadding(false);
        density = getResources().getDisplayMetrics().density;

        // （第一张）卡片的宽高
        mWidth = 900;
        mHeight = 1200;
        mTranslate = (int) ((mHeight - mHeight * SCALE_RATE) / 2 + density * 10);

        // 突出的卡片底部高度，抬高第一张卡片
        mDy = mTranslate - (mHeight * (1 - SCALE_RATE) / 2);
//        mDy = density * 5;
        // 卡片背景色
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white_smoke));
        paint.setAntiAlias(true);

        // 卡片边框色card
        paint_s = new Paint();
        paint_s.setColor(getResources().getColor(R.color.black_alpha_40));
        paint_s.setStyle(Paint.Style.STROKE);
        paint_s.setStrokeWidth(DeviceUtils.dip2px(getContext(), 1));
        paint_s.setAntiAlias(true);
        rect = new RectF();
        round = density * 8;
    }

    public interface OnSwipeListener {
        void onSwipe(int type);
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {

        this.onSwipeListener = onSwipeListener;
    }

    public void setAdapter(CardAdapter adapter) {
        this.adapter = adapter;

        SwipeLayout swipeLayout2 = new SwipeLayout(getContext());
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        swipeLayout2.setLayoutParams(params);
        swipeLayout2.setTranslationY(mTranslate);
        swipeLayout2.setScaleX(SCALE_RATE);
        swipeLayout2.setScaleY(SCALE_RATE);
        View bindLayout2 = adapter.bindLayout(); // 并不和 adapter 绑定，只是返回一个 cardView
        swipeLayout2.addView(bindLayout2);    // SwipeLayout 加入卡片内容
        addView(swipeLayout2);   // 卡堆加入一张卡片(第二张)，addView 的先后会影响卡片的 child Index 的，并且先加入的会被后加入的遮挡
        LogUtils.d("SwipeCardLayout", "setAdapter() - addView2(" + swipeLayout2.toString() + ")");

        SwipeLayout swipeLayout1 = new SwipeLayout(getContext());
        LayoutParams params1 = new LayoutParams(mWidth, mHeight);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        swipeLayout1.setLayoutParams(params1);
        View bindLayout1 = adapter.bindLayout();
        swipeLayout1.addView(bindLayout1);      // 生成第一张 SwipeLayout 左右划 ViewGroup，并加入卡堆
        addView(swipeLayout1);
        LogUtils.d("SwipeCardLayout", "setAdapter() - addView1(" + swipeLayout1.toString() + ")");

        adapter.bindData(adapter.tQueue.poll(), bindLayout1);    // 第一条数据和第一张卡片绑定了
        adapter.bindData(adapter.tQueue.poll(), bindLayout2);    // 第二条数据和第二张卡片绑定了

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        LogUtils.i(TAG, "onDraw: " + x + " " + y);
        float width = getWidth() / 2.0f;
        float height = getHeight() / 2.0f;
        View childAt = getChildAt(BACK_INDEX);   // 第二张

        // 第一张、第二张必然会画，是 add 到 layout 内的 View

        // 画固定底片
        // 画第四张
        if (adapter.tQueue.size() > 1) {
            float s_width1 = childAt.getWidth() * SCALE_RATE;
            float s_height1 = childAt.getHeight() * SCALE_RATE;
            float dx1 = (s_width1 - s_width1 * SCALE_RATE) / 2;
            float l1 = width - s_width1 / 2 + dx1;
            float t1 = height + s_height1 / 2 + mTranslate;
            float r1 = width + s_width1 / 2 - dx1;
            float b1 = t1 + mDy;

            rect.left = l1;
            rect.top = t1 - 20 * density;
            rect.right = r1;
            rect.bottom = b1;
            canvas.drawRoundRect(rect, round, round, paint);
            canvas.drawRoundRect(rect, round, round, paint_s);
        }

        // 画第三张
        if (adapter.tQueue.size() > 0) {
            float scaleX = childAt.getScaleX();
            float s_width = childAt.getWidth() * scaleX;
            float s_height = childAt.getHeight() * scaleX;

            float dx = (s_width - s_width * SCALE_RATE) / 2;
            float l = width - s_width / 2 + dx;
            float t = height + s_height / 2 + childAt.getTranslationY();
            float r = width + s_width / 2 - dx;
            float b = t + mDy;

            rect.left = l;
            rect.top = t - 20 * density;
            rect.right = r;
            rect.bottom = b;
            canvas.drawRoundRect(rect, round, round, paint);
            canvas.drawRoundRect(rect, round, round, paint_s);
        }
    }

    public static abstract class CardAdapter<T> {
        Queue<T> tQueue;

        public CardAdapter(Queue<T> tQueue) {
            this.tQueue = tQueue;
        }

        public abstract View bindLayout();

        public abstract void bindData(T data, View convertView);
    }

    // 一张卡片：可以左划 & 右划的 View，里面可以加载一张正常的卡片
    class SwipeLayout extends FrameLayout {

        private float eventRawX;
        private float eventRawY;
        private boolean isAnimation;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void refreshFloor() {
            // 更新下面的卡片大小（及位置）
            float translationX = Math.abs(getTranslationX());
            if (translationX <= SWIPE_SLOP) {
                float p = translationX / SWIPE_SLOP;
                RelativeLayout parent = (RelativeLayout) getParent();   // SwipeCardLayout
                View childAt = parent.getChildAt(BACK_INDEX);
                if (childAt != this) {
                    childAt.setScaleX(SCALE_RATE + (1 - SCALE_RATE) * p);
                    childAt.setScaleY(SCALE_RATE + (1 - SCALE_RATE) * p);
                    childAt.setTranslationY(mTranslate - (mTranslate * p));
                    parent.invalidate();
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (isAnimation) {  // 正在作卡片飞出动画
                return true;
            }
            // 防止第二张卡片也触发触控事件（会存在：第一张 hold 住了，第二张卡片先被划走；显露出第三第四张卡片等问题）
            // 最后一张不能划走
//            if (((RelativeLayout) getParent()).getChildAt(FRONT_INDEX) != this) {
//                return true;
//            }
            // 最后一张可以划走
            if (((RelativeLayout) getParent()).getChildCount() > 1 &&   // 不是最后一张卡片
                    ((RelativeLayout) getParent()).getChildAt(FRONT_INDEX) != this) {   // 触碰的不是第一张卡片
                return true;
            }
            LogUtils.d("SwipeLayout", "onTouchEvent() - this(" + this.toString() + ")");
            LogUtils.d("SwipeLayout", "onTouchEvent() - childAt0(" + ((RelativeLayout) getParent()).getChildAt(0).toString() + ")");

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    eventRawX = event.getRawX();
                    eventRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    float dx = rawX - eventRawX;
                    float dy = rawY - eventRawY;
                    // 卡片跟随手指移动
                    float translationX = getTranslationX();
                    setTranslationX(translationX + dx);
                    setTranslationY(getTranslationY() + dy);

                    // 卡片倾斜
                    if (Math.abs(translationX) <= SWIPE_SLOP) {
                        setRotation(getTranslationX() / SWIPE_SLOP * MAX_ROTATE);

                    }
                    refreshFloor();
                    eventRawX = rawX;
                    eventRawY = rawY;
                    break;
                case MotionEvent.ACTION_UP:
                    final float translationX1 = getTranslationX();
                    final RelativeLayout parent = (RelativeLayout) getParent();
                    if (Math.abs(translationX1) > SWIPE_SLOP) {
                        // 卡片位移过阈值，执行消失动画
                        int tx = translationX1 > SWIPE_SLOP ? parent.getWidth() : -parent.getWidth() / 2 - getWidth();
                        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("translationX", translationX1, tx);
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
                        animator.setDuration(100);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimation = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (onSwipeListener != null) {
                                    int type = translationX1 > SWIPE_SLOP ? TYPE_RIGHT : TYPE_LEFT;
                                    onSwipeListener.onSwipe(type);
                                }
                                if (adapter.tQueue.size() > 0) {
                                    SwipeLayout childAt = (SwipeLayout) parent.getChildAt(FRONT_INDEX);   // child 1 是它本身（第一张卡片），child 0 是下面一张卡片（后 addView 的）
                                    adapter.bindData(adapter.tQueue.poll(), childAt.getChildAt(0)); // child 1(划走的卡片和下一条数据绑定)
                                } else {
                                    parent.removeView(SwipeLayout.this);
                                    return;
                                }

                                if (adapter.tQueue.size() >= 0) {   // 还有数据，要有伪装的卡片(第三第四张)
                                    // childAt(0) 和 childAt(1) 进行了交换
                                    parent.getChildAt(BACK_INDEX).bringToFront();
                                    LogUtils.d("SwipeLayout", "onTouchEvent(2front) - this(" + this.toString() + ")");
                                    LogUtils.d("SwipeLayout", "onTouchEvent(2front) - childAt0(" + ((RelativeLayout) getParent()).getChildAt(0).toString() + ")");
                                    setRotation(0);
                                    setTranslationX(0);

                                    setTranslationY(mTranslate);
                                    setScaleX(SCALE_RATE);
                                    setScaleY(SCALE_RATE);
                                }
                                isAnimation = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                isAnimation = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        animator.start();
                    } else {
                        // 卡片位移没有过阈值，回复原位
                        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationX", translationX1, 0);
                        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationY", getTranslationY(), 0);
                        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotation", getRotation(), 0);
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2, holder3);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                refreshFloor();
                            }
                        });
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimation = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimation = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                isAnimation = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.setDuration(150);
                        animator.start();
                    }

                    break;
            }

            return true;
        }

    }
}

