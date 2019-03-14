package demo.li.opal.uidemo.nestedRecycler;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import NS_PITU_META_PROTOCOL.stMetaMaterialFeed;
import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.CommonUtils;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;

/**
 * Created by opalli on 8/1/18.
 * <p>
 * 所有 feeds 列表的管理类，任何需要加载列表的 Activity / 布局（可以没有头部、不用下拉刷新）皆可用
 * 使用时，在布局中加入一个 id 为 feeds_list 的 android.support.v7.widget.RecyclerView 即可
 * 此 controller 负责该 feeds 列表的数据拉取、刷新、点击事件等等，所有列表的新增操作都不要放在 Activity 中，应该放在这里
 * 如需更新 Activity，在 OnFeedsListResponse 中添加接口即可
 */

public class FeedsListController {
    private static final String TAG = FeedsListController.class.getName();

    public static final int FAKE_FEED_COUNT = 3;

    public static final int REQ_LOGIN = 101;
    public static final int REQ_SETTINGS = 102;
    public static final int REQ_SHARE = 103;
    public static final int REQ_FEED_LIST = 104;
    public static final int REQ_FEED_DETAIL = 105;

    private String mFeedListId;
    private ArrayList<stMetaMaterialFeed> mData = new ArrayList();

    private Activity mActivity;
    private OnFeedsListResponse mFeedsResponse;

    private FeedsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private boolean mFirstPage = true;
    private boolean mFinished = false;
    private boolean mFetching;
    private int mScreenHeight;
    private int mState = 0;

    private boolean mIsFirstResume = true;

    private int pageIndex = 1;

    public enum FeedListID {
        feed_list_hot("hot"),
        feed_list_my("my"),
        feed_list_tag("tag:");

        FeedListID(String name) {
            this.name = name;
        }

        private String name;

        public String getRequestID() {
            return getRequestID(null);
        }

        public String getRequestID(String tag) {
            switch (this) {
                case feed_list_tag:
                    return feed_list_tag.name + tag;
                default:
                    return name;
            }
        }

        // 默认是首页最热 feeds
        public static FeedListID getDefault() {
            return feed_list_hot;
        }
    }

    public FeedsListController(Activity activity, String feedListId, OnFeedsListResponse feedsListResponse) {
        mActivity = activity;
        mFeedListId = feedListId;
        mScreenHeight = DeviceUtils.getScreenHeight(mActivity);
        mFeedsResponse = feedsListResponse;

        init();
    }

    public void init() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.feeds_list);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, Utils.dip2px(mActivity, 30), Utils.dip2px(mActivity, 14), Utils.dip2px(mActivity, 14)));

        mAdapter = new FeedsAdapter(mActivity, mData);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnScrollListener(this);
        mRecyclerView.setNestedScrollingEnabled(true);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        restoreCachedFeeds();
        //fetchData(true);
    }

    private void restoreCachedFeeds() {
        if (!CommonUtils.isEmpty(mData)) {
            mData.clear();
        }
        pageIndex = 1;
        ArrayList<stMetaMaterialFeed> cachedData = (ArrayList<stMetaMaterialFeed>) FeedsProvider.restoreFeedsFromFile(pageIndex++ % 10);
        if (cachedData != null && !cachedData.isEmpty()) {
            mData.addAll(cachedData);
        } else {
            for (int i = 0; i < FAKE_FEED_COUNT; i++) {
                stMetaMaterialFeed fake = new stMetaMaterialFeed();
                fake.type = -1;
                mData.add(fake);
            }
            mFinished = true;
            mAdapter.setHasMore(!mFinished);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
        showFeedsList(true);
    }

    private void showFeedsList(boolean show) {
        if (mRecyclerView == null) {
            return;
        }
        if (show) {
            if (mRecyclerView.getVisibility() != View.VISIBLE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    public void onScrollToBottom() {
        LogUtils.i(TAG, "NestedScrollView 滑到底部 - RV.onScrollToBottom()");
        if (mRecyclerView == null || mFinished) {
            return;
        }
        fetchData(false);
    }

    public void clearList() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void refresh() {
        //mAdapter.resetData();
        //updateRecyclerView(0, 10);
        fetchData(true);    // 重置 attachInfo
    }

    public void fetchData(boolean refresh) {
        if (!mFetching) {
            mFetching = true;
            mFirstPage = refresh;

            ArrayList<stMetaMaterialFeed> feeds = (ArrayList<stMetaMaterialFeed>) FeedsProvider.restoreFeedsFromFile(pageIndex++ % 10);
            if (mFirstPage) {
                // 拉首屏数据
                mFirstPage = false;
                if (!CommonUtils.isEmpty(mData)) {
                    mData.clear();
                }

            }
            if (!CommonUtils.isEmpty(feeds)) {
                mData.addAll(feeds);
            }
            mFinished = FeedsProvider.isFinished(mFeedListId);
            LogUtils.i(TAG, "Feed list finished = " + mFinished);
            if (mAdapter != null) {
//                OskVideoController.getInstance().pause();
//                mAdapter.setPlayingItem(-1);
//                mAdapter.setLastPlayedItem(-1);
                mAdapter.notifyDataSetChanged();
                mAdapter.setHasMore(!mFinished);
            }
            LogUtils.d(TAG, "[onGet] mFirstPage = " + mFirstPage + "， mFinished = " + mFinished + ", materialItems = " + feeds);

            if (!CommonUtils.isEmpty(mData)) {
                LogUtils.d(TAG, "[onGet] 最终列表非空，显示列表");
                // 最终列表非空，显示列表
                showFeedsList(true);
                if (mFeedsResponse != null) {
                    mFeedsResponse.onFeedFetchSuccess(CommonUtils.isEmpty(mData), "");
                }
            } else {
                LogUtils.d(TAG, "[onGet] 最终列表为空，显示暂无内容的提示页面");
                // 最终列表为空，显示去收藏的提示页面
                if (mFeedsResponse != null) {
                    mFeedsResponse.onFeedFetchSuccess(CommonUtils.isEmpty(mData), "");
                }
            }
            mFetching = false;

        }
    }

    public void onRefreshUI(int pos, boolean isAddFav, int favCount) {
        if (mAdapter != null) {
            mAdapter.onRefreshUI(pos, isAddFav, favCount);
        }
    }

    public interface OnFeedsListResponse {
        void onFeedFetchStart(boolean isFirstPage);

        void onFeedFetchSuccess(boolean isEmpty, String msg);

        void onFeedFetchFail(int errorCode, String msg, boolean isFirstPage);
    }

}
