package demo.li.opal.uidemo.cardDeck;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.FileUtils;

/**
 * Created by opalli on 2018/12/01
 */

public class CosCardVH extends CardVH<CosCardItemData> {

    private int cornerDecorPath[] = {
            R.drawable.cardpat_1,
            R.drawable.cardpat_2,
            R.drawable.cardpat_3,
            R.drawable.cardpat_4
    };

    public TextView description;
    public ImageView topLeftDecor, rightBottomDecor;
    public ImageView discardHint, saveHint;

    public CosCardVH(View view) {
        super(view);
        description = view.findViewById(R.id.card_description);
        topLeftDecor = view.findViewById(R.id.top_left_decor);
        rightBottomDecor = view.findViewById(R.id.right_bottom_decor);
        discardHint = view.findViewById(R.id.discard_hint);
        saveHint = view.findViewById(R.id.save_hint);
    }

    @Override
    public void bindData(CosCardItemData itemData) {
//        super.bindData(itemData); // 无需圆角图片
        cardImage.setImageURI(FileUtils.getUri(itemData.getImagePath()));

        int decorIndex = itemData.index % cornerDecorPath.length;
        topLeftDecor.setImageResource(cornerDecorPath[decorIndex]);
        rightBottomDecor.setImageResource(cornerDecorPath[decorIndex]);
        description.setText(itemData.description);

        discardHint.setImageAlpha(0);
        saveHint.setImageAlpha(0);
    }
}
