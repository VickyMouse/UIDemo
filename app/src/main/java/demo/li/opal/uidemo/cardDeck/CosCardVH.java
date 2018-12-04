package demo.li.opal.uidemo.cardDeck;

import android.view.View;
import android.widget.TextView;

import demo.li.opal.uidemo.R;

/**
 * Created by opalli on 2018/12/01
 */

public class CosCardVH extends CardVH<CosCardItemData> {

    TextView userName;

    public CosCardVH(View view) {
        super(view);
        userName = view.findViewById(R.id.card_user_name);
    }

    @Override
    public void bindData(CosCardItemData itemData) {
        super.bindData(itemData);
        userName.setText(itemData.getDescription());
    }
}
