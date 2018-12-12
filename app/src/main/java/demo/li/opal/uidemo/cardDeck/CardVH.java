package demo.li.opal.uidemo.cardDeck;

import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.FileUtils;

/**
 * Created by opalli on 2018/12/01
 */

public class CardVH<T extends CardItemData> {

    public SimpleDraweeView cardImage;

    public CardVH(View view) {
        cardImage = view.findViewById(R.id.card_image_view);
    }

    public void bindData(T itemData) {
//            Glide.with(CardDeckActivity.this).load(itemData.imagePath).into(cardImage);

        ResizeOptions options = new ResizeOptions(cardImage.getWidth(), cardImage.getHeight());
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(FileUtils.getUri(itemData.getImagePath()))
                .setResizeOptions(options)
                .build();

        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(cardImage.getController()).build();
        cardImage.setController(controller);
    }
}
