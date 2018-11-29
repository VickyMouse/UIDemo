package demo.li.opal.uidemo.media;

import android.media.AudioManager;
import android.view.Surface;
import android.view.View;

import com.tencent.oskplayer.OskPlayer;
import com.tencent.oskplayer.OskPlayerConfig;
import com.tencent.oskplayer.player.OskExoMediaPlayer;

import demo.li.opal.uidemo.BuildConfig;
import demo.li.opal.uidemo.GlobalContext;
import demo.li.opal.uidemo.Utils.LogUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class OskVideoController {

    private static final String TAG = OskVideoController.class.getName();

    private IMediaPlayer mPlayer;
    public InternalListener mListener;
    private FeedsCropVideoView videoView;

    private volatile static OskVideoController mInstance;

    public static OskVideoController getInstance() {
        if (mInstance == null) {
            synchronized (OskVideoController.class) {
                if (mInstance == null) {
                    mInstance = new OskVideoController();
                }
            }
        }
        return mInstance;
    }

    private OskVideoController() {
        initOskConfig();
        initOskPlayer();
    }

    private void initOskConfig(){

        OskPlayerConfig cfg = new OskPlayerConfig();
        if (BuildConfig.DEBUG){
            cfg.setDebugVersion(true);
        }else{
            cfg.setDebugVersion(false);
        }
        cfg.setEnableCache(true);
        OskPlayer.init(GlobalContext.getContext(),cfg);
    }

    private void initOskPlayer(){
        mPlayer = new OskExoMediaPlayer();
        initSetting();
    }

    private void initSetting(){
        mListener = new InternalListener();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(mListener);
        mPlayer.setOnInfoListener(mListener);
        mPlayer.setLooping(true);
    }


    private class InternalListener implements View.OnClickListener,
            IMediaPlayer.OnPreparedListener,
            IMediaPlayer.OnInfoListener{
        @Override
        public void onClick(View view){
            startPlay();
        }

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer){
            try {
                LogUtils.d(TAG,"oskplayer prepared");
                mPlayer.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra){
            LogUtils.d(TAG,"oskplayer info" + what);
            if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                if (videoView != null){
                    videoView.showPlayView();
                }
                return true;
            }
            return false;
        }
    }

    public void setVideoView(FeedsCropVideoView videoView){
        this.videoView = videoView;
    }

    public void startPlay(){
        LogUtils.i(TAG, "video edit: start play");
        if (mPlayer != null){
            mPlayer.prepareAsync();
        }
        if (videoView != null){
            videoView.showBufferView();
        }
    }

    public void setDataSource(String url){
        if (mPlayer != null){
            try {
                mPlayer.release();
                initOskPlayer();
                mPlayer.setDataSource(OskPlayer.getInstance().getUrl(url));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setSurface(Surface surface) {
        if (mPlayer != null) {
            LogUtils.i(TAG, "video edit: bindToSurface() - setSurface()");
            mPlayer.setSurface(surface);
        }
    }

    public void pause(){
        LogUtils.i(TAG, "video edit: pause");
        if (mPlayer != null){
            try {
                mPlayer.pause();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (videoView != null){
            videoView.showStopView();
        }
    }

    public void resume(){
        LogUtils.i(TAG, "video edit: resume");
        if (mPlayer != null){
            try {
                mPlayer.start();
            }catch (Exception e){
                LogUtils.e(e);
            }
        }
        if (videoView != null){
            videoView.showPlayView();
        }
    }

}
