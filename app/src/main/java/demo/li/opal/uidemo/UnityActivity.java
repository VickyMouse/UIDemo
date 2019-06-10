package demo.li.opal.uidemo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.opalli.StarGazing.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

public class UnityActivity extends UnityPlayerActivity {
    private LinearLayout u3dLayout;
    private Button btnUp, btnDown, btnLeft, btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);
        u3dLayout = findViewById(R.id.u3d_layout);
        u3dLayout.addView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        btnUp = findViewById(R.id.up_btn);
        btnDown = findViewById(R.id.down_btn);
        btnLeft = findViewById(R.id.left_btn);
        btnRight = findViewById(R.id.right_btn);
        btnUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "MoveUp", "");
            }
        });
        btnDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "MoveDown", "");
            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "MoveLeft", "");
            }
        });
        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "MoveRight", "");
            }
        });
    }
//        public String getName(final String str) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(UnityActivity.this, str, Toast.LENGTH_LONG).show();
//                }
//            });
//            return "我是怪兽，哈哈哈";
//        }
//        /**
//         * 3D调用此方法，用于退出3D
//         */
//        public void makePauseUnity() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mUnityPlayer != null) {
//                        try {
//                            mUnityPlayer.quit();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    UnityActivity.this.finish();
//                }
//            });
//        }

    /**
     * 按键点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onDestroy();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UnityPlayer.UnitySendMessage("Manager", "Unload", "");
        mUnityPlayer.quit();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mUnityPlayer.quit();
        // this.finish();
    }
}
