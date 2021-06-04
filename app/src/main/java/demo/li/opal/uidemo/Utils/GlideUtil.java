//package demo.li.opal.uidemo.Utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.load.resource.gif.GifDrawable;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.target.Target;
//import com.bumptech.glide.request.transition.Transition;
//
//public class GlideUtil {
//
//    /**
//     * 不处理failed loading
//     *
//     * @param context
//     * @param imageView
//     * @param imgUrl
//     */
//    public static void asynLoadImageView(Context context, final ImageView imageView, String imgUrl) {
//        asynLoadImageView(context, imageView, imgUrl, null);
//    }
//
//    /**
//     * 异步加载图片，只有下载完成了才回去替换ImageView
//     *
//     * @param context
//     * @param imageView
//     * @param imgUrl
//     */
//    @SuppressLint("CheckResult")
//    public static void asynLoadImageView(Context context, final ImageView imageView, String imgUrl, final FailedLoadCallback failedLoadCallback) {
//        if (TextUtils.isEmpty(imgUrl)) {
//            return;
//        }
//
//        if (imgUrl.toLowerCase().endsWith(".gif")) {
//            Glide.with(context)
//                    .asGif()
//                    .load(imgUrl)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
//                    .listener(new RequestListener<GifDrawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
//                            if (failedLoadCallback != null) {
//                                failedLoadCallback.onFailed();
//                            }
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
//                            imageView.setImageDrawable(resource);
//                            resource.start();
//                            return true;
//                        }
//                    }).into(imageView);
//            return;
//        }
//
//        Glide.with(context)
//                .asBitmap()
//                .load(imgUrl)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        if (failedLoadCallback != null) {
//                            failedLoadCallback.onFailed();
//                        }
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        imageView.setImageBitmap(resource);
//                        return true;
//                    }
//                }).into(imageView);
//    }
//
//    /**
//     * 异步回调的方式加载图片
//     *
//     * @param context
//     * @param imgUrl
//     * @param loadFinishedCallback
//     */
//    public static void asynLoadImageView(Context context, String imgUrl, final LoadFinishedCallback loadFinishedCallback) {
//        if (TextUtils.isEmpty(imgUrl)) {
//            return;
//        }
//
//        Glide.with(context)
//                .asBitmap()
//                .load(imgUrl)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        loadFinishedCallback.onLoaded(resource);
//                    }
//                });
//    }
//
//    /**
//     * 图片预加载，提升显示的速度
//     *
//     * @param context
//     * @param imgUrl
//     */
//    public static void preload(Context context, String imgUrl) {
//        if (TextUtils.isEmpty(imgUrl)) {
//            return;
//        }
//
//        Glide.with(context)
//                .load(imgUrl)
//                .preload();
//
//    }
//
//    public interface LoadFinishedCallback {
//        void onLoaded(Bitmap bitmap);
//    }
//
//    public interface FailedLoadCallback {
//        void onFailed();
//    }
//}
