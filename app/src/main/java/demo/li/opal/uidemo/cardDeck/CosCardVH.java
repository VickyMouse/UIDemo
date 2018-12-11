package demo.li.opal.uidemo.cardDeck;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

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

    TextView description;
    ImageView topLeftDecor, rightBottomDecor;

    public CosCardVH(View view) {
        super(view);
        description = view.findViewById(R.id.card_description);
        topLeftDecor = view.findViewById(R.id.top_left_decor);
        rightBottomDecor = view.findViewById(R.id.right_bottom_decor);
    }

    @Override
    public void bindData(CosCardItemData itemData) {
//        super.bindData(itemData); // 无需圆角图片
        if (TextUtils.isEmpty(itemData.imagePath)) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(FileUtils.getUriByRes(R.drawable.anim_daily_cos_loading))
                    .setAutoPlayAnimations(true)
                    .build();
            cardImage.setController(controller);
        } else {
            cardImage.setImageURI(FileUtils.getUri(itemData.getImagePath()));
        }
        int decorIndex = itemData.index % cornerDecorPath.length;
        topLeftDecor.setImageResource(cornerDecorPath[decorIndex]);
        rightBottomDecor.setImageResource(cornerDecorPath[decorIndex]);
        description.setText(itemData.getDescription());

    }
}
