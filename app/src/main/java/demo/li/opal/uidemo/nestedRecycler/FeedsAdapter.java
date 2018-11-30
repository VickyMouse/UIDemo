package demo.li.opal.uidemo.nestedRecycler;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import NS_PITU_META_PROTOCOL.eMaterialFeedDisplay;
import NS_PITU_META_PROTOCOL.eMaterialFeedNumType;
import NS_PITU_META_PROTOCOL.stMetaMaterialFeed;
import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;
import demo.li.opal.uidemo.nestedRecycler.holder.AdFeedVH;
import demo.li.opal.uidemo.nestedRecycler.holder.FakeFeedVH;
import demo.li.opal.uidemo.nestedRecycler.holder.FeedsFootVH;
import demo.li.opal.uidemo.nestedRecycler.holder.NormalFeedVH;

/**
 * Created by opalli on 2018/7/18.
 */
public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = FeedsAdapter.class.getName();

    public static final int REQ_REFRESH_UI = 121;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_AD = 1;
    public static final int VIEW_TYPE_FAKE = 2;
    public static final int VIEW_TYPE_FOOTER = 10;

    private ArrayList<stMetaMaterialFeed> mData;
    private WeakReference<Activity> mActivityRef;

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示
    private int screenW = 0;
    private long lastClickTime = 0;
    static public long webviewWaitTime = 0;

    private String strCountFormat;

    private int playingItem = -1;
    private int lastPlayedItem = -1;

//    private FeedsCropVideoView.TextureSurfaceListener mSurfaceListener = new FeedsCropVideoView.TextureSurfaceListener() {
//        @Override
//        public void onSurfaceAvailable(FeedBaseVH viewHolder, FeedsCropVideoView v) {
//            int pos = viewHolder.getAdapterPosition();
//            if (pos == playingItem && pos >= 0 && mData.get(pos).isNormalOrAd() && mData.get(pos).isPreviewVideo()) {
//                LogUtils.d(TAG, "video edit: bind - onSurfaceAvailable(" + pos + ")");
//                if (!OnlineVideoController.getInstance().isPlaying()) {
//                    LogUtils.d(TAG, "video edit: bind - onSurfaceAvailable(" + pos + "); not playing");
//                    v.bindController(OskVideoController.getInstance());
//                    OnlineVideoController.getInstance().play();
//                    viewHolder.btnPlay.setVisibility(View.GONE);
//                    LogUtils.d(TAG, "video edit: bind - onSurfaceAvailable() - playVideo()");
//                }
//            }
//        }
//    };

    public FeedsAdapter(Activity activity, ArrayList<stMetaMaterialFeed> data) {
        mActivityRef = new WeakReference<>(activity);
        mData = data;
        screenW = DeviceUtils.getScreenWidth(activity);
        strCountFormat = activity.getResources().getString(R.string.feeds_reading_count);
    }

    public static final String EXTRA_KEY_ADD_FAV_FEED_POS = "add2FavFeedPos";

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return VIEW_TYPE_FOOTER;
        } else if (mData.get(position).isFeedAd()) {
            return VIEW_TYPE_AD;
        } else if (mData.get(position).isFakeFeed()) {
            return VIEW_TYPE_FAKE;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FOOTER:
                LogUtils.d(TAG, "[onCreateViewHolder] is VIEW_TYPE_FOOTER");
                return new FeedsFootVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds_footer, null));
            case VIEW_TYPE_AD:
                LogUtils.d(TAG, "[onCreateViewHolder] is VIEW_TYPE_AD");
                return new AdFeedVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_feed, null));
            case VIEW_TYPE_FAKE:
                return new FakeFeedVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fake_feed, null));
            case VIEW_TYPE_NORMAL:
            default:
                LogUtils.d(TAG, "[onCreateViewHolder] is VIEW_TYPE_NORMAL");
                return new NormalFeedVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_FOOTER:
                LogUtils.i(TAG, "[onBindViewHolder] pos = " + position + ", is VIEW_TYPE_FOOTER");
                final FeedsFootVH vh = (FeedsFootVH) viewHolder;
                // 只有获取数据为空时，hasMore 为 false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
                if (hasMore == true) {
                    // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个 footView
                    vh.loading.setVisibility(View.VISIBLE);
                    vh.tips.setVisibility(View.GONE);
//                    vh.tips.setText("正在加载更多...");
                    // 不隐藏 footView 提示
//                    fadeTips = false;
                } else {
                    // 如果没有更多数据了
                    vh.loading.setVisibility(View.GONE);
                    vh.tips.setVisibility(View.VISIBLE);
                }
                break;
            case VIEW_TYPE_FAKE:
                LogUtils.i(TAG, "[onBindViewHolder] pos = " + position + ", is VIEW_TYPE_FAKE");
                break;
            case VIEW_TYPE_AD:
                LogUtils.i(TAG, "[onBindViewHolder] pos = " + position + ", is VIEW_TYPE_AD");
                stMetaMaterialFeed ad = mData.get(position);
                final Activity ac = mActivityRef.get();
                final AdFeedVH adHolder = (AdFeedVH) viewHolder;

                float adRatio = ad.preview.image.origin_height * 1.0f / ad.preview.image.origin_width;
                int thumbHeight = (int) ((screenW - DeviceUtils.dip2px(ac, 28)) * adRatio);
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) adHolder.coverContainer.getLayoutParams();
                rlp.height = thumbHeight;
                DraweeController adController = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(ad.preview.image.image_base))
                        .setAutoPlayAnimations(true)
                        .build();
                adHolder.cover.setController(adController);
                adHolder.coverContainer.setClickable(false);

                adHolder.title.setText(ad.title);
                adHolder.desc.setText(ad.desc);
                if (!TextUtils.isEmpty(ad.preview.jump_url)) {
                    adHolder.btnGoTo.setVisibility(View.VISIBLE);
                    adHolder.btnGoTo.setTag(ad.preview.jump_url);
                } else {
                    adHolder.btnGoTo.setVisibility(View.GONE);
                }
                break;
            case VIEW_TYPE_NORMAL:
            default:
                LogUtils.i(TAG, "[onBindViewHolder] pos = " + position + ", is VIEW_TYPE_NORMAL");
                final stMetaMaterialFeed feed = mData.get(position);
                Activity activity = mActivityRef.get();
                final NormalFeedVH holder = (NormalFeedVH) viewHolder;
                //LogUtils.d(TAG, "[onBindViewHolder] pos = " + position + ", feed.isContentVideo() = " + feed.isContentVideo());
                //holder.videoView.setViewHolder(holder);
                if (feed.isContentVideo()) {
                    holder.btnIsVideo.setVisibility(View.VISIBLE);
                } else {
                    holder.btnIsVideo.setVisibility(View.GONE);
                }
                float ratio = feed.preview.image.origin_height * 1.0f / feed.preview.image.origin_width;
                //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.cover.getLayoutParams();
                int coverHeight = (int) ((screenW - DeviceUtils.dip2px(activity, 28)) * ratio);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.coverContainer.getLayoutParams();
                lp.height = coverHeight;
                //holder.cover.setImageURI(Uri.parse(material.preview.image.image_base));
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(feed.preview.image.image_base))
                        .setAutoPlayAnimations(true)
                        .build();
                holder.cover.setController(controller);
                //MemoryManager.getInstance().getTemplateThumbLoader().loadImage(path, viewHolder.videoThumb);
                //holder.videoView.setVisibility(View.GONE);

                holder.title.setText(feed.title);
