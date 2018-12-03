package demo.li.opal.uidemo.cardDeck;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by opalli on 2018/12/01
 */

public abstract class CardAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    /**
     * layout 文件 ID，调用者必须实现
     */
    public abstract int getLayoutId();

    /**
     * item 数量，调用者必须实现
     */
    public abstract int getCount();

    /**
     * View 与数据绑定回调，可重载
     */
    public abstract void bindView(View view, int index);

    /**
     * 获取数据用
     */
    public abstract Object getItem(int index);


    /**
     * 可滑动区域定制
     *
     * @param view 拖动的 View
     */
    public Rect obtainDraggableArea(View view) {
        return null;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }
}
