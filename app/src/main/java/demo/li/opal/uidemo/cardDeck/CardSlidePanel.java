package demo.li.opal.uidemo.cardDeck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;

/**
 * 卡片滑动面板，主要逻辑实现类
 *
 * @author xmuSistone
 */
@SuppressLint({"HandlerLeak", "NewApi", "ClickableViewAccessibility"})
public class CardSlidePanel extends ViewGroup {
    private static final String TAG = CardSlidePanel.class.getSimpleName();

    private List<CardItemView> viewList = new ArrayList<>(); // 存放的是每一层的 view，从顶到底
    private List<View> releasedViewList = new ArrayList<>(); // 手指松开后存放的 view 列表，可能因为手速过快，同时会有多个 View 被松手开始做动画？？

    /* 拖拽工具类 */
    private final ViewDragHelper mDragHelper; // 这个跟原生的ViewDragHelper差不多，我仅仅只是修改了Interpolator
    private int initialTopViewX = 0, initialTopViewY = 0; // 最初时，中间 View（第一张卡片的左边和上边）的 x, y 位置
    private int allWidth = 0; // 面板的宽度
    private int allHeight = 0; // 面板的高度
    private int childWith = 0; // 每一个子 View 对应的宽度

    private static final float SCALE_STEP = 0.08f; // view 叠加缩放的步长
    private static final int MAX_SLIDE_DISTANCE_LINKAGE = 500; // 水平距离+垂直距离

    private int itemMarginTop = 10; // 卡片距离顶部的偏移量
    private int bottomMarginTop = 40; // 底部按钮与卡片的 margin 值
    private int yOffsetStep = 40; // view 叠加垂直偏移量的步长
    private int mTouchSlop = 5; // 判定为滑动的阈值，单位是像素

    private static final int X_VEL_THRESHOLD = 800; // vel：velocity，速度
    private static final int X_DISTANCE_THRESHOLD = 300;

    // 是否增加一个回弹？
    public static final int VANISH_TYPE_LEFT = 0;
    public static final int VANISH_TYPE_RIGHT = 1;

    private CardDeckListener cardDeckListener; // 回调接口
    private int isShowing = 0; // 当前正在显示的小项
    private Point downPoint = new Point();
    private CardAdapter adapter;
    private static final int VIEW_COUNT = Math.min(15, (int) Math.floor(1 / SCALE_STEP) + 2);  // 限制一下 VIEW_COUNT 数量，使得 scale 等永远是正值，不然会有很特别的反向增大现象;    //  Todo: 多于 4 是否会有问题，因为 processLinkageView 只处理了 4 张牌
    private Rect draggableArea;
    private WeakReference<Object> savedFirstItemData;

    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            orderViewStack();

            boolean reset = false;
            if (adapter.getCount() > 0) {
                Object firstObj = adapter.getItem(0);
                if (null == savedFirstItemData) {
                    // 此前就没有数据，需要保存第一条数据
                    // 异常情况？因为 doBindAdapter 会初始化 savedFirstItemData
                    savedFirstItemData = new WeakReference<>(firstObj);
                    isShowing = 0;
                } else {
                    Object savedObj = savedFirstItemData.get();
                    if (firstObj != savedObj) {
                        // 如果第一条数据不等的话，需要重置
                        isShowing = 0;
                        reset = true;
                        savedFirstItemData = new WeakReference<>(firstObj);
                    }
                }
            }

