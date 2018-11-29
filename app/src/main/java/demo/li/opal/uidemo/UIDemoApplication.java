package demo.li.opal.uidemo;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import demo.li.opal.uidemo.Utils.PrefsUtils;

/**
 * Application类
 */
public class UIDemoApplication extends Application {

    private static final String TAG = UIDemoApplication.class.getSimpleName();

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplication();
        GlobalContext.setContext(mContext);
        PrefsUtils.init();
        /** point -- Fresco **/
        initFresco();
    }

    private void initFresco() {

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplication())
                .setDownsampleEnabled(true) // 支持开启PNG,JPEG,WebP的缩放功能
//                .setRequestListeners(requestListeners)  // 打开 log
                .build();
        Fresco.initialize(getApplication(), config);
    }

    public Application getApplication() {
        return this;
    }
}
