package demo.li.opal.uidemo;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.LogUtils;

/**
 * Version 1.0
 * <p/>
 * Date: 2014-04-24 14:46
 * Author: yonnielu
 * <p/>
 * Copyright © 1998-2014 Tencent Technology (Shenzhen) Company Ltd.
 */

/**
 * 首页按钮的布局 VH
 */
public class MainButtonVH extends RecyclerView.ViewHolder {
    private static final String TAG = MainButtonVH.class.getSimpleName();

    public TextView mLabel;
    public SimpleDraweeView mButton;
    public ImageView mIndicator;
    public MainButtonModel mModel;

    public MainButtonVH(View itemView) {
        super(itemView);
        mButton = itemView.findViewById(R.id.main_button_image);
        mLabel = itemView.findViewById(R.id.main_button_text);
        mIndicator = itemView.findViewById(R.id.main_button_indicator);
    }

    public void setModel(MainButtonModel model) {
        LogUtils.v(TAG, "setModel() - " + model);
        mModel = model;
        if (mModel != null) {
            mButton.setBackgroundDrawable(null);
            String path = (mModel.image instanceof Integer ? "res:///" : "") + mModel.image;
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(path))
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new ControllerListener<ImageInfo>() {
                        @Override
                        public void onSubmit(String id, Object callerContext) {
                            LogUtils.d(TAG, "DraweeController - onSubmit(" + id + ")");
                        }

                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            LogUtils.d(TAG, "DraweeController - onFinalImageSet(" + id + ")");
                        }

                        @Override
                        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                            LogUtils.d(TAG, "DraweeController - onIntermediateImageSet(" + id + ")");
                        }

                        @Override
                        public void onIntermediateImageFailed(String id, Throwable throwable) {
                            LogUtils.d(TAG, "DraweeController - onIntermediateImageFailed(" + id + "): " + throwable.getMessage());
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            LogUtils.d(TAG, "DraweeController - onFailure(" + id + "): " + throwable.getMessage());
                        }

                        @Override
                        public void onRelease(String id) {
                            LogUtils.d(TAG, "DraweeController - onRelease(" + id + ")");
                        }
                    })
                    .build();
            mButton.setController(controller);
            LogUtils.d(TAG, "[setModel] mModel.label = " + mModel.label);
            mLabel.setText(mModel.label);
        }
    }

    public void updateItemSize() {
        // 计算并设置每个 item 的大小
        int sw = DeviceUtils.getScreenWidth(GlobalContext.getContext());
        int itemWidth = (int) (sw * GridActivity.MAIN_ICON_WIDTH);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        itemView.setLayoutParams(lp);
    }
}