            int delay = 0;
            for (int i = 0; i < VIEW_COUNT; i++) {
                CardItemView itemView = viewList.get(i);
                if (isShowing + i < adapter.getCount()) {
                    if (itemView.getVisibility() == View.VISIBLE) {
                        if (!reset) {
                            continue;
                        }
                    } else if (i == 0) {
                        if (isShowing > 0) {
                            isShowing++;
                        }
                        cardDeckListener.onShow(isShowing);
                    }
                    // 卡片一张张渐显动画
                    if (i == VIEW_COUNT - 1) {
                        itemView.setAlpha(0);
                        itemView.setVisibility(View.VISIBLE);
                    } else {
                        itemView.setVisibilityWithAnimation(View.VISIBLE, delay++);
                    }
                    adapter.bindView(itemView, isShowing + i);
                } else {
                    itemView.setVisibility(View.INVISIBLE);
                }
            }
            if (delay > 0) {
                LogUtils.d(TAG, "delay > 0");
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardDeckListener.onCardDeckLoadFinish();
                    }
                }, CardItemView.DELAY_INTERVAL * delay + CardItemView.ANIM_DURATION);
            }
        }
    };

    public CardSlidePanel(Context context) {
        this(context, null);
    }

    public CardSlidePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardSlidePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.card);

        itemMarginTop = (int) a.getDimension(R.styleable.card_itemMarginTop, itemMarginTop);
        bottomMarginTop = (int) a.getDimension(R.styleable.card_bottomMarginTop, bottomMarginTop);
        yOffsetStep = (int) a.getDimension(R.styleable.card_yOffsetStep, yOffsetStep);
        // 滑动相关类
        mDragHelper = ViewDragHelper.create(this, 10f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        a.recycle();

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getChildCount() != VIEW_COUNT) {
                    doBindAdapter();
                }
            }
        });
    }

    private void doBindAdapter() {
        if (adapter == null || allWidth <= 0 || allHeight <= 0) {
            return;
        }

        // 1. addView 添加到 ViewGroup 中，添加了 4 张卡片
        for (int i = 0; i < VIEW_COUNT; i++) {
            CardItemView itemView = new CardItemView(getContext());
            itemView.bindLayoutResId(adapter.getLayoutId());    // R.layout.card_item
            itemView.setParentView(this);
            // Todo: wrap_content
            addView(itemView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            if (i < VIEW_COUNT - 2) {
                itemView.setAlpha(0);   // 最后一张卡片，alpha 为 0
            }
        }

        // 2. viewList 初始化，4 个卡片 View
        viewList.clear();
        for (int i = 0; i < VIEW_COUNT; i++) {
            // 上面 addView 加入的每一个，child index 依次增加，叠放次序依次往上（top），所以数据要反过来绑
            viewList.add((CardItemView) getChildAt(VIEW_COUNT - 1 - i));
        }

        // 3. 填充数据
        int count = adapter.getCount(); // 数据个数，不只是 VIEW_COUNT(4)，可能大大超出
        for (int i = 0; i < VIEW_COUNT; i++) {
            if (i < count) {
                adapter.bindView(viewList.get(i), i);
                if (i == 0) {
                    savedFirstItemData = new WeakReference<>(adapter.getItem(i));
                }
            } else {    // 数据个数小于 View 个数，即不足 VIEW_COUNT(4) 张卡片了
                viewList.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void doUnbindAdapter() {
        if (adapter == null) {
            return;
        }
        adapter.unregisterDataSetObserver(mDataObserver);
    }

    /**
     * 这是 viewDragHelper 拖拽效果的主要逻辑
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView,
                                          int left, int top,
                                          int dx, int dy) {
            onViewPosChanged((CardItemView) changedView);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 如果数据 List 为空，或者子 View 不可见，则不予处理
            if (adapter == null || adapter.getCount() == 0
                    || child.getVisibility() != View.VISIBLE
                    || child.getScaleX() <= 1.0f - SCALE_STEP) {
                // 一般来讲，如果拖动的是第三层、或者第四层的 View，则直接禁止
                // 此处用 getScale 的用法来巧妙回避
                return false;
            }

            // 1. 只有顶部的 View 才允许滑动
            int childIndex = viewList.indexOf(child);
            if (childIndex > 0) {
                return false;
            }

            // 2. 获取可滑动区域
            ((CardItemView) child).onStartDragging();
            if (draggableArea == null) {
                draggableArea = adapter.obtainDraggableArea(child);
            }

            // 3. 判断是否可滑动
            boolean shouldCapture = true;
            if (null != draggableArea) {
                shouldCapture = draggableArea.contains(downPoint.x, downPoint.y);
            }

            // 4. 如果确定要滑动，就让 touch 事件交给自己消费
            if (shouldCapture) {
                getParent().requestDisallowInterceptTouchEvent(shouldCapture);
            }
            return shouldCapture;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            // 这个用来控制拖拽过程中松手后，自动滑行的速度
            return 256;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            animToSide((CardItemView) releasedChild, (int) xvel, (int) yvel);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {  // 更新 x, 卡片跟随手指移动
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {  // 更新 y, 卡片跟随手指移动
            return top;
        }
    }

    // 调用 offsetLeftAndRight 导致 viewPosition 改变，会调到此处，更新除第一张以外的其他卡片
    public void onViewPosChanged(CardItemView changedView) {
        // 此处对 index 做保护处理
        int index = viewList.indexOf(changedView);
        if (index + 2 > viewList.size()) {  // index > VIEW_COUNT - 1 - 1，即最后一张卡片，牌堆中只有一张卡片了，没有下面的卡片需要微调
            return;
        }
        processLinkageView(changedView);
    }

    /**
     * 对 View 重新排序
     */
    private void orderViewStack() {
        if (releasedViewList.size() == 0) {
            // 是 TouchEvent action down （而没有左右划松手）触发事件的话
            // 如果在左右划松手动画未完成时又点击，会往下走
            return;
        }
        // Todo: ??打一下 log，应该是 release 到最后一张卡片了，所以没有卡片了？？不用再隐藏一下吗？
        CardItemView changedView = (CardItemView) releasedViewList.get(0);
        if (changedView.getLeft() == initialTopViewX) {
            releasedViewList.remove(0);
            return;
        }

        // 1. 消失的卡片 View 位置重置，由于大多手机会重新调用 onLayout 函数，所以此处大可以不做处理，不信你注释掉看看
        changedView.offsetLeftAndRight(initialTopViewX - changedView.getLeft());
        changedView.offsetTopAndBottom(initialTopViewY - changedView.getTop() + yOffsetStep * (VIEW_COUNT - 1)); // 2 是因为此卡片被放到最后了，而一共只显示三张卡片，之后的都被遮挡
        float scale = 1.0f - SCALE_STEP * (VIEW_COUNT - 1);
        changedView.setScaleX(scale);
        changedView.setScaleY(scale);
        changedView.setAlpha(0);

        // 2. 卡片 View 在 ViewGroup 中的次序调整，插入到最后
        LayoutParams lp = changedView.getLayoutParams();
        removeViewInLayout(changedView);
        addViewInLayout(changedView, 0, lp, true);  // view 的 index 越小，越在下面

        // 3. ChangedView 填充新数据，数据用完则隐藏卡片
        int newIndex = isShowing + VIEW_COUNT;
        if (newIndex < adapter.getCount()) {
            adapter.bindView(changedView, newIndex);
        } else {
            changedView.setVisibility(View.INVISIBLE);
        }

        // 4. viewList 中的卡片 view 的位次调整
        viewList.remove(changedView);
        viewList.add(changedView);
        releasedViewList.remove(0);

        // 5. 更新 showIndex、接口回调
        if (isShowing + 1 < adapter.getCount()) {
            isShowing++;
        }
        if (null != cardDeckListener) {
            cardDeckListener.onShow(isShowing);
        }
    }

    /**
     * 顶层卡片 View 位置改变，底层的位置需要调整
     *
     * @param changedView 顶层的卡片 view
     */
    private void processLinkageView(View changedView) {
        // 一共四张卡片，第一张是可以拖动和划走的
        int changeViewLeft = changedView.getLeft();
        int changeViewTop = changedView.getTop();
        // 这里需要计算两个方向的位移吗？即使计算两个方向，不应该使用勾股定理吗？
//        int distance = Math.abs(changeViewTop - initialTopViewY)    // 第一张卡片 Y 轴移动的距离
//                + Math.abs(changeViewLeft - initialTopViewX);    // 第一张卡片 X 轴移动的距离
        // 使用勾股定理计算位移，而不是单纯 x，y 方向相加
        float distance = (float) DeviceUtils.calDistance(
                new PointF(changeViewLeft, changeViewTop),
                new PointF(initialTopViewX, initialTopViewY));
        float rate = distance / (float) MAX_SLIDE_DISTANCE_LINKAGE;

//        float rate1 = rate;
//        float rate2 = rate - 0.1f;
//        // Todo: 是不是可以调整顺序优化一下？把 MAX_SLIDE_DISTANCE_LINKAGE 改成 50 验证一下
//        if (rate > 1) {
//            rate1 = 1;
//        }
//
//        if (rate2 < 0) {
//            rate2 = 0;
//        } else if (rate2 > 1) {
//            rate2 = 1;
//        }
//
//        adjustLinkageViewItem(changedView, rate1, 1);    // 第二张卡片，会缩放和上下移动
//        adjustLinkageViewItem(changedView, rate2, 2);    // 第三张卡片，会缩放和上下移动
        float rateOne;
        for (int i = 1; i < VIEW_COUNT - 1; i++) {
            // 这里使用 0.1f 而不是 SCALE_STEP，是为了越往后的卡片受顶部卡片的移动的影响越小，可以换成更大的值就能观察到影响
            rateOne = Math.max(Math.min(rate - 0.1f * (i - 1), 1), 0);
            adjustLinkageViewItem(changedView, rateOne, i);
        }

        // 第四张及更后面的卡片，一开始在第三张卡片后方，看不见，不缩放和移动，只改变透明度
        if (VIEW_COUNT == viewList.size()) {
            CardItemView bottomCardView = viewList.get(VIEW_COUNT - 1);
            bottomCardView.setAlpha(rate);      // 这里必须使用 rate，如果使用 rateOne（卡片很多时远小于 rate，造成划掉很多张卡片后，后面的卡片是半透明的）
        }
    }

    // 由 index 对应 view 变成 index-1 对应的 view
    private void adjustLinkageViewItem(View changedView, float rate, int index) {
        int changeIndex = viewList.indexOf(changedView);
        int initPosY = yOffsetStep * index;
        float initScale = 1 - SCALE_STEP * index;

        int nextPosY = yOffsetStep * (index - 1);   // 上面一张卡片的初始位移
        float nextScale = 1 - SCALE_STEP * (index - 1); // 上面一张卡片的初始大小

        int offset = (int) (initPosY + (nextPosY - initPosY) * rate);
        float scale = initScale + (nextScale - initScale) * rate;

        View adjustView = viewList.get(changeIndex + index);
        adjustView.offsetTopAndBottom(offset - adjustView.getTop() + initialTopViewY);  // adjustView.getTop() - initialTopViewY = 已有的 offset
        adjustView.setScaleX(scale);
        adjustView.setScaleY(scale);
//        if (adjustView.getAlpha() != 1.0f) {
//            adjustView.setAlpha(1.0f);
//        }
    }

    /**
     * 松手时处理滑动到边缘的动画
     */
    private void animToSide(CardItemView changedView, int xVel, int yVel) {
        int finalX = initialTopViewX;
        int finalY = initialTopViewY;
        int flyType = -1;

        // 1. 下面这一坨计算 finalX 和 finalY，要读懂代码需要建立一个比较清晰的数学模型才能理解，不信拉倒
        int dx = changedView.getLeft() - initialTopViewX;
        int dy = changedView.getTop() - initialTopViewY;

        // yVel < xVel * xyRate 则允许以速度计算偏移
        final float xyRate = 3f;
        if (xVel > X_VEL_THRESHOLD && Math.abs(yVel) < xVel * xyRate) {
            // x 正方向的速度足够大，向右滑动消失
            finalX = allWidth;
            finalY = yVel * (childWith + changedView.getLeft()) / xVel + changedView.getTop();  // x 方向速度越大，或者 View 越靠右边，y 方向位移越小
            flyType = VANISH_TYPE_RIGHT;
        } else if (xVel < -X_VEL_THRESHOLD && Math.abs(yVel) < -xVel * xyRate) {
            // x 负方向的速度足够大，向左滑动消失
            finalX = -childWith;
            finalY = yVel * (childWith + changedView.getLeft()) / (-xVel) + changedView.getTop();
            flyType = VANISH_TYPE_LEFT;
        } else if (dx > X_DISTANCE_THRESHOLD && Math.abs(dy) < dx * xyRate) {
            // x 正方向的位移足够大，向右滑动消失
            finalX = allWidth;
            finalY = dy * (childWith + initialTopViewX) / dx + initialTopViewY;
            flyType = VANISH_TYPE_RIGHT;
        } else if (dx < -X_DISTANCE_THRESHOLD && Math.abs(dy) < -dx * xyRate) {
            // x 负方向的位移足够大，向左滑动消失
            finalX = -childWith;
            finalY = dy * (childWith + initialTopViewX) / (-dx) + initialTopViewY;
            flyType = VANISH_TYPE_LEFT;
        }

        // （计算后）如果斜率太高，y 方向超出面板范围，就折中处理
        if (finalY > allHeight) {
            finalY = allHeight;
        } else if (finalY < -allHeight / 2) {
            finalY = -allHeight / 2;
        }

        // 如果没有飞向两侧（y 方向速度远大于 x 方向，或者 x 方向位移没有超出阈值），而是回到了中间，需要谨慎处理
        if (finalX == initialTopViewX) {
            changedView.animTo(initialTopViewX, initialTopViewY);
        } else {
            // 2. 向两边消失的动画
            releasedViewList.add(changedView);
            if (mDragHelper.smoothSlideViewTo(changedView, finalX, finalY)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

            // 3. 消失动画即将进行，listener 回调
            if (flyType >= 0 && cardDeckListener != null) {
                // Todo: 区分一下左右划
                cardDeckListener.onCardVanish(isShowing, flyType);
            }
        }
    }

    /**
     * 点击按钮消失动画
     */
    private void vanishOnBtnClick(int type) {
        View animateView = viewList.get(0);
        if (animateView.getVisibility() != View.VISIBLE || releasedViewList.contains(animateView)) {
            return;
        }

        int finalX = 0;
        int extraVanishDistance = 100; // 为加快 vanish 的速度，额外添加消失的距离
        if (type == VANISH_TYPE_LEFT) {
            finalX = -childWith - extraVanishDistance;
        } else if (type == VANISH_TYPE_RIGHT) {
            finalX = allWidth + extraVanishDistance;
        }

        if (finalX != 0) {
            releasedViewList.add(animateView);
            if (mDragHelper.smoothSlideViewTo(animateView, finalX, initialTopViewY + allHeight / 2)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        if (type >= 0 && cardDeckListener != null) {
            cardDeckListener.onCardVanish(isShowing, type);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            // 动画结束
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                orderViewStack();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        // 按下时保存坐标信息
        if (action == MotionEvent.ACTION_DOWN) {
            this.downPoint.x = (int) ev.getX();
            this.downPoint.y = (int) ev.getY();
        }
        return super.dispatchTouchEvent(ev);
    }

    /* touch 事件的拦截与处理都交给 mDragHelper 来处理 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN 的时候就对 view 重新排序，更新卡堆，即使动画没有做完
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING) {
                mDragHelper.abort();
            }
            orderViewStack();

            // 保存初次按下时 arrowFlagView 的 Y 坐标
            // action_down 时就让 mDragHelper 开始工作，否则有时候导致异常
            mDragHelper.processTouchEvent(ev);
        }
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            // 统一交给 mDragHelper 处理，由 DragHelperCallback 实现拖动效果
            // 该行代码可能会抛异常，正式发布时请将这行代码加上 try catch
            mDragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));

        allWidth = getMeasuredWidth();
        allHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed,
                            int left, int top,
                            int right, int bottom) {
        // 一共 VIEW_COUNT 个 View，从 viewList 的 [0, VIEW_COUNT)（child 的 (VIEW_COUNT, 0]）是第一张到第四张卡片
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View viewItem = viewList.get(i);
            // 1. 先 layout 出来
            int childHeight = viewItem.getMeasuredHeight();
            int viewLeft = (getWidth() - viewItem.getMeasuredWidth()) / 2;  // 居中
            viewItem.layout(viewLeft, itemMarginTop, viewLeft + viewItem.getMeasuredWidth(), itemMarginTop + childHeight);

            // 2. 调整位置
            int offset = yOffsetStep * i;
            float scale = 1 - SCALE_STEP * i;

            if (i > VIEW_COUNT - 2) {
                // 备用的 View，最后一张卡片
                offset = yOffsetStep * (VIEW_COUNT - 2);   // 最后一张备用卡或者超出 countThresh 后的卡片位置同前一张重叠
                scale = 1 - SCALE_STEP * (VIEW_COUNT - 2); // 最后一张备用卡或者超出 countThresh 后的卡片大小同前一张也一样
            }
            viewItem.offsetTopAndBottom(offset);

            // 3. 调整缩放、重心等
            viewItem.setPivotY(viewItem.getMeasuredHeight());   // y 轴底部为变化中心
            viewItem.setPivotX(viewItem.getMeasuredWidth() / 2);   //  x 轴中间为变化中心
            viewItem.setScaleX(scale);
            viewItem.setScaleY(scale);
        }

        if (childCount > 0) {
            // 初始化一些中间参数
            initialTopViewX = viewList.get(0).getLeft();
            initialTopViewY = viewList.get(0).getTop();
            childWith = viewList.get(0).getMeasuredWidth();
        }
    }

    public void setAdapter(final CardAdapter adapter) {
        this.adapter = adapter;
        doBindAdapter();
        adapter.registerDataSetObserver(mDataObserver);
    }

    public CardAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置卡片操作回调
     */
    public void setCardDeckListener(CardDeckListener cardDeckListener) {
        this.cardDeckListener = cardDeckListener;
    }

    /**
     * 卡片回调接口
     */
    public interface CardDeckListener {
        /**
         * 新卡片显示回调
         *
         * @param index 最顶层显示的卡片的index
         */
        void onShow(int index);

        /**
         * 卡片飞向两侧回调
         *
         * @param index 飞向两侧的卡片数据 index
         * @param type  飞向哪一侧{@link #VANISH_TYPE_LEFT}或{@link #VANISH_TYPE_RIGHT}
         */
        void onCardVanish(int index, int type);

        void onCardDeckLoadFinish();
    }
}