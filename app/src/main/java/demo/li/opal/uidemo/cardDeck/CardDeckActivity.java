package demo.li.opal.uidemo.cardDeck;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.FileUtils;
import demo.li.opal.uidemo.Utils.LogUtils;

public class CardDeckActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = CardDeckActivity.class.getSimpleName();

    private CardSlidePanel.CardDeckListener cardSwitchListener;

    private String imagePaths[] = {
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall01.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall02.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall03.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall04.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall05.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall06.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall07.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall08.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall09.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall10.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall11.jpg",
            FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall12.jpg"
    }; // 12 个图片资源

    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋",
            "李易峰", "霍建华", "胡歌", "曾志伟", "吴孟达", "梁朝伟"}; // 12个人名

    private List<CosCardItemData> dataList = new ArrayList<>();
    private CardSlidePanel slidePanel;
    private View btnLoadMore;
    private View btnRetake;
    private View btnDiscard;
    private View btnSave;

    private float availableCardDeckW = -1f, availableCardDeckH = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_deck);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (slidePanel != null) {
            slidePanel.doUnbindAdapter();
        }
    }

    private void initView() {
        slidePanel = findViewById(R.id.image_slide_panel);
        // 对当前卡片的左右滑动监听
        cardSwitchListener = new CardSlidePanel.CardDeckListener() {

            @Override
            public void onShow(int index) {
                LogUtils.d("Card", "正在显示-" + dataList.get(index).getDescription());
            }

            @Override
            public void onCardVanish(int index, int type) {
                LogUtils.d("Card", "正在消失-" + dataList.get(index).getDescription() + " 消失 type=" + type);
            }

            @Override
            public void onCardDeckLoadFinish() {
                LogUtils.d(TAG, "onCardDeckLoadFinish()");
                if (!btnLoadMore.isEnabled()) {
                    LogUtils.d(TAG, "onCardDeckLoadFinish() - enable btn");
                    btnLoadMore.setEnabled(true);
                }
            }
        };
        slidePanel.setCardDeckListener(cardSwitchListener);

        // 2. 绑定 Adapter
        calculateTopCardScale();
        slidePanel.setAdapter(new CardAdapter() {
            @Override
            public int getLayoutId() {
                return R.layout.cos_card_item;
            }

            @Override
            public int getCount() {
                return dataList.size(); // 不只是 VIEW_COUNT(4)，可能大大超出
            }

            @Override
            public void bindView(View view, int index) {
                Object tag = view.getTag(); // ViewHolder 的指针
                CosCardVH viewHolder;
                if (null != tag) {
                    viewHolder = (CosCardVH) tag;
                } else {
                    viewHolder = new CosCardVH(view);
                    view.setTag(viewHolder);
                }
                viewHolder.bindData(dataList.get(index));
            }

            @Override
            public Object getItem(int index) {
                return dataList.get(index);
            }

            @Override
            public Rect obtainDraggableArea(View view) {
                // 可滑动区域定制，该函数只会调用一次
                View contentView = view.findViewById(R.id.card_item);
                int left = view.getLeft() + contentView.getPaddingLeft();
                int right = view.getRight() - contentView.getPaddingRight();
                int top = view.getTop() + contentView.getPaddingTop();
                int bottom = view.getBottom() - contentView.getPaddingBottom();
                return new Rect(left, top, right, bottom);
            }
        });

        // 加载更多数据
        btnLoadMore = findViewById(R.id.load_more);
        btnLoadMore.setEnabled(false);
        btnLoadMore.setOnClickListener(this);

        btnRetake = findViewById(R.id.change_photo);
        btnRetake.setOnClickListener(this);
        btnDiscard = findViewById(R.id.discard_container);
        btnDiscard.setOnClickListener(this);
        btnSave = findViewById(R.id.save_container);
        btnSave.setOnClickListener(this);

        slidePanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogUtils.d(TAG, "onGlobalLayout()");

                if (dataList.size() == 0) {
                    prepareDataList();
                    slidePanel.doBindData();
                    slidePanel.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    private void calculateTopCardScale() {
        if (availableCardDeckW < 0 || availableCardDeckH < 0) {
            availableCardDeckW = DeviceUtils.getScreenWidth(CardDeckActivity.this) * 0.786f;
            availableCardDeckH = DeviceUtils.getScreenHeight(CardDeckActivity.this)
                    - DeviceUtils.dip2px(CardDeckActivity.this, 83 * 2 + 75 + 3 * 7);
//            availableCardDeckH = ((RelativeLayout)slidePanel.getParent()).getMeasuredHeight()
//                    - DeviceUtils.dip2px(CardDeckActivity.this, 83*2 + 75);
            int initWidth = slidePanel.getTopCardW();
            int initHeight = slidePanel.getTopCardH();
            float cardDeckRatio = 1f * initWidth / initHeight;
            if (availableCardDeckW / availableCardDeckH < cardDeckRatio) {
                slidePanel.setTopCardW((int) availableCardDeckW);
                slidePanel.setTopCardH((int) (availableCardDeckW / cardDeckRatio));
            } else {
                slidePanel.setTopCardH((int) availableCardDeckH);
                slidePanel.setTopCardW((int) (availableCardDeckH * cardDeckRatio));
            }
            slidePanel.setTopCardScale(1f * slidePanel.getTopCardW() / DeviceUtils.dip2px(CardDeckActivity.this, initWidth));
            slidePanel.setItemMarginTop(slidePanel.getItemMarginTop() + (int)(availableCardDeckH - slidePanel.getTopCardH())/2);
        }
    }

    private void prepareDataList() {
        for (int i = 0; i < 6; i++) {
            CosCardItemData dataItem = new CosCardItemData(imagePaths[i], dataList.size(), "造型设计 by " + names[i]);
            dataList.add(dataItem);
        }
    }

    private void appendDataList() {
        for (int i = 0; i < 6; i++) {
            CosCardItemData dataItem = new CosCardItemData(dataList.size());
            dataList.add(dataItem);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_more:
                btnLoadMore.setEnabled(false);
                appendDataList();
                slidePanel.getAdapter().notifyDataSetChanged();
                break;
            case R.id.change_photo:
                break;
            case R.id.discard_container:
                slidePanel.vanishOnBtnClick(CardSlidePanel.VANISH_TYPE_LEFT);
                break;
            case R.id.save_container:
                slidePanel.vanishOnBtnClick(CardSlidePanel.VANISH_TYPE_RIGHT);
                break;
        }
    }
}
