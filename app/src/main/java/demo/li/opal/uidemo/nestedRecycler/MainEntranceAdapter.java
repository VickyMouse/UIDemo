package demo.li.opal.uidemo.nestedRecycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.GlideUtil;
import demo.li.opal.uidemo.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 首页中间功能模块的数据适配器
 * <p>
 * Created by panda on 2018/8/2
 **/
public class MainEntranceAdapter extends RecyclerView.Adapter<MainEntranceAdapter.Holder> {

    private Context mContext;
    private ArrayList<ItemModel> mDatas, mLocalDatas = new ArrayList<>();
    private MergedDataCallback mergedDataCallback;
    private int mItemWidth;
    private int mItemHeight;

    /**
     * 资源的前缀，为了便于coding
     */
    private static final String sPrefixTitlesColorName = "ops_title_color_";
    private static final String sPrefixSubTitlesColorName = "ops_subtitle_color_";
    private static final String sPrefixBackgroundResName = "main_default_bg_";
    private static final String sPrefixForgroundResName = "main_default_icon_";

    /**
     * UI上的比例值
     */
    private static final float sItemWidthRadio = 0.373f;
    /**
     * 这个radio是相对于width的
     */
    private static final float sItemHeightRadio = 1.69f;

    public MainEntranceAdapter(Context mContext, MergedDataCallback mergedDataCallback) {
        this.mContext = mContext;
        this.mDatas = buildDefaultDatas();
        this.mLocalDatas.addAll(this.mDatas);
        this.mergedDataCallback = mergedDataCallback;
        // 大小只需要计算一次即可
        mItemWidth = (DeviceUtils.getScreenWidth(mContext) - 3 * mContext.getResources().getDimensionPixelSize(R.dimen.camera_bottom_button_text_btm_margin)) / 2;
        mItemHeight = (int) (mItemWidth / sItemHeightRadio);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.main_center_ops_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ViewGroup.LayoutParams params = holder.mRoot.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.mRoot.setLayoutParams(params);
        ItemModel model = mDatas.get(position);
        // 解决乱序问题，通过tag保证唯一性
        holder.mRoot.setTag(model.id);
        holder.mTitle.setText(model.itemTitle);
        holder.mBg.setImageResource(model.itemBgRes);
        holder.mFg.setImageResource(model.itemFgRes);
        holder.mTitle.setTextColor(mContext.getResources().getColor(model.itemTitleColorRes));
        int color = mContext.getResources().getColor(model.itemSubTitleColorRes);
        holder.mSubTitle.setTextColor(color);
        holder.mSubTitle.setText(model.itemSubTitle);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private View mRoot;
        private TextView mTitle;
        private TextView mSubTitle;
        private ImageView mFg;
        private ImageView mBg;

        // 下面2个view是为了做动画而设置
        private TextView mSubTitle2;
        private ImageView mFg2;
        private int mTouchSlop;

        AnimatorSet set = new AnimatorSet();

        Holder(View itemView) {
            super(itemView);
            mRoot = itemView;
            mRoot.setOnTouchListener(this);
            mTitle = (TextView) mRoot.findViewById(R.id.item_title);
            mSubTitle = (TextView) mRoot.findViewById(R.id.item_subtitle);
            mBg = (ImageView) mRoot.findViewById(R.id.item_bg);
            mFg = (ImageView) mRoot.findViewById(R.id.item_fg);
            mSubTitle2 = (TextView) mRoot.findViewById(R.id.item_subtitle2);
            mFg2 = (ImageView) mRoot.findViewById(R.id.item_fg2);
            mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }

        boolean isMove = false;
        private float mDownX;
        private float mDownY;
        private float mRawY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mRawY = event.getRawY();
                    doTouchAnimator(set, mRoot, 0.92f);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(event.getX() - mDownX) > mTouchSlop || Math.abs(event.getY() - mDownY) > mTouchSlop) {
                        isMove = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    doTouchAnimator(set, mRoot, 1.0f);
                    if (isMove) {
                        break;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    doTouchAnimator(set, mRoot, 1.0f);
                    break;
            }
            return true;
        }
    }

    /**
     * 艹蛋的需求X，如果后端配置是是default，则需要通过id查找到初始的背景或前景
     *
     * @param itemId
     * @param isBg   是否是背景图片
     * @return
     */
    private Bitmap queryDefaultImage(String itemId, boolean isBg) {
        for (ItemModel model : mLocalDatas) {
            if (model.id.equalsIgnoreCase(itemId)) {
                if (isBg) {
                    return BitmapFactory.decodeResource(mContext.getResources(), model.itemBgRes);
                } else {
                    return BitmapFactory.decodeResource(mContext.getResources(), model.itemFgRes);
                }
            }
        }
        return null;
    }

    /**
     * 防止出现图片加载速度不一致导致的空白，等待图片都ready才进行ui设置
     *
     * @param model
     */
    private void loadNewItem(final Holder holder, final ItemModel model, final ImageLoadCallback imageLoadCallback) {
        final Bitmap[] bgBitmap = {null};
        final Bitmap[] fgBitmap = {null};

        Observable<Boolean> bgImg = Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) {
                if (model.itemBgUri.toLowerCase().equals("default")) {
                    bgBitmap[0] = queryDefaultImage(model.id, true);
                    e.onComplete();
                    return;
                }
                GlideUtil.asynLoadImageView(mContext, model.itemBgUri, new GlideUtil.LoadFinishedCallback() {
                    @Override
                    public void onLoaded(Bitmap bitmap) {
                        bgBitmap[0] = bitmap;
                        e.onComplete();
                        Log.d("111", "bgImg.............");
                    }
                });
            }
        });
        Observable<Boolean> fgImg = Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) {
                if (model.itemFgUri.toLowerCase().equals("default")) {
                    fgBitmap[0] = queryDefaultImage(model.id, false);
                    e.onComplete();
                    return;
                }
                GlideUtil.asynLoadImageView(mContext, model.itemFgUri, new GlideUtil.LoadFinishedCallback() {
                    @Override
                    public void onLoaded(Bitmap bitmap) {
                        fgBitmap[0] = bitmap;
                        e.onComplete();
                        Log.d("111", "fgImg.............");
                    }
                });
            }
        });
        Observable.concat(bgImg, fgImg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("111", "finished.............");

                        if (imageLoadCallback != null) {
                            imageLoadCallback.onRxFinished(model, holder, bgBitmap[0], fgBitmap[0]);
                        }
                    }
                });
    }

    private void loadLast(Holder holder, ItemModel lastModel) {
        if (lastModel == null) {
            holder.mTitle.setText("");
            holder.mSubTitle.setText("");
            holder.mFg.setImageBitmap(null);
            holder.mBg.setImageBitmap(null);
            return;
        }
        holder.mTitle.setText(lastModel.itemTitle);
        if (!TextUtils.isEmpty(lastModel.itemTitleColor)) {
            try {
                holder.mTitle.setTextColor(Color.parseColor(lastModel.itemTitleColor));
            } catch (Throwable e) {
            }
        } else if (lastModel.itemTitleColorRes > 0) {
            holder.mTitle.setTextColor(mContext.getResources().getColor(lastModel.itemTitleColorRes));
        }
        holder.mSubTitle.setText(lastModel.itemSubTitle);
        if (!TextUtils.isEmpty(lastModel.itemSubTitleColor)) {
            try {
                holder.mSubTitle.setTextColor(Color.parseColor(lastModel.itemSubTitleColor));
            } catch (Throwable e) {
            }
        } else if (lastModel.itemSubTitleColorRes > 0) {
            holder.mSubTitle.setTextColor(mContext.getResources().getColor(lastModel.itemSubTitleColorRes));
        }
        if (!TextUtils.isEmpty(lastModel.itemFgUri)) {
            // 这里假设了这个图片加载会比较快，实际上也是这样的
            GlideUtil.asynLoadImageView(mContext, holder.mFg, lastModel.itemFgUri);
        } else if (lastModel.itemFgRes > 0) {
            holder.mFg.setImageResource(lastModel.itemFgRes);
        }
        if (!TextUtils.isEmpty(lastModel.itemBgUri)) {
            // 这里假设了这个图片加载会比较快，实际上也是这样的
            GlideUtil.asynLoadImageView(mContext, holder.mBg, lastModel.itemBgUri);
        } else if (lastModel.itemBgRes > 0) {
            holder.mBg.setImageResource(lastModel.itemBgRes);
        }
    }

    /**
     * @return
     */
    public int thisRecycleViewHeight() {
//        if (mDatas.isEmpty()) {
//            return 0;
//        } else if (mDatas.size() == 1) {
//            return mItemHeight;
//        } else {
        return mItemHeight * 2 + mContext.getResources().getDimensionPixelSize(R.dimen.camera_bottom_button_text_btm_margin) * 2;
//        }
    }

    /**
     * 执行按下的动画
     *
     * @param set
     * @param root
     * @param scale
     */
    private void doTouchAnimator(AnimatorSet set, View root, float scale) {
        if (set.isRunning()) {
            set.cancel();
        }
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(root, "scaleX", root.getScaleX(), scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(root, "scaleY", root.getScaleY(), scale);
        set.setDuration(150);
        set.playTogether(scaleX, scaleY);
        set.start();
    }

    /**
     * 素材替换的动画
     */
    public void doExchangeAnimator(final TextView exitText, final ImageView exitImage, final TextView enterText, final ImageView enterImage) {
        AnimatorSet set = new AnimatorSet();

        // 保存住原始属性值，为了可以持续的进行素材替换
        final float x1 = exitText.getX();
        final float x2 = enterText.getX();
        final float x3 = exitImage.getX();
        final float x4 = enterImage.getX();

        // 原始的view退下
        ObjectAnimator textTransX = ObjectAnimator.ofFloat(exitText, "x", -60);
        ObjectAnimator textAlpha = ObjectAnimator.ofFloat(exitText, "alpha", 0f);
        ObjectAnimator imageTransX = ObjectAnimator.ofFloat(exitImage, "x", -60);
        ObjectAnimator imageAlpha = ObjectAnimator.ofFloat(exitImage, "alpha", 0f);
        // 新的view从右边进入
        enterImage.setX(120);
        ObjectAnimator textTransX2 = ObjectAnimator.ofFloat(enterText, "x", x1);
        ObjectAnimator textAlpha2 = ObjectAnimator.ofFloat(enterText, "alpha", 1f);
        ObjectAnimator imageTransX2 = ObjectAnimator.ofFloat(enterImage, "x", x3);
        ObjectAnimator imageAlpha2 = ObjectAnimator.ofFloat(enterImage, "alpha", 1f);

        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(textTransX, textAlpha, imageTransX, imageAlpha, textTransX2, textAlpha2, imageTransX2, imageAlpha2);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 还原属性
                exitText.setX(x1);
                exitText.setAlpha(1.0f);
                enterText.setX(x2);
                enterText.setAlpha(0.0f);
                exitText.setText(enterText.getText());
                exitText.setTextColor(enterText.getCurrentTextColor());
                exitImage.setX(x3);
                exitImage.setAlpha(1.0f);
                enterImage.setX(x4);
                enterImage.setAlpha(0.0f);
                exitImage.setImageDrawable(enterImage.getDrawable());
            }
        });
        set.start();
    }

    public void setDatas(ArrayList<ItemModel> datas) {
        this.mDatas = datas;
    }

    /**
     * 构建缺省的功能入口数据
     *
     * @return
     */
    private ArrayList<ItemModel> buildDefaultDatas() {
        ArrayList<ItemModel> datas = new ArrayList<>();
        String[] titles = mContext.getResources().getStringArray(R.array.main_func_ops_titles);
        for (int i = 0; i < titles.length; i++) {
            // 这里的结构是title#identify id#priority
            String[] items = titles[i].split("#");
            ItemModel itemModel = new ItemModel();
            String lowId = items[1].toLowerCase();
            itemModel.id = items[1];
            itemModel.priority = Integer.parseInt(items[2]);
            itemModel.itemTitle = items[0];
            itemModel.itemBgRes = getDrawableId(mContext, sPrefixBackgroundResName + lowId);
            itemModel.itemFgRes = getDrawableId(mContext, sPrefixForgroundResName + lowId);
            itemModel.itemTitleColorRes = getColorId(mContext, sPrefixTitlesColorName + lowId);
            itemModel.itemSubTitleColorRes = getColorId(mContext, sPrefixSubTitlesColorName + lowId);
            datas.add(itemModel);
        }
        return datas;
    }

    private int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }

    private int getDrawableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    private int getId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    /**
     * 单项的数据结构
     */
    public static class ItemModel {
        private String id;
        /**
         * 有限级，决定了排列的顺序，越大越靠前
         */
        private int priority;

        /**
         * 缺省背景图片的资源
         */
        private int itemBgRes;
        /**
         * 服务端下发背景图片的资源
         */
        private String itemBgUri;
        /**
         * 缺省前景图片的资源
         */
        private int itemFgRes;
        /**
         * 服务端下发前图片的资源
         */
        private String itemFgUri;
        /**
         * 一级title的资源，不可变
         */
        private String itemTitle;
        /**
         * 缺省一级itle颜色的资源
         */
        private int itemTitleColorRes;
        /**
         * 服务端下发一级itle颜色的资源
         */
        private String itemTitleColor;
        /**
         * 一级title的资源，可变，缺省的情况下是没有的，服务端下发不为空的时候显示
         */
        private String itemSubTitle;
        /**
         * 二级title颜色的资源，不可变
         */
        private int itemSubTitleColorRes;
        /**
         * 服务端下发二级title颜色的资源
         */
        private String itemSubTitleColor;

        ItemModel() {
        }
    }

    /**
     * GridLayoutManager的边距
     */
    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        /**
         * 具体边距值，单位px
         */
        private int space;
        /**
         * 单行的item的个数
         */
        private int lineCounts;
        /**
         * Grid单行个数
         */
        private static final int sGridColumns = 6;

        public SpaceItemDecoration(int space) {
            this.space = space;
            this.lineCounts = sGridColumns;
        }

        public SpaceItemDecoration(int space, int lineCounts) {
            this.space = space;
            this.lineCounts = lineCounts;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildLayoutPosition(view);

            if (position % 4 == 0 || position % 4 == 1) {
                outRect.bottom = space / 2;
            } else {
                outRect.top = space / 2;
            }

            if (position % 2 == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            }

            if (position % 2 == 1) {
                outRect.left = space / 2;
                outRect.right = space;
            }
        }
    }

    /**
     * 更艹蛋的需求。。。
     */
    static class DelayedAnimator implements Delayed {
        private long duration = 0;
        private DelayBean bean;

        public DelayedAnimator(long duration, DelayBean bean) {
            this.duration = System.currentTimeMillis() + duration;
            this.bean = bean;
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.duration - ((DelayedAnimator) o).getDuration());
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = duration - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        public long getDuration() {
            return duration;
        }

        public static class DelayBean {
            ItemModel model;
            Holder h;
            Bitmap bgBitmap;
            Bitmap fgBitmap;

            public DelayBean(ItemModel model, Holder h, Bitmap bgBitmap, Bitmap fgBitmap) {
                this.model = model;
                this.h = h;
                this.bgBitmap = bgBitmap;
                this.fgBitmap = fgBitmap;
            }
        }
    }

    public interface MergedDataCallback {
        void onLoaded();
    }

    public interface ImageLoadCallback {
        void onRxFinished(ItemModel model, Holder holder, Bitmap bgBitmap, Bitmap fgBitmap);
    }
}