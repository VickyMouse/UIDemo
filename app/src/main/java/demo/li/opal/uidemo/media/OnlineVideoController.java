//package demo.li.opal.uidemo.media;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.util.Log;
//import android.view.Surface;
//
//import java.io.IOException;
//
//import demo.li.opal.uidemo.GlobalContext;
//import demo.li.opal.uidemo.Utils.LogUtils;
//import demo.li.opal.uidemo.Utils.PlayerUtil;
//
//public class OnlineVideoController {
//
//    private static final String TAG = OnlineVideoController.class.getName();
//
//    private volatile static OnlineVideoController mInstance;
//
//    private MediaPlayer mMediaPlayer;
//    private FeedsCropVideoView mVideoView;
//
//    private float mVideoHeight;
//    private float mVideoWidth;
//
//    private boolean mIsDataSourceSet;
//    private boolean mIsVideoPrepared;
//    private boolean mIsPlayCalled;
//
//    private State mState;
//
////    protected Subscription sSubscription;
////    private Subscription mMediaPlayerSubscription; // mediaPlayer init subscription
//
////    private long mStartPoint = 0;
//
//    public enum State {
//        UNINITIALIZED, PLAY, STOP, PAUSE, END
//    }
//
//    public static OnlineVideoController getInstance() {
//        if (mInstance == null) {
//            synchronized (OnlineVideoController.class) {
//                if (mInstance == null) {
//                    mInstance = new OnlineVideoController();
////                    mInstance.initPlayer();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    private OnlineVideoController() {
//        initPlayer();
//    }
//
//    private void initPlayer() {
//        if (mMediaPlayer == null) {
////            mMediaPlayer = new IjkMediaPlayer();
//            mMediaPlayer = new MediaPlayer();
//        } else {
////            // 上一次 mediaPlayerPrepare 仍未开始，取消 subscription
////            if (mMediaPlayerSubscription != null && !mMediaPlayerSubscription.isUnsubscribed()) {
////                mMediaPlayerSubscription.unsubscribe();
////                mMediaPlayerSubscription = null;
////            }
//            mMediaPlayer.reset();
//        }
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mIsVideoPrepared = false;
//        mIsPlayCalled = false;
//        mState = State.UNINITIALIZED;
//    }
//
//    private void initListener() {
//        if (mMediaPlayer == null) {
//            return;
//        }
//        LogUtils.i(TAG, "video edit: bind - initListener()");
//        mMediaPlayer.setOnVideoSizeChangedListener(
//                new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        mVideoWidth = width;
//                        mVideoHeight = height;
//                        if (mVideoView != null) {
////                            mVideoView.updateTextureViewSize(mVideoWidth, mVideoHeight);
//                        }
//                        LogUtils.i(TAG, "video edit: onVideoSizeChanged(" + mVideoWidth + ", " + mVideoHeight + ")");
//                    }
//                }
//        );
//
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mState = State.END;
//                LogUtils.i(TAG, "Video has ended.");
//
//                if (mListener != null) {
//                    mListener.onVideoEnd();
//                }
//            }
//        });
//
//        // Play video when the media source is ready for playback.
//        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                LogUtils.i(TAG, "Video - onPrepared()");
//                mIsVideoPrepared = true;
//                LogUtils.i(TAG, "Video - not prepared: " + mIsPlayCalled + ", " + (mVideoView != null) + (mVideoView == null ? "" : ", " + mVideoView.isVideoViewReady()));
//                if (mIsPlayCalled && (mVideoView != null && mVideoView.isVideoViewReady())) {
//                    LogUtils.i(TAG, "video edit: bind - prepared and play() was called.");
//                    play();
//                }
//
//                if (mListener != null) {
//                    mListener.onVideoPrepared();
//                }
//            }
//        });
//
//        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//            @Override
//            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                LogUtils.i(TAG, "Video - onBufferingUpdate(" + percent + "), state = " + mState + ", isPlaying = " + mMediaPlayer.isPlaying());
//                if (mListener != null) {
//                    mListener.onBufferingProgress(percent);
//                }
//            }
//        });
//
//        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                if (mListener != null) {
//                    switch (what) {
//                        // NOTE: There is a known bug in Android. When playing HLS stream it's just never calls OnInfoListener or OnBuffering.
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            mListener.onBufferingStart();
//                            break;
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            mListener.onBufferingEnd();
//                            break;
//                    }
//                }
//                return false;
//            }
//        });
//
//        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                LogUtils.i(TAG, "Video - onError(" + what + ", " + extra + ")");
//                return false;
//            }
//        });
//
//    }
//
//    /**
//     * @see MediaPlayer#setDataSource(String)
//     */
//    public void setDataSource(final String path) {
//        initPlayer();
//        initListener();
//        // mediaPlayer reset似乎是异步处理的，200ms处理reset，delay之后再setDataSource和prepare
////        mMediaPlayerSubscription = Observable.timer(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
////            @Override
////            public void call(Long integer) {
//        try {
//            mMediaPlayer.setDataSource(path);
//            mIsDataSourceSet = true;
//            prepare(path);
//        } catch (IOException e) {
//            LogUtils.i(TAG, "video edit: bind - prepared IOException");
//            LogUtils.d(TAG, e.getMessage());
//        } catch (IllegalStateException e) {
//            LogUtils.i(TAG, "IllegalStateException - " + e.getMessage());
//            LogUtils.d(TAG, e.getMessage());
//        } catch (Exception e) {
//            LogUtils.i(TAG, "Exception - " + e.getMessage());
//            LogUtils.d(TAG, e.getMessage());
//        }
////            }
////        });
//    }
//
//    /**
//     * @see MediaPlayer#setDataSource(Context, Uri)
//     */
//    public void setDataSource(Context context, Uri uri) {
//        initPlayer();
//        initListener();
//
//        try {
//            mMediaPlayer.setDataSource(context, uri);
//            mIsDataSourceSet = true;
//            prepare();
//        } catch (IOException e) {
//            LogUtils.d(TAG, e.getMessage());
//        }
//    }
//
//    /**
//     * @see MediaPlayer#setDataSource(java.io.FileDescriptor)
//     */
////    public void setDataSource(AssetFileDescriptor afd) {
////        initPlayer();
////
////        try {
////            long startOffset = afd.getStartOffset();
////            long length = afd.getLength();
////            mMediaPlayer.setDataSource(afd.getFileDescriptor(), startOffset, length);
////            mIsDataSourceSet = true;
////            prepare();
////        } catch (IOException e) {
////            LogUtils.d(TAG, e.getMessage());
////        }
////    }
//    private void prepare() {
//        try {
//            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
//
//            // creating MediaPlayer
//            mMediaPlayer.prepareAsync();
//        } catch (IllegalArgumentException e) {
//            LogUtils.i(TAG, "video edit: prepare IllegalArgumentException");
//            LogUtils.d(TAG, e.getMessage());
//        } catch (SecurityException e) {
//            LogUtils.i(TAG, "video edit: prepare SecurityException");
//            LogUtils.d(TAG, e.getMessage());
//        } catch (IllegalStateException e) {
//            LogUtils.i(TAG, "video edit: prepare IllegalStateException");
//            LogUtils.d(TAG, e.toString());
//        }
//    }
//
//    private void prepare(String path) {
//        try {
//            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
//            // creating MediaPlayer
//            mMediaPlayer.prepareAsync();
//        } catch (IllegalArgumentException e) {
//            LogUtils.i(TAG, "video edit: bind - prepare IllegalArgumentException");
//            LogUtils.d(TAG, e.getMessage());
//        } catch (SecurityException e) {
//            LogUtils.i(TAG, "video edit: bind - prepare SecurityException");
//            LogUtils.d(TAG, e.getMessage());
//        } catch (IllegalStateException e) {
//            LogUtils.i(TAG, "video edit: bind - prepare IllegalStateException");
//            LogUtils.d(TAG, e.toString());
//            // don't know why, but sometimes prepare will throw IllegalStateException,
//            // in that case,recreate the media player.
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//            setDataSource(path);
//        }
//    }
//
//    /**
//     * Play or resume video. Video will be played as soon as view is available and media player is
//     * prepared.
//     * <p/>
//     * If video is stopped or ended and play() method was called, video will start over.
//     */
//    public void play() {
//        if (!mIsDataSourceSet) {
//            LogUtils.i(TAG, "Video - play() was called but data source was not set.");
//            return;
//        }
//
//        mIsPlayCalled = true;
//
//        if (!mIsVideoPrepared) {
//            LogUtils.i(TAG, "Video - play() was called but video is not prepared yet, waiting.");
//            return;
//        }
//
//        if (mVideoView == null || (mVideoView != null && !mVideoView.isVideoViewReady())) {
//            LogUtils.i(TAG, "Video - play() was called but view is not available yet, waiting.");
//            return;
//        }
//
//        if (mState == State.PLAY) {
//            LogUtils.i(TAG, "Video - play() was called but video is already playing.");
//            return;
//        }
//
////        if (sSubscription != null) {
////            if (!sSubscription.isUnsubscribed()) {
////                sSubscription.unsubscribe();
////            }
////            sSubscription = null;
////            LogUtils.i(TAG, "video edit: bind - VideoPlayer sSubscription.unsubscribe() start");
////        }
////
////        sSubscription = Observable.interval(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).onBackpressureDrop().subscribe(new Action1<Long>() {
////
////            private int lastPos = 0;
////
////            @Override
////            public void call(Long aLong) {
////                if (mMediaPlayer != null) {
////                    if (mMediaPlayer.isPlaying()) {
////                        final long duration = mMediaPlayer.getDuration();
////                        final long current = mMediaPlayer.getCurrentPosition();
////
////                        /** 为底层buffering回调有问题做容错 */
//////                        if (sPlayer.mBuffering) {
//////                            if (lastPos > current + 200) {
//////                                sPlayer.mBuffering = false;
//////                                lastPos = current;
//////                            }
//////                            //卡顿上报点 这个翻译也是碉堡了
//////                            if (sPlayer.mNeedCatonReport && current > 0) {
//////                                sPlayer.mNeedCatonReport = false;
//////                                if (duration > 0)
//////                                    sPlayer.mCurrentDuration = duration / 1000f;
//////                                sPlayer.mCatonPoints.add(current / 1000f);
//////                            }
//////                        } else {
//////                            lastPos = current;
//////                        }
////
////                        if (duration > 0 && mListener != null) {
////                            mListener.onProgress((int) (current * 100f / duration), (int) current, false, -1);
////                        }
////                    }
////                } else {
////                    if (sSubscription != null && !sSubscription.isUnsubscribed()) {
////                        sSubscription.unsubscribe();
////                    }
////                }
////            }
////        }, new Action1<Throwable>() {
////            @Override
////            public void call(Throwable throwable) {
////                //error
////            }
////        });
//
//        if (mState == State.PAUSE) {
//            LogUtils.i(TAG, "Video - play() was called but video is paused, resuming.");
//            mState = State.PLAY;
//            mMediaPlayer.start();
//            return;
//        }
//
//        if (mState == State.END || mState == State.STOP) {
//            // Todo: disable audio for now
//            AudioManager am = (AudioManager) GlobalContext.getContext().getSystemService(Context.AUDIO_SERVICE);
//            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//
//            LogUtils.i(TAG, "Video - play() was called but video already ended, starting over.");
//            mState = State.PLAY;
//            mMediaPlayer.seekTo(0);  // 从头开始播放（视频编辑界面，起始位置不一定为0）
//            mMediaPlayer.start();
//            return;
//        }
//
//        mState = State.PLAY;
//        mMediaPlayer.start();
//    }
//
//    /**
//     * Pause video. If video is already paused, stopped or ended nothing will happen.
//     */
//    public void pause() {
//        if (mState == State.PAUSE) {
//            LogUtils.i(TAG, "pause() was called but video already paused.");
//            return;
//        }
//
//        if (mState == State.STOP) {
//            LogUtils.i(TAG, "pause() was called but video already stopped.");
//            return;
//        }
//
//        if (mState == State.END) {
//            LogUtils.i(TAG, "pause() was called but video already ended.");
//            return;
//        }
//
//        mState = State.PAUSE;
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.pause();
//        }
////        if (sSubscription != null) {
////            if (!sSubscription.isUnsubscribed()) {
////                sSubscription.unsubscribe();
////            }
////            LogUtils.i(TAG, "VideoPlayer sSubscription.unsubscribe() pause()");
////        }
//    }
//
//    /**
//     * Stop video (pause and seek to beginning). If video is already stopped or ended nothing will
//     * happen.
//     */
//    public void stop() {
//        if (mState == State.STOP) {
//            LogUtils.i(TAG, "stop() was called but video already stopped.");
//            return;
//        }
//
//        if (mState == State.END) {
//            LogUtils.i(TAG, "stop() was called but video already ended.");
//            return;
//        }
//        AudioManager am = (AudioManager) GlobalContext.getContext().getSystemService(Context.AUDIO_SERVICE);
//        am.abandonAudioFocus(null);
//
//        mState = State.STOP;
////        if (sSubscription != null) {
////            if (!sSubscription.isUnsubscribed()) {
////                sSubscription.unsubscribe();
////            }
////            sSubscription = null;
////            LogUtils.i(TAG, "VideoPlayer sSubscription.unsubscribe() stop");
////        }
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.pause();
//            mMediaPlayer.seekTo(0);
//        }
//    }
//
//    /**
//     * @see MediaPlayer#setLooping(boolean)
//     */
//    public void setLooping(boolean looping) {
//        mMediaPlayer.setLooping(looping);
//    }
//
//    /**
//     * @see MediaPlayer#seekTo(int)
//     */
//    public void seekTo(int milliseconds) {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.seekTo(milliseconds);
//        }
////        if (updateStart) {
////            mStartPoint = milliseconds;
////        }
//    }
//
//    public void release() {
//        stop();
//        mMediaPlayer.setSurface(null);
//        mMediaPlayer.release();
//        mInstance = null;
//    }
//
//    public boolean isPlaying() {
//        return mMediaPlayer.isPlaying();
//    }
//
//    public void closeVolume() {
//        PlayerUtil.setVolume(GlobalContext.getContext(), 0);
//    }
//
//    public void openVolume(int volume) {
//        PlayerUtil.setVolume(GlobalContext.getContext(), volume);
//    }
//
//    public void bindToSurface(FeedsCropVideoView videoView, Surface surface) {
//        if (videoView != null && surface != null) {
//            mVideoView = videoView;
//            if (mMediaPlayer != null && surface != null) {
//                LogUtils.i(TAG, "video edit: bindToSurface() - setSurface()");
//                mMediaPlayer.setSurface(surface);
//            }
//            if (mVideoView != null) {
////                mVideoView.updateTextureViewSize(mVideoWidth, mVideoHeight);
//            }
//        }
//    }
//
//    public boolean isPlayerReady() {
//        return mIsDataSourceSet && mIsPlayCalled && mIsVideoPrepared;
//    }
//
//    /**
//     * @see MediaPlayer#getDuration()
//     */
//    public int getDuration() {
//        return (int) mMediaPlayer.getDuration();
//    }
//
//    public int getCurrentPosition() {
//        return (int) mMediaPlayer.getCurrentPosition();
//    }
//
//    private MediaPlayerListener mListener;
//
//    /**
//     * Listener trigger 'onVideoPrepared' and 'onVideoEnd' events
//     */
//    public void setListener(MediaPlayerListener listener) {
//        mListener = listener;
//    }
//
//    public interface MediaPlayerListener {
//
//        void onVideoPrepared();
//
//        void onVideoEnd();
//
//        void onProgress(int progress, int current, boolean buffering, int bufferingPercent);
//
//        void onBufferingProgress(int percent);
//
//        void onBufferingStart();
//
//        void onBufferingEnd();
//
//    }
//}