package demo.li.opal.uidemo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import demo.li.opal.uidemo.Utils.ApiHelper;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;
import demo.li.opal.uidemo.Utils.NotchInScreenUtils;

public class GridActivity extends AppCompatActivity {
    private static final String TAG = GridActivity.class.getSimpleName();

    private static final int COLUMN_COUNT = 3;
    public static MainRatioType mainRatioType = MainRatioType.getDefault();
    public static int mainWidth, mainHeight;
    public static int bannerHeight;

    private RecyclerView recyclerGrid;
    private ViewGroup root;
    ArrayList<MainButtonModel> icons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        if (NotchInScreenUtils.hasNotchInScreenHw(GlobalContext.getContext())) {
            LogUtils.i("WindowSize", "hasNotchInScreenHw(" + NotchInScreenUtils.getNotchHeightHw(GlobalContext.getContext()) + ")");
        } else if (NotchInScreenUtils.hasNotchInScreenAtXM()) { //Todo: 还必须验证一下 vivo 和 oppo 的情况，小米没有 notch 的手机，也需要减去 status bar
            LogUtils.i("WindowSize", "hasNotchInScreenAtXM(" + NotchInScreenUtils.getStatusBarHeight(GlobalContext.getContext()) + ")");
        }

        final Window window = getWindow();

        // 拿屏幕宽高方法 1 - window 尺寸
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        LogUtils.d(TAG, "WindowSize1(" + lp.width + ", " + lp.height + ")");
        LogUtils.d(TAG, "WindowSize1(" + window.getDecorView().getWidth() + ", " + window.getDecorView().getHeight() + ")");

        // 拿屏幕宽高方法 2 - Window DisplayMetrics 尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) GlobalContext.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            } else {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }
        }
        LogUtils.d(TAG, "WindowSize2(" + displayMetrics.widthPixels + ", " + displayMetrics.heightPixels + ")");

        // 拿屏幕宽高方法 3 - P 图使用的方式，不适用刘海屏
        LogUtils.d(TAG, "WindowSize3(" + DeviceUtils.getScreenWidth(this) + ", " + DeviceUtils.getScreenHeight(this) + ")");

        // 拿屏幕宽高方法 4（布丁使用）
        root = findViewById(R.id.root);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
                if (ApiHelper.hasJellyBean()) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }

                ViewGroup.LayoutParams lp = root.getLayoutParams();
                LogUtils.d(TAG, "WindowSize4(" + lp.width + ", " + lp.height + ")");
                LogUtils.d(TAG, "WindowSize4(" + root.getWidth() + ", " + root.getHeight() + ")");

                // 拿屏幕宽高方法 1 again - window 尺寸
                LogUtils.d(TAG, "WindowSize1 - postLayout(" + lp.width + ", " + lp.height + ")");
                LogUtils.d(TAG, "WindowSize1 - postLayout(" + window.getDecorView().getWidth() + ", " + window.getDecorView().getHeight() + ")");
            }
        });

        // 拿屏幕宽高方法 5（P 图最新）
        Rect rectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        LogUtils.d("MainScreen", "WindowSize5(" + rectangle.left + ", " + rectangle.top + ", " + rectangle.right + ", " + rectangle.bottom + ")");
        int width = rectangle.right - rectangle.left;
        int height = rectangle.bottom - rectangle.top;
        LogUtils.d("MainScreen", "WindowSize5(" + width + ", " + height + ")");

        // 方法 5
        if (width > 0 && height > 0) {
            if (width * 1.0 / height > 0.75) {
                lp.width = (int) (lp.height * 0.5);
            }
        }
        // 全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //下面图1
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            //下面图2
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            //下面图3
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            getWindow().setAttributes(lp);
        }
        getWindow().setAttributes(lp);

        initButtons();

        checkMainScreenSize();
        checkMainRatioType();
        checkBannerHeight();

        recyclerGrid = findViewById(R.id.grid);
        recyclerGrid.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        recyclerGrid.setLayoutManager(layoutManager);
        //绑定适配器
        MainIconAdapter adapter = new MainIconAdapter(this, icons);
        recyclerGrid.setAdapter(adapter);
//        adapter.setOnItemClickListener(this);//将接口传递到数据产生的地方

        // 计算并设置网格中 icon 的重叠区域
        double overlapVPercent;
        overlapVPercent = mainRatioType.getIcOverlapV();
        final int overlapH = (int) (mainWidth * MAIN_ICON_H_OVERLAP);
        final int overlapV = (int) (mainWidth * overlapVPercent);

        recyclerGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildLayoutPosition(view);
                if (position / COLUMN_COUNT != 0) {   // 非第一行
                    outRect.top = -overlapV;
                }

                int columnIndex = position % COLUMN_COUNT;
                if (columnIndex != 0) {   // 非第一列，需要调整位置
                    outRect.left = -overlapH * columnIndex;
                }
                LogUtils.d(TAG, "mainIc(" + position + "): (" + outRect.left + ", " + outRect.right + ", " + outRect.top + ", " + outRect.bottom + ")");
            }
        });

    }


    private void checkMainRatioType() {
        double ratio = mainWidth * 1.0 / mainHeight;
        mainRatioType = MainRatioType.getRatioTypeByRatio(ratio);
    }


    private void checkMainScreenSize() {
        // 采用 DeviceUtils 中的 getScreenHeight 在小米手机上获得的高度会比真实高度短（猜测是减去了 statusBar 的高度，即使 statusBar 并没有显示）
        // 下述这个方法，会获取整个屏幕的高度，包含 status bar，但不包含 notch（TT——TT），但在小米部分手机上需要减去一个 statusBar，但是sb又不是很好判断，所以也废弃
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) GlobalContext.getContext().getSystemService(Context.WINDOW_SERVICE);
//        if (windowManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
//            } else {
//                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//            }
//        }
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//        mainHeight = Math.max(width, height);
//        mainWidth = Math.min(width, height);
//
////        android.os.Debug.waitForDebugger();
//        if (NotchInScreenUtils.hasNotchInScreenHw(GlobalContext.getContext())) {
//            LogUtils.i("MainScreen", "hasNotchInScreenHw()");
//            mainHeight -= NotchInScreenUtils.getNotchHeightHw(GlobalContext.getContext());
//        } else if (NotchInScreenUtils.hasNotchInScreenAtXM()) { //Todo: 还必须验证一下 vivo 和 oppo 的情况，小米没有 notch 的手机，也需要减去 status bar
//            LogUtils.i("MainScreen", "hasNotchInScreenAtXM()");
//            mainHeight -= NotchInScreenUtils.getStatusBarHeight(GlobalContext.getContext());
//        }
        // 还是改成 UI 渲染完成后，拿取根节点宽高吧
        Rect rectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        LogUtils.i("MainScreen", "MainScreenRect(" + rectangle.left + ", " + rectangle.top + ", " + rectangle.right + ", " + rectangle.bottom + ")");
        int width = rectangle.right - rectangle.left;
        int height = rectangle.bottom - rectangle.top;
        mainHeight = Math.max(width, height);
        mainWidth = Math.min(width, height);
        LogUtils.i("MainScreen", "MainScreen(" + mainWidth + ", " + mainHeight + ")");
    }

    /**
     * 获取到显示到首页的按钮列表：
     * 如果有参数引导需要调用的模块，那么就筛选出这些模块进行显示
     * 如果没有参数引导，那么就是启动默认的模块。注意默认顺序需要进行排序，按照priority排序，更大的靠前。
     * 上面固定的2个大按钮，分别为3000，2000，ViewPager里面的按钮从1000开始，900，800，700递减。
     * <p>
     * 参数可以为空，代表获取到默认的列表
     *
     * @return
     */
    public ArrayList<MainButtonModel> initButtons() {
        // 美化照片
        icons.add(new MainButtonModel(R.id.btn_editor, 3000, R.drawable.ic_main_editor_normal,
                getResources().getString(R.string.editor), Module.TTPTBEAUTIFY.name()));

        // 美容美妆
        icons.add(new MainButtonModel(R.id.btn_beauty, 2000, R.drawable.ic_main_cosmetics_normal,
                getResources().getString(R.string.makeup), Module.TTPTFACE.name()));

        // 故事拼图
        icons.add(new MainButtonModel(R.id.btn_collage, 1000, R.drawable.ic_main_collage_normal,
                getResources().getString(R.string.collage), Module.TTPTCOLLAGE.name()));

        // 疯狂变脸
        icons.add(new MainButtonModel(R.id.btn_cosfun, 700, R.drawable.ic_main_cosfun_normal,
                getResources().getString(R.string.cosmeticfun), Module.TTPTCOSFUN.name()));

        // 大头贴
        icons.add(new MainButtonModel(R.id.btn_play_sticker, 610, R.drawable.ic_main_play_sticker_normal,
                getResources().getString(R.string.play_sticker), Module.TTPTPLAYSTICKER.name()));


        // 魔法抠图
        icons.add(new MainButtonModel(R.id.btn_buckle, 650, R.drawable.ic_main_buckle_normal,
                getResources().getString(R.string.buckle), Module.TTPTBUCKLE.name()));

        //视频模板
        icons.add(new MainButtonModel(R.id.btn_video_tpl, 626, R.drawable.ic_main_video_tpl_normal,
                getResources().getString(R.string.video_template), Module.TTPTMVEDIT.name()));

        // 表情ME
        icons.add(new MainButtonModel(R.id.btn_emoji, 620, R.drawable.ic_main_emoji_normal,
                getResources().getString(R.string.emoji), Module.TTPTEMOJI.name()));

        // GIF表情包
        icons.add(new MainButtonModel(R.id.btn_gif, 580, R.drawable.ic_main_gif_normal,
                getResources().getString(R.string.gif_camera), Module.TTPTGIF.name()));

        //动感MV
        icons.add(new MainButtonModel(R.id.btn_video, 500, R.drawable.ic_main_video_normal,
                getResources().getString(R.string.video), Module.TTPTVIDEOEDIT.name()));

        // 趣味多图
        icons.add(new MainButtonModel(R.id.btn_batch, 400, R.drawable.ic_main_batch_normal,
                getResources().getString(R.string.batch_editor), Module.TTPTBATCH.name()));

        // 自拍相机
        icons.add(new MainButtonModel(R.id.btn_camera, 100, R.drawable.ic_main_camera_normal1_noword,
                getResources().getString(R.string.beauty_camera), Module.TTPTCAMERA.name()));

        // 返回之前需要做排序处理，从大到小
        Collections.sort(icons, new Comparator<MainButtonModel>() {
            @Override
            public int compare(MainButtonModel lhs, MainButtonModel rhs) {
                return rhs.priority - lhs.priority;
            }
        });
        LogUtils.d(TAG, "[getInitButtons] pager buttons = " + icons);
        return icons;
    }

    // 首页图标相关参数
    public static final int BASE_SCREEN_WIDTH = 375; // 标注图基于的屏幕宽度
    public static final double MAIN_BOTTOM_PANEL_MARGIN = 34.0 / BASE_SCREEN_WIDTH;
    public static final double MAIN_ICON_FLAG_TOP_MARGIN = 35.0 / BASE_SCREEN_WIDTH;
    public static final double MAIN_ICON_FLAG_RIGHT_MARGIN = 30.0 / BASE_SCREEN_WIDTH;
    public static final double MAIN_ICON_WIDTH = 147.0 / BASE_SCREEN_WIDTH;
    // OutRect 的值是和原来的值的差，147*3-375 = 66 是叠加的总宽，系统计算出来的每个叠加是 66 / 3 = 22；我们需要的叠加是 66 / 2 = 33，所以每一个的移动是 11
    public static final double MAIN_ICON_H_OVERLAP = 11.0 / BASE_SCREEN_WIDTH;

    public enum MainRatioType {
        ratio_long("long", 0f, 0.52f,
                375.0 / BASE_SCREEN_WIDTH, 10.0 / BASE_SCREEN_WIDTH,
                0.379, 12.0 / BASE_SCREEN_WIDTH),
        ratio_normal("normal", 0.52f, 0.62f,
                330.0 / BASE_SCREEN_WIDTH, -2.0 / BASE_SCREEN_WIDTH,
                0.381, 26.0 / BASE_SCREEN_WIDTH),
        ratio_short("short", 0.62f, 1f,
                240.0 / BASE_SCREEN_WIDTH, -2.0 / BASE_SCREEN_WIDTH,
                0.208, 30.0 / BASE_SCREEN_WIDTH);

        MainRatioType(String name, float ratioMin, float ratioMax, double bannerHeight, double pageIndicatorMargin, double iconsTopMargin, double icOverlapV) {
            this.name = name;
            this.ratioMin = ratioMin;
            this.ratioMax = ratioMax;
            this.bannerHeight = bannerHeight;
            this.pageIndicatorMargin = pageIndicatorMargin;
            this.iconsTopMargin = iconsTopMargin;
            this.icOverlapV = icOverlapV;
        }

        private String name;
        private float ratioMin;
        private float ratioMax;
        private double bannerHeight;    // banner 所占高度（和屏幕宽度的比）
        private double pageIndicatorMargin;  // 首页图标竖直方向重叠的大小（和屏幕宽度的比）
        private double iconsTopMargin;  // 首页图标整体距屏幕上边距（和屏幕高度的比）
        private double icOverlapV;  // 首页图标竖直方向重叠的大小（和屏幕宽度的比）

        // 默认是全屏的比例
        public static MainRatioType getDefault() {
            return ratio_normal;
        }

        public static MainRatioType getRatioTypeByRatio(double ratio) {
            if (ratio < ratio_long.ratioMax) {
                return ratio_long;
            } else if (ratio > ratio_short.ratioMin) {
                return ratio_short;
            }
            return getDefault();
        }

        public double getBannerHeight() {
            return bannerHeight;
        }

        public double getPageIndicatorMargin() {
            return pageIndicatorMargin;
        }

        public double getIconsTopMargin() {
            return iconsTopMargin;
        }

        public double getIcOverlapV() {
            return icOverlapV;
        }

        public String toString() {
            return super.toString() + "(" + ratioMin + ", " + ratioMax + ")";
        }
    }

    public void checkBannerHeight() {
        bannerHeight = (int) (mainRatioType.getBannerHeight() * mainWidth);
    }

    /**
     * 主模块按字母排序
     */
    public enum Module {
        TTPTFRONTPAGE, /**
         * 首页
         */
        TTPTBANNERSOCIAL, /**
         * 话题圈
         */
        TTPTBEAUTIFY, /**
         * 美化照片
         */
        TTPTBUCKLE, /**
         * 魔法抠图
         */
        TTPTCAMERA, /**
         * 自拍相机
         */
        TTPTGIF, /**
         * 表情包
         */
        TTPTEMOJI, /**
         * 萌偶
         */
        TTPTCOLLAGE, /**
         * 故事拼图
         */
        TTPTCOS, /**
         * 自然美妆
         */
        TTPTCOSFUN, /**
         * 疯狂变妆
         */
        TTPTFACE, /**
         * 人像美容
         */
        TTPTBATCH, /**
         * 趣味多图
         */
        TTPTMARKETING, /**
         * 社会化营销
         */
        TTPTMATERIALS, /**
         * 素材中心
         */
        TTPTSETTING, /**
         * 设置
         */
        TTPTVIDEOEDIT, /**
         * 动感MV
         */
        TTPTPLAYSTICKER, /**
         * 大头贴
         */
        TTPTMVEDIT, /**
         * 视频模板
         */
        TTPTFULLMARKETING, /**
         * 伪装为 native 的 webview
         */
        TTPTDLGMARKETING, /**
         * 伪装为 native dim dialog 的 webview
         */
        TTPTFEEDDETAIL, /**
         * Feed详情页
         */
        TTPTFEEDTAG, /**
         * TAG聚合页
         */
        TTPTWXMP  /** 启动小程序 */
    }
}
