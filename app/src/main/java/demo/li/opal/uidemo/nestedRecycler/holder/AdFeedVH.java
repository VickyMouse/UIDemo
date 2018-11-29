package demo.li.opal.uidemo.nestedRecycler.holder;

import android.view.View;
import android.widget.TextView;

import demo.li.opal.uidemo.R;

public class AdFeedVH extends FeedBaseVH {

    public TextView desc;
    public TextView btnGoTo;
    public View textContainer;

    public AdFeedVH(View itemView) {
        super(itemView);
        textContainer = itemView.findViewById(R.id.text_container);
        desc = (TextView) itemView.findViewById(R.id.feed_desc);
        btnGoTo = (TextView) itemView.findViewById(R.id.btn_go_to);
    }
}
