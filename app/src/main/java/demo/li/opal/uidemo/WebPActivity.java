package demo.li.opal.uidemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.FrescoUtils;

public class WebPActivity extends AppCompatActivity {

    private SimpleDraweeView webPImg;
    private SimpleDraweeView gifImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webp);

        webPImg = findViewById(R.id.webp_img);
//        FrescoUtils.loadWebpFromHttp(webPImg, "http://cdn1.biuapp.im/586a6a434c58f937ca050dbd3efa9b747e9fd1e0.webp");
        FrescoUtils.loadWebpFromAsset(webPImg, "newyear_pig.webp");
        gifImg = findViewById(R.id.gif_img);
        FrescoUtils.loadGifFromHttp(gifImg, "https://upload-images.jianshu.io/upload_images/1961674-e4f51160697c8c82.gif");

    }
}
