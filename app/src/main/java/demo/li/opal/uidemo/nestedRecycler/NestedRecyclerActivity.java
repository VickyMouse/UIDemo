//package demo.li.opal.uidemo.nestedRecycler;
//
//import android.annotation.SuppressLint;
//import android.graphics.Rect;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.widget.NestedScrollView;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.TextPaint;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.facebook.drawee.view.SimpleDraweeView;
//
//import demo.li.opal.uidemo.R;
//import demo.li.opal.uidemo.Utils.DeviceUtils;
//import demo.li.opal.uidemo.Utils.LogUtils;
//import demo.li.opal.uidemo.views.IndicatorLinearLayout;
//import demo.li.opal.uidemo.page.PagerGridLayoutManager;
//import demo.li.opal.uidemo.page.PagerGridSnapHelper;
//import demo.li.opal.uidemo.swipe.FeedsSwipeRefreshLayout;
//import demo.li.opal.uidemo.viewanimator.AnimationListener;
//import demo.li.opal.uidemo.viewanimator.ViewAnimator;
//
//public class NestedRecyclerActivity extends AppCompatActivity implements FeedsSwipeRefreshLayout.OnRefreshListener, FeedsListController.OnFeedsListResponse {
//    public static final String TAG = NestedRecyclerActivity.class.getSimpleName();
//    private MainEntranceAdapter mainFunEntranceAdapter;
//    private NestedScrollView mScrollView;
//
//    private FeedsListController mFeedsListController;
//
//    private RecyclerView mCenterOps;
//    private RecyclerView mHotTags;
//    private FeedsSwipeRefreshLayout mRefreshLayout;
//    private SimpleDraweeView mBtnAvatar;
//    private SimpleDraweeView mBtnScrollAvatar;
//    private TextView mFeedsTitle;
//    private Toast mFeedsUpdateToast;
//    private ImageView mCamera;
//
//    // 热门 tag
//    private static final int ROW_COUNT = 1;
//    private FeedHotTagsAdapter mHotTagsAdapter;
//    private GridLayoutManager mLayoutManager;
//    private String bannerUri;
//
//    private float topBarHeight; // 控件距离 coordinatorLayout 底部距离
//    private boolean isTopBarAnimating; // 动画是否在进行
//    private ViewGroup mTopBar; // 动画是否在进行
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nested_recycler);
//
//        mCenterOps = (RecyclerView) findViewById(R.id.main_center_ops);
//        mHotTags = (RecyclerView) findViewById(R.id.hot_tags);
//        mRefreshLayout = (FeedsSwipeRefreshLayout) findViewById(R.id.refresh_layout);
//        mFeedsTitle = (TextView) findViewById(R.id.main_feeds_title);
//        mBtnAvatar = (SimpleDraweeView) findViewById(R.id.btn_avatar);
//        mBtnScrollAvatar = (SimpleDraweeView) findViewById(R.id.btn_scroll_avatar);
//        mCamera = findViewById(R.id.main_camera);
//
//        initMainBannerUI();
//        initMainIconUI();
//        initHotTagsUI();
//        initMainTopUI();
//        initRefreshLayout();
//        initScrollView();
//        TextPaint tp = mFeedsTitle.getPaint();
//        tp.setFakeBoldText(true);
//        mFeedsListController = new FeedsListController(this,
//                FeedsListController.FeedListID.feed_list_hot.getRequestID(), this);
//        mFeedsListController.fetchData(true);
//    }
//
//    MainEntranceAdapter.SpaceItemDecoration decoration;
//
//    public void initMainIconUI() {
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCenterOps.getLayoutParams();
//        final IndicatorLinearLayout centerIndicator = (IndicatorLinearLayout) findViewById(R.id.main_center_indicator);
//        PagerGridLayoutManager gridLayoutManager = new PagerGridLayoutManager(2, 2, PagerGridLayoutManager.HORIZONTAL);
//        gridLayoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
//            @Override
//            public void onPageSizeChanged(int pageSize) {
//
//            }
//
//            @Override
//            public void onPageSelect(int pageIndex) {
//                centerIndicator.changeIndex(pageIndex);
//            }
//        });
//        final int space = getResources().getDimensionPixelSize(R.dimen.camera_bottom_button_text_btm_margin);
//        mainFunEntranceAdapter = new MainEntranceAdapter(this, new MainEntranceAdapter.MergedDataCallback() {
//            @Override
//            public void onLoaded() {
//                mCenterOps.removeItemDecoration(decoration);
//                decoration = new MainEntranceAdapter.SpaceItemDecoration(space, (int) Math.ceil(mainFunEntranceAdapter.getItemCount() / 2.0f));
//                mCenterOps.addItemDecoration(decoration);
//                mCenterOps.getAdapter().notifyDataSetChanged();
//                centerIndicator.total((int) Math.ceil(mCenterOps.getAdapter().getItemCount() / 4.0f));
//            }
//        });
//        centerIndicator.total(3);
//        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
//        pageSnapHelper.attachToRecyclerView(mCenterOps);
//        params.height = mainFunEntranceAdapter.thisRecycleViewHeight();
//        mCenterOps.setLayoutParams(params);
//        mCenterOps.setLayoutManager(gridLayoutManager);
//        decoration = new MainEntranceAdapter.SpaceItemDecoration(space);
//        mCenterOps.addItemDecoration(decoration);
//        mCenterOps.setAdapter(mainFunEntranceAdapter);
//        mCenterOps.setOverScrollMode(View.OVER_SCROLL_NEVER);
//    }
//
//    public void initHotTagsUI() {
////        android.os.Debug.waitForDebugger();
//        mLayoutManager = new GridLayoutManager(this, ROW_COUNT, LinearLayoutManager.HORIZONTAL, false);
//        mHotTags.setLayoutManager(mLayoutManager);
//        mHotTagsAdapter = new FeedHotTagsAdapter(this);
//        mHotTags.setAdapter(mHotTagsAdapter);
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    public void initMainTopUI() {
//        topBarHeight = getResources().getDimensionPixelSize(R.dimen.feeds_top_bar_height);
//        mTopBar = (ViewGroup) findViewById(R.id.top_bar);
//        mTopBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mScrollView != null) {
//                    mScrollView.scrollTo(0, 0);
//                }
//            }
//        });
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    public void initMainBannerUI() {
//        final ImageView mainEmptyBottom = (ImageView) findViewById(R.id.main_empty_bottom);
//        final ImageView mainEmptyTop = (ImageView) findViewById(R.id.main_empty_top);
//        FrameLayout mainEmptyTopRoot = (FrameLayout) findViewById(R.id.main_empty_top_root);
//        // 计算top图片的size
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mainEmptyTopRoot.getLayoutParams();
//        lp.width = DeviceUtils.getScreenWidth(this)
//                - 2 * getResources().getDimensionPixelSize(R.dimen.camera_bottom_button_text_btm_margin);
//        lp.height = (int) (lp.width * 0.637f);  // UE的设计要求是宽:高比为350:223
//        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        mainEmptyTopRoot.setLayoutParams(lp);
//        // 计算bottom图片的size
//        lp = (RelativeLayout.LayoutParams) mainEmptyBottom.getLayoutParams();
//        lp.width = DeviceUtils.getScreenWidth(this);
//        lp.height = (int) (lp.width * 0.71f);  // UE的设计要求是宽:高比为53:75
//        mainEmptyBottom.setLayoutParams(lp);
//        final int move = ViewConfiguration.get(this).getScaledTouchSlop() * 2;
//        mainEmptyTop.setImageResource(R.drawable.main_default_header_fg);
//        mainEmptyBottom.setImageResource(R.drawable.main_default_header_bg);
//    }
//
////    private void initHeader() {
////        if (mFeedsListController != null) {
////            RefreshableRecyclerViewHeader mainHeader = (RefreshableRecyclerViewHeader) findViewById(R.id.main_header);
////            mFeedsListController.attachHeader(mainHeader);
////            mainHeader.setOnScrollTopListener(new RefreshableRecyclerViewHeader.OnScrollTopListener() {
////                @Override
////                public void onTop(boolean isTop) {
////                    mRefreshLayout.setEnabled(isTop);
////                }
////            });
////        }
////    }
//
//    private void initRefreshLayout() {
//        mRefreshLayout.setColorSchemeResources(R.color.main_color);
//        mRefreshLayout.setOnRefreshListener(this);
//    }
//
//    private void initScrollView() {
//        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
//        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                LogUtils.i(TAG, "NestedScrollView 滑动");
//                if (scrollY <= 0) {
//                    // 滑到顶部
//                    LogUtils.i(TAG, "NestedScrollView 滑到顶部");
//                    mRefreshLayout.setEnabled(true);
//                    checkTopBarVisibility();
//                    mCamera.setAlpha(1f);
//                } else {
//                    if (oldScrollY <= 0) {
//                        LogUtils.i(TAG, "NestedScrollView 从顶部滑开");
//                        mRefreshLayout.setEnabled(false);
//                    }
//                }
////                LogUtils.i(TAG, "NestedScrollView(" + scrollY + ", " + v.getChildAt(0).getMeasuredHeight() + ", " + v.getMeasuredHeight() + ", " + (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) + ")");
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    // 滑到底部
//                    LogUtils.i(TAG, "NestedScrollView 滑到底部");
//                    if (mFeedsListController != null) {
//                        mFeedsListController.onScrollToBottom();
//                    }
//                }
//
//                float newAlpha = (float) (scrollY - 240) / 500;
//                newAlpha = newAlpha > 0 ? newAlpha : 0;
//                newAlpha = newAlpha < 1 ? newAlpha : 1;
//                mCamera.setAlpha(1 - newAlpha);
//                if (newAlpha == 1) {
//                    mCamera.setEnabled(false);
//                    mCamera.setVisibility(View.GONE);
//                } else {
//                    mCamera.setEnabled(true);
//                    mCamera.setVisibility(View.VISIBLE);
//                }
//
//
//                // 控制拍照相机的消失可见
////                if (scrollY < oldScrollY && mCamera.getAlpha() < 1 && scrollY <= 240) {
////                    // 向下滑动
////                    float newAlpha = mCamera.getAlpha() + 0.02f;
////                    if (newAlpha > 1) {
////                        newAlpha = 1;
////                    }
////                    mCamera.setAlpha(newAlpha);
////                } else if (scrollY > oldScrollY && mCamera.getAlpha() > 0 && scrollY >= 60) {
////                    // 向上滑动
////                    float newAlpha = mCamera.getAlpha() - 0.02f;
////                    if (newAlpha < 0) {
////                        newAlpha = 0;
////                    }
////                    mCamera.setAlpha(newAlpha);
////                }
//            }
//        });
//    }
//
//    @Override
//    public void onRefresh() {
//        mRefreshLayout.setRefreshing(true);
//        reloadFeedsList();
//        // 首页的上半部分也需要进行刷新
//
////        mRefreshLayout.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                mRefreshLayout.setRefreshing(false);
////            }
////        }, 3000);
//    }
//
//    private void reloadFeedsList() {
//        if (mFeedsListController != null) {
//            mFeedsListController.refresh();
//        }
//    }
//
//    public void checkTopBarVisibility() {
//        int[] location = new int[2];
//        mFeedsTitle.getLocationInWindow(location);
//        LogUtils.i(TAG, "[onFeedListScroll] top = " + location[1] + ", isAnimate = " + isTopBarAnimating);
//        if (location[1] >= 0 && !isTopBarAnimating && mTopBar.getVisibility() == View.VISIBLE) {
//            showTopBarAnim(false);
//        } else if (location[1] < 0 && !isTopBarAnimating && mTopBar.getVisibility() != View.VISIBLE) {
//            showTopBarAnim(true);
//            if (mFeedsUpdateToast != null) {
//                mFeedsUpdateToast.cancel();
//            }
//        }
//    }
//
//    private void showTopBarAnim(boolean show) {
//        LogUtils.i(TAG, "show()");
//        isTopBarAnimating = true;
//        if (show) {
//            mTopBar.setVisibility(View.VISIBLE);
//            ViewAnimator.animate(mTopBar)
//                    .translationY(-topBarHeight, 0)
//                    .duration(300)
//                    .decelerate()
//                    .onStop(new AnimationListener.Stop() {
//                        @Override
//                        public void onStop() {
//                            isTopBarAnimating = false;
//                        }
//                    })
//                    .start();
//        } else {
//            ViewAnimator.animate(mTopBar)
//                    .translationY(0, -topBarHeight)
//                    .duration(300)
//                    .decelerate()
//                    .onStop(new AnimationListener.Stop() {
//                        @Override
//                        public void onStop() {
//                            mTopBar.setVisibility(View.INVISIBLE);
//                            isTopBarAnimating = false;
//                        }
//                    })
//                    .start();
//        }
//    }
//
//    /**
//     * GridLayoutManager的边距
//     */
//    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//        /**
//         * 具体边距值，单位px
//         */
//        private int space;
//        /**
//         * 单行的item的个数
//         */
//        private int lineCounts;
//        /**
//         * Grid单行个数
//         */
//        private static final int sGridColumns = 6;
//
//        public SpaceItemDecoration(int space) {
//            this.space = space;
//            this.lineCounts = sGridColumns;
//        }
//
//        public SpaceItemDecoration(int space, int lineCounts) {
//            this.space = space;
//            this.lineCounts = lineCounts;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            int position = parent.getChildLayoutPosition(view);
//
//            if (position % 4 == 0 || position % 4 == 1) {
//                outRect.bottom = space / 2;
//            } else {
//                outRect.top = space / 2;
//            }
//
//            if (position % 2 == 0) {
//                outRect.left = space;
//                outRect.right = space / 2;
//            }
//
//            if (position % 2 == 1) {
//                outRect.left = space / 2;
//                outRect.right = space;
//            }
//        }
//    }
//
//    @Override
//    public void onFeedFetchStart(boolean isFirstPage) {
//        LogUtils.d(TAG, "[onFeedFetchStart] isFirstPage = " + isFirstPage);
//        if (isFirstPage) {
//            mRefreshLayout.setRefreshing(true);
//        }
//    }
//
//    @Override
//    public void onFeedFetchSuccess(boolean isEmpty, String msg) {
//        LogUtils.d(TAG, "[onFeedFetchSuccess] isEmpty = " + isEmpty + ", msg = " + msg);
//        mRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
//
//    @Override
//    public void onFeedFetchFail(final int errorCode, final String msg, boolean isFirstPage) {
//        LogUtils.d(TAG, "[onFeedFetchFail] errorCode = " + errorCode + ", msg = " + msg);
//        mRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(false);
//                LogUtils.d(TAG, "Error " + errorCode + ": " + msg);
//            }
//        });
//    }
//
//}
