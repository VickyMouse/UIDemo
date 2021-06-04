//package demo.li.opal.uidemo.media;
//
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.Surface;
//import android.view.TextureView;
//
//import demo.li.opal.uidemo.Utils.LogUtils;
//import demo.li.opal.uidemo.nestedRecycler.holder.FeedBaseVH;
//
//public class FeedsCropVideoView extends TextureView implements TextureView.SurfaceTextureListener {
//
//    private static final String TAG = FeedsCropVideoView.class.getName();
//
//    private OskVideoController mVideoController;
//    public Surface mCurrentSurface;
//
//    private boolean mIsViewAvailable;
//
//    private FeedBaseVH mViewHolder;
//
//    private Handler handler;
//
//    public FeedsCropVideoView(Context context) {
//        super(context);
//        initView();
//    }
//
//    public FeedsCropVideoView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public FeedsCropVideoView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        initView();
//    }
//
//    private void initView() {
//        setSurfaceTextureListener(this);
//        handler = new Handler();
//    }
//
//    @Override
//    public synchronized void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//        LogUtils.d(TAG, "video edit: bind - onSurfaceAvailable(" + mViewHolder.getAdapterPosition() + ")");
//        if (mCurrentSurface != null) {
//            mCurrentSurface.release();
//            mCurrentSurface = null;
//        }
//        mCurrentSurface = new Surface(surfaceTexture);
//        mIsViewAvailable = true;
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
////        LogUtils.d(TAG, "video edit: onSurfaceTextureSizeChanged(" + pos + ")");
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        LogUtils.d(TAG, "video edit: bind - onSurfaceTextureDestroyed(" + mViewHolder.getAdapterPosition() + ")");
//        if (mCurrentSurface != null) {
//            mCurrentSurface.release();
//            mCurrentSurface = null;
//        }
//        if (surface != null) {
//            surface.release();
//        }
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
////        LogUtils.d(TAG, "video edit: onSurfaceTextureUpdated(" + pos + ")");
//    }
//
////    public void updateTextureViewSize(float width, float height) {
////
////        float viewWidth = getWidth();
////        float viewHeight = getHeight();
////
////        mVideoWidth = width;
////        mVideoHeight = height;
////
////        float scaleX = 1.0f;
////        float scaleY = 1.0f;
////
////        float videoAspect = mVideoWidth / mVideoHeight;
////        float viewAspect = viewWidth / viewHeight;
////        if (mFillMode == R.id.fill_none) {
////            if (viewAspect > videoAspect) {
////                scaleY = mVideoHeight / mVideoWidth * viewAspect;
////            } else {
////                scaleX = mVideoWidth / mVideoHeight / viewAspect;
////            }
////
////        } else {
////            if (videoAspect < 1.0f) {
////                scaleX = mVideoWidth / mVideoHeight / viewAspect;
////            } else {
////                scaleY = mVideoHeight / mVideoWidth * viewAspect;
////            }
////        }
////        Matrix matrix = new Matrix();
////        matrix.setScale(scaleX, scaleY, viewWidth / 2, viewHeight / 2);
////        LogUtils.d(TAG, "video edit: updateTextureViewSize(" + scaleX + ", " + scaleY + ")");
////        LogUtils.d(TAG, "video edit: updateTextureViewSize(" + videoAspect + ", " + viewAspect + ")");
////
////        setTransform(matrix);
////    }
//
//    public void setViewHolder(FeedBaseVH viewHolder) {
//        mViewHolder = viewHolder;
//    }
//
//    public void bindController(OskVideoController controller) {
//        LogUtils.d(TAG, "video edit: bind - bindController(" + mViewHolder.getAdapterPosition() + ")");
//        if (controller != null) {
//            mVideoController = controller;
//            mVideoController.setSurface(mCurrentSurface);
//            mVideoController.setVideoView(this);
//        }
//    }
//
//    public void unbindController() {
////        LogUtils.d(TAG, "video edit: bind - bindController(" + pos + ")");
//        if (mVideoController != null) {
//            mVideoController = null;
//        }
//    }
//
//    public boolean isVideoViewReady() {
//        return mIsViewAvailable;
//    }
//
//    public void showStopView() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mViewHolder.cover.setVisibility(VISIBLE);
//            }
//        });
//    }
//
//    public void showBufferView() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mViewHolder.cover.setVisibility(VISIBLE);
//            }
//        });
//    }
//
//    public void showPlayView() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mViewHolder.cover.setVisibility(GONE);
//            }
//        });
//    }
//
//}