package demo.li.opal.uidemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import demo.li.opal.uidemo.R;

public class IndicatorLinearLayout extends LinearLayout {
    private int selectedIcon = 0;
    private int unselectedIcon = 0;

    public IndicatorLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public IndicatorLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorStyle, 0, 0);
        selectedIcon = a.getResourceId(R.styleable.IndicatorStyle_selected, -1);
        unselectedIcon = a.getResourceId(R.styleable.IndicatorStyle_unselected, -1);
        a.recycle();
        setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public void total(int total) {
        total(total, 0);
    }

    public void total(int total, int index) {
        removeAllViews();
        if (total <= 1) {
            return;
        }

        for (int i = 0; i < total; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == index) {
                imageView.setImageResource(selectedIcon);
            } else {
                imageView.setImageResource(unselectedIcon);
            }
            if (i < total -1) {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = 20;
                addView(imageView, params);
            } else {
                addView(imageView);
            }
        }
    }

    public void changeIndex(int index) {
        for (int i = 0; i < getChildCount(); i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            if (i == index) {
                imageView.setImageResource(selectedIcon);
            } else {
                imageView.setImageResource(unselectedIcon);
            }
        }
    }
}
