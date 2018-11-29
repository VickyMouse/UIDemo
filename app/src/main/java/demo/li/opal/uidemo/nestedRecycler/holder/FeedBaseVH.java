package demo.li.opal.uidemo.nestedRecycler.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import demo.li.opal.uidemo.R;

public class FeedBaseVH extends RecyclerView.ViewHolder {

    public View coverContainer;
    public SimpleDraweeView cover;
    public TextView title;

    public FeedBaseVH(View itemView) {
        super(itemView);
        coverContainer = itemView.findViewById(R.id.cover_container);
        cover = (SimpleDraweeView) coverContainer.findViewById(R.id.feed_thumb);
        title = (TextView) itemView.findViewById(R.id.feed_title);
    }
}
