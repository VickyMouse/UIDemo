package demo.li.opal.uidemo;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import demo.li.opal.uidemo.Utils.LogUtils;

public class MainIconAdapter extends RecyclerView.Adapter<MainButtonVH> implements View.OnClickListener {
    private static final String TAG = MainIconAdapter.class.getSimpleName();
    private List<MainButtonModel> data;
    private LayoutInflater inflater;
    private RecyclerView mRecyclerView;//用来计算Child位置
    private OnItemClickListener onItemClickListener;

    //对外提供接口初始化方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MainIconAdapter(Context context, List<MainButtonModel> data) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 创建VIewHolder，导入布局，实例化itemView
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MainButtonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.layout_main_button_item, parent, false);
        //导入itemView，为itemView设置点击事件
        itemView.setOnClickListener(this);
        return new MainButtonVH(itemView);
    }

    /**
     * 绑定VIewHolder，加载数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MainButtonVH holder, int position) {
        MainButtonModel model = data.get(position);
        String path = (model.image instanceof Integer ? "res:///" : "") + model.image;
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
        holder.mButton.setController(controller);
        holder.mLabel.setText(model.label);//加载数据
        holder.updateItemSize();
    }

    /**
     * 数据源的数量，item的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * 适配器绑定到RecyclerView 的时候，回将绑定适配器的RecyclerView 传递过来
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    /**
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        //RecyclerView可以计算出这是第几个Child
        int childAdapterPosition = mRecyclerView.getChildAdapterPosition(v);
        LogUtils.e(TAG, "onClick: " + childAdapterPosition);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(childAdapterPosition, data.get(childAdapterPosition));
        }
    }

    /**
     * 接口回调
     * 1、定义接口，定义接口中的方法
     * 2、在数据产生的地方持有接口，并提供初始化方法，在数据产生的时候调用接口的方法
     * 3、在需要处理数据的地方实现接口，实现接口中的方法，并将接口传递到数据产生的地方
     */
    public interface OnItemClickListener {
        void onItemClick(int position, MainButtonModel model);
    }
}