//                FeedCardTagsAdapter horizontalListViewAdapter = new FeedCardTagsAdapter(activity, feed.tags);
//                holder.tags.setAdapter(horizontalListViewAdapter);
//                holder.tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Activity ac = mActivityRef.get();
//                        if (System.currentTimeMillis() - lastClickTime < 1000){
//                            return;
//                        }
//                        lastClickTime = System.currentTimeMillis();
//                        if (ac != null) {
//                            Intent intent = new Intent(ac, TagFeedsActivity.class);
//                            intent.putExtra(IntentUtils.KEY_FEEDS_TAG_ID, feed.tags.get(position).name);
////                        intent.putExtra(IntentUtils.KEY_FEEDS_TAG_COUNT, material.tags.get(position).numbers.get(eMaterialFeedTagNumType._eMaterialFeedTagNumTypeFeeds));
//                            ac.startActivity(intent);
//                        }
//                    }
//                });

                Integer favStatus = feed.display_ui.get(eMaterialFeedDisplay._eMaterialFeedDisplayStar);
                favStatus = favStatus == null ? 0 : favStatus;
                if (Integer.valueOf(0).equals(favStatus)) {
                    holder.favImg.setImageResource(R.drawable.ic_feed_fav_count);
                } else {
                    holder.favImg.setImageResource(R.drawable.ic_feed_favored);
                }

                Integer number = feed.numbers.get(eMaterialFeedNumType._eMaterialFeedNumTypeStar);
                number = number == null ? 0 : number;
                holder.favCount.setText(String.valueOf(number < 0 ? 0 : number));
                //number = feed.numbers.get(eMaterialFeedNumType._eMaterialFeedNumTypeShare);
                //holder.shareCount.setText(String.valueOf(number < 0 ? 0 : number));
                number = feed.numbers.get(eMaterialFeedNumType._eMaterialFeedNumTypeView);
                number = number == null ? 0 : number;
                holder.readCount.setText("500+");

                if (position == 0) {
                    holder.rightDecor.setVisibility(View.VISIBLE);
                    holder.leftDecor.setVisibility(View.GONE);
                } else if (position == 1) {
                    holder.leftDecor.setVisibility(View.VISIBLE);
                    holder.rightDecor.setVisibility(View.GONE);
                } else {
                    holder.rightDecor.setVisibility(View.GONE);
                    holder.leftDecor.setVisibility(View.GONE);
                }

                holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        int dataCount = mData != null ? mData.size() : 0;
        return dataCount == 0 ? 0 : dataCount + 1;
    }

    public void onRefreshUI(int pos, boolean isAddFav, int favCount) {
        if (favCount >= 0) {
            stMetaMaterialFeed feed = mData.get(pos);
            if (feed != null) {
                feed.display_ui.put(eMaterialFeedDisplay._eMaterialFeedDisplayStar, isAddFav ? 1 : 0);
                feed.numbers.put(eMaterialFeedNumType._eMaterialFeedNumTypeStar, favCount);
                mData.set(pos, feed);
                notifyItemChanged(pos);
            }
        }
    }

    public boolean isFooter(int position) {
        return position == getItemCount() - 1;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}
