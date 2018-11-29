package demo.li.opal.uidemo.nestedRecycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import demo.li.opal.uidemo.R;

public class NormalFeedVH extends FeedBaseVH {

    //public HorizontalListView tags;
    public ImageView rightDecor;
    public ImageView leftDecor;
    public View favBtn;
    public ImageView addFavAnim;
    public SimpleDraweeView favImg;
    public TextView favCount;
    //public TextView shareCount;
    public TextView readCount;

    public ImageView btnIsVideo;

    public NormalFeedVH(View itemView) {
        super(itemView);
        //tags = (HorizontalListView) itemView.findViewById(R.id.feed_tags);
        rightDecor = (ImageView) itemView.findViewById(R.id.right_decor);
        leftDecor = (ImageView) itemView.findViewById(R.id.left_decor);
        favBtn = itemView.findViewById(R.id.fav_container);
        addFavAnim = (ImageView) favBtn.findViewById(R.id.add_fav_flash);
        favImg = (SimpleDraweeView) favBtn.findViewById(R.id.fav_img);

        favCount = (TextView) itemView.findViewById(R.id.fav_count);
        //shareCount = (TextView) itemView.findViewById(R.id.share_count);
        readCount = (TextView) itemView.findViewById(R.id.read_count);

        btnIsVideo = (ImageView) itemView.findViewById(R.id.btn_is_video);


    }
}
