package demo.li.opal.uidemo.nestedRecycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import demo.li.opal.uidemo.R;

public class FeedsFootVH extends RecyclerView.ViewHolder {
    public TextView tips;
    public ImageView loading;

    public FeedsFootVH(View itemView) {
        super(itemView);
        tips = (TextView) itemView.findViewById(R.id.tips);
        loading = (ImageView) itemView.findViewById(R.id.loading);
    }
}

