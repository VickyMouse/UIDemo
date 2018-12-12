package demo.li.opal.uidemo.cardDeck;

import android.view.View;
import android.widget.TextView;

import demo.li.opal.uidemo.R;

/**
 * Created by opalli on 2018/12/01
 */

public class NormalCardVH extends CardVH<NormalCardItemData> {

    public View maskView;
    public TextView userName;
    public TextView picCount;
    public TextView likeCount;

    public NormalCardVH(View view) {
        super(view);
        maskView = view.findViewById(R.id.maskView);
        userName = view.findViewById(R.id.card_user_name);
        picCount = view.findViewById(R.id.card_pic_num);
        likeCount = view.findViewById(R.id.card_like);
    }

    @Override
    public void bindData(NormalCardItemData itemData) {
        super.bindData(itemData);
        userName.setText(itemData.getUserName());
        picCount.setText(itemData.getImageNum() + "");
        likeCount.setText(itemData.getLikeNum() + "");
    }
}
