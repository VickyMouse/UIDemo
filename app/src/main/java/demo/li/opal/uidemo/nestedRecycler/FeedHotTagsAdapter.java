package demo.li.opal.uidemo.nestedRecycler;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import demo.li.opal.uidemo.R;

public class FeedHotTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = FeedHotTagsAdapter.class.getSimpleName();
    private ArrayList<String> tags = new ArrayList() {{
        add("自拍贴纸");
        add("最强 Pose 机");
        add("滤镜");
        add("拍照小技巧");
        add("调色教室");
        add("美妆");
        add("最 in 玩法");
        add("疯狂变脸");
        add("照骗大法");
    }};

    private WeakReference<Activity> mActivityRef;

    public FeedHotTagsAdapter(Activity ctx) {
        mActivityRef = new WeakReference<>(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotTagsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_hot_tag, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final HotTagsVH holder = (HotTagsVH) viewHolder;
        holder.init();
        final String tag = tags.get(position);
        holder.tv.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    private class HotTagsVH extends RecyclerView.ViewHolder {
        public TextView tv;

        HotTagsVH(View view) {
            super(view);
        }

        public void init() {
            tv = (TextView) itemView.findViewById(R.id.tag_name);
        }
    }
}



