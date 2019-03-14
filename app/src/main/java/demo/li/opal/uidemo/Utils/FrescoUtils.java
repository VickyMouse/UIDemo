/**
 * Created by apc on 15/10/30.
 */
package demo.li.opal.uidemo.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.File;

import demo.li.opal.uidemo.GlobalContext;

public class FrescoUtils {
    private static final String TAG = FrescoUtils.class.getSimpleName();

    public static final String FRESCO_SCHEME_ASSETS = "asset:///";
    /**
     * Be careful, it's different
     */
    private static final String FRESCO_SCHEME_STORAGE = "file://";
    private static final String FRESCO_SCHEME_CONTENT = "content://";
    private static final String FRESCO_SCHEME_RES = "res://";

    /**
     * Fresco URL 转换方法，把图片地址转换成 Fresco 支持的格式
     *
     * @param url
     * @return
     */
    public static Uri getUri(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(FileUtils.RES_PREFIX_ASSETS)) {
            url = FRESCO_SCHEME_ASSETS + FileUtils.checkAssetsPhoto(GlobalContext.getContext(), url.substring(FileUtils.RES_PREFIX_ASSETS.length()));
        } else if (url.startsWith(FileUtils.RES_PREFIX_STORAGE)) {
//            url = FRESCO_SCHEME_STORAGE + FileUtils.checkPhoto(url);
            return Uri.fromFile(new File(FileUtils.checkPhoto(url)));
            /* 此处修复 Bug #62785233：【选图界面】在设置中修改"保存路径"文件夹，若包含特殊字符（如：???），拍照保存，下次进入打开选图界面，该图片缩略图为空。
            问题原因：Uri.parse() 方法传入的参数 urlString 中的特殊字符必须要经过转义, 否则会出问题。
            修复方式：采用："对文件路径拼接成的 uri 字符串利用 Uri.encode() 进行编码, 然后传递给 Uri.parse() 方法"的方式修改仍然不行，导致所有图片都加载不出来了!!
            所以最终选择了：使用 android.net.Uri.fromFile(File file) 方法获取本地文件的 Uri 对象。
            */
        }
        LogUtils.d(TAG, "[getUri] + END, output url = " + url);
        return Uri.parse(url);
    }

    public static Uri getUriByRes(int resId) {
        return Uri.parse(FRESCO_SCHEME_RES + "drawable/" + resId);
    }

    public static String getResPath(int resId) {
        return FRESCO_SCHEME_RES + "drawable/" + resId;
    }

    public static void setImageController(SimpleDraweeView draweeView, String path, ControllerListener controllerListener) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(FileProvider.getUriForFile(GlobalContext.getContext(), "demo.li.opal.uidemo.fileProvider", new File(path)))
                .setControllerListener(controllerListener).build();
        draweeView.setController(controller);
    }

    /**
     * 设置图片或视频缩略图到 imageView 中
     */
    public static void setImageController(SimpleDraweeView imageView, String path, int width, int height) {
        if (FrescoUtils.getUri(path) == null) {
            return;
        }
        ResizeOptions options = new ResizeOptions(width, height);
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(FrescoUtils.getUri(path))
                .setResizeOptions(options)
                .build();

        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }


    public static void setGifController(SimpleDraweeView imageView, Uri uri) {
        setGifController(imageView, uri, new BaseControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                LogUtils.d(TAG, "setGifController() - onSubmit()");
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                LogUtils.d(TAG, "setGifController() - onFinalImageSet()");
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                LogUtils.d(TAG, "setGifController() - onIntermediateImageSet()");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                LogUtils.d(TAG, "setGifController() - onIntermediateImageFailed()");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                LogUtils.d(TAG, "setGifController() - onFailure(): " + throwable.getMessage());
            }

            @Override
            public void onRelease(String id) {
                LogUtils.d(TAG, "setGifController() - onRelease()");
            }
        });
    }

    private static void setGifController(SimpleDraweeView imageView, Uri uri, ControllerListener<ImageInfo> listener) {
        LogUtils.v(TAG, "[setGifController] uri: " + uri.toString());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .setControllerListener(listener)
                .build();
        imageView.setController(controller);
    }

    public static void setWebpController(SimpleDraweeView imageView, Uri uri) {
        setWebpController(imageView, uri, new BaseControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                LogUtils.d(TAG, "setWebpController() - onSubmit()");
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                LogUtils.d(TAG, "setWebpController() - onFinalImageSet()");
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                LogUtils.d(TAG, "setWebpController() - onIntermediateImageSet()");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                LogUtils.d(TAG, "setWebpController() - onIntermediateImageFailed()");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                LogUtils.d(TAG, "setWebpController() - onFailure(): " + throwable.getMessage());
            }

            @Override
            public void onRelease(String id) {
                LogUtils.d(TAG, "setWebpController() - onRelease()");
            }
        });
    }

    private static void setWebpController(SimpleDraweeView imageView, Uri uri, ControllerListener<ImageInfo> listener) {
        LogUtils.v(TAG, "setWebpController(" + uri.toString() + ")");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .setControllerListener(listener)
                .build();
        imageView.setController(controller);
    }

    public static void loadGifFromFile(SimpleDraweeView imageView, String path, ControllerListener<ImageInfo> listener) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME) // "file"
                .path(path)
                .build();
        setGifController(imageView, uri, listener);
    }

    public static void loadGifFromRes(SimpleDraweeView imageView, String path, int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(resId))
                .build();

        setGifController(imageView, uri);
    }

    public static void loadGifFromAsset(SimpleDraweeView imageView, String path) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME) // "asset"
                .path(path)
                .build();

        setGifController(imageView, uri);
    }

    public static void loadWebpFromAsset(SimpleDraweeView imageView, String path) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME) // "asset"
                .path(path)
                .build();

        setWebpController(imageView, uri);
    }

    public static void loadGifFromHttp(SimpleDraweeView imageView, String path) {
        setGifController(imageView, Uri.parse(path));
    }

    public static void loadWebpFromHttp(SimpleDraweeView imageView, String path) {
        setWebpController(imageView, Uri.parse(path));
    }

    public static void loadGifFromHttp(SimpleDraweeView imageView, Uri uri, BaseControllerListener<ImageInfo> listener) {
        setGifController(imageView, uri, listener);
    }

    public static void loadImageAndGetBitmap(SimpleDraweeView imageView, Uri uri, final BitmapListener listener) {
        Postprocessor redMeshPostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "redMeshPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                listener.onSuccess(bitmap);
            }
        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(redMeshPostprocessor)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(imageView.getController())
                        // other setters as you need
                        .build();
        imageView.setController(controller);
    }

    public interface BitmapListener {
        void onSuccess(Bitmap bitmap);
    }
}
