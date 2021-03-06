package demo.li.opal.uidemo.cardDeck;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * 卡片 View 项
 *
 * @author opalli on 2018/12/01
 */
@SuppressLint("NewApi")
public class CardItemView extends FrameLayout {

    public static final int ANIM_DURATION = 300;
    public static final int DELAY_INTERVAL = 150;
    private Spring springX, springY;
    private CardSlidePanel parentView;
    private ObjectAnimator alphaAnimator;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSpring();
    }

    private void initSpring() {
        SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(15, 20);
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring().setSpringConfig(springConfig);
        springY = mSpringSystem.createSpring().setSpringConfig(springConfig);

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
            }
        });
    }

    /**
     * 动画移动到某个位置
     */
    public void animTo(int xPos, int yPos) {
        setCurrentSpringPos(getLeft(), getTop());
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置当前 spring 位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    public void setParentView(CardSlidePanel parentView) {
        this.parentView = parentView;
    }

    public void onStartDragging() {
        springX.setAtRest();
        springY.setAtRest();
    }

    public void bindLayoutResId(int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutResId, null);
        addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void setVisibilityWithAnimation(final int visibility, int delayIndex) {
        if (visibility == View.VISIBLE && getVisibility() != View.VISIBLE) {
            setAlpha(0);
            setVisibility(visibility);

            if (null != alphaAnimator) {
                alphaAnimator.cancel();
            }
            alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
            alphaAnimator.setDuration(ANIM_DURATION);
            alphaAnimator.setStartDelay(delayIndex * DELAY_INTERVAL);
            alphaAnimator.start();
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        // 抗锯齿
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
//                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG)); //设置图形、图片的抗锯齿
//        super.onDraw(canvas);
//    }
}
