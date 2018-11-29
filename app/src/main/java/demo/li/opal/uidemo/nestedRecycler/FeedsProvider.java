package demo.li.opal.uidemo.nestedRecycler;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import NS_PITU_CLIENT_INTERFACE_WITH_LOGIN.stGetMaterialFeedListRsp;
import NS_PITU_META_PROTOCOL.stMetaMaterialFeed;
import demo.li.opal.uidemo.GlobalContext;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.FileUtils;
import demo.li.opal.uidemo.Utils.GsonUtils;
import demo.li.opal.uidemo.Utils.LogUtils;
import demo.li.opal.uidemo.Utils.PrefsUtils;

/**
 * Created by opalli on 18/7/19.
 */
public class FeedsProvider {
    public static final String TAG = FeedsProvider.class.getSimpleName();

    private static String mFirstFeedId = "";
    private static HashMap<String, String> feedsAttachInfo = new HashMap<>();
    private static HashMap<String, Boolean> feedsFinishedInfo = new HashMap<>();

    // 素材信息要分类存储，key 为分类名称
    public static void storeFeeds(String categoryId, ArrayList<stMetaMaterialFeed> materials) {
            String jString = GsonUtils.objList2Json(materials);
            LogUtils.v(TAG, "[storeFeeds] storeFeeds " + jString);
            PrefsUtils.getFeedsPrefs().edit().putString(categoryId, jString).apply();
    }

    // 素材信息要分类存储，key 为分类名称
    public static void storeFeeds2File(String page, ArrayList<stMetaMaterialFeed> materials) {
        String jString = GsonUtils.objList2Json(materials);
        LogUtils.v(TAG, "[storeFeeds] storeFeeds " + jString);
        FileUtils.save(new File(DeviceUtils.getExternalFilesDir(GlobalContext.getContext(), "feeds") + "/feedsList_" + page + ".txt"), jString);
    }

    // 某个分类下的素材信息
    public static List<stMetaMaterialFeed> restoreFeeds(String categoryId) {
        String jString = PrefsUtils.getFeedsPrefs().getString(categoryId, "");
        if (TextUtils.isEmpty(jString)) {
            return null;
        } else {
            return GsonUtils.json2ObjList(jString, stMetaMaterialFeed.class);
        }
    }

    // 某个分类下的素材信息
    public static List<stMetaMaterialFeed> restoreFeedsFromFile(int pageIndex) {
        String jString = FileUtils.readTxtFile(GlobalContext.getContext(), FileUtils.RES_PREFIX_ASSETS + "feeds/feedsList_" + pageIndex + ".json");
        if (TextUtils.isEmpty(jString)) {
            jString = FileUtils.readTxtFile(GlobalContext.getContext(), FileUtils.RES_PREFIX_ASSETS + "feeds/feedsList_1.json");
        }
        return GsonUtils.json2ObjList(jString, stMetaMaterialFeed.class);
    }

    //从下载的数据中解析某一类素材
    public static ArrayList<stMetaMaterialFeed> parseFeeds(stGetMaterialFeedListRsp rsp) {
        if (rsp == null) {
            return null;
        }
        return rsp.feedlist;
    }

    //保存当前各子分类的加载信息
    //TODO by jakieliu, 这里需要考虑这些信息的清空时机
    public static void saveFeedsInfo(String categoryId, String attachInfo, boolean isFinished) {
        if (feedsAttachInfo != null && feedsFinishedInfo != null) {
            feedsAttachInfo.put(categoryId, attachInfo);
            feedsFinishedInfo.put(categoryId, isFinished);
        }
    }

    public static String getAttachInfo(String categoryId) {
        if (feedsAttachInfo == null) {
            return null;
        }
        return feedsAttachInfo.get(categoryId);
    }

    public static boolean isFinished(String feedsListId) {
        if (feedsFinishedInfo != null && feedsFinishedInfo.containsKey(feedsListId)) {
            return feedsFinishedInfo.get(feedsListId);
        }
        return false;
    }

    public static void saveFirstFeedId(String feedId) {
        mFirstFeedId = feedId;
        //PrefsUtils.getFeedsPrefs().edit().putString(PrefsUtils.PREFS_KEY_FEEDS_FIRST_ID, feedId).apply();
    }

    public static String getFirstFeedId() {
        return mFirstFeedId;
        //return PrefsUtils.getFeedsPrefs().getString(PrefsUtils.PREFS_KEY_FEEDS_FIRST_ID, "");
    }
}
