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
import demo.li.opal.uidemo.Utils.FileUtils;
import demo.li.opal.uidemo.Utils.LogUtils;

public class CardDeckActivity extends FragmentActivity {
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

    private List<NormalCardItemData> dataList = new ArrayList<>();
    private CardSlidePanel slidePanel;
    private View btnLoadMore;


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
                LogUtils.d("Card", "正在显示-" + dataList.get(index).getUserName());
            }

            @Override
            public void onCardVanish(int index, int type) {
                LogUtils.d("Card", "正在消失-" + dataList.get(index).getUserName() + " 消失 type=" + type);
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
        slidePanel.setAdapter(new CardAdapter() {
            @Override
            public int getLayoutId() {
                return R.layout.card_item;
            }

            @Override
            public int getCount() {
                return dataList.size(); // 不只是 VIEW_COUNT(4)，可能大大超出
            }

            @Override
            public void bindView(View view, int index) {
                Object tag = view.getTag(); // ViewHolder 的指针
                CardVH viewHolder;
                if (null != tag) {
                    viewHolder = (CardVH) tag;
                } else {
                    viewHolder = new CardVH(view);
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
                View contentView = view.findViewById(R.id.card_item_content);
                View topLayout = view.findViewById(R.id.card_top_layout);
                View bottomLayout = view.findViewById(R.id.card_bottom_layout);
                int left = view.getLeft() + contentView.getPaddingLeft() + topLayout.getPaddingLeft();
                int right = view.getRight() - contentView.getPaddingRight() - topLayout.getPaddingRight();
                int top = view.getTop() + contentView.getPaddingTop() + topLayout.getPaddingTop();
                int bottom = view.getBottom() - contentView.getPaddingBottom() - bottomLayout.getPaddingBottom();
                return new Rect(left, top, right, bottom);
            }
        });

        // 加载更多数据
        btnLoadMore = findViewById(R.id.load_more);
        btnLoadMore.setEnabled(false);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoadMore.setEnabled(false);
                appendDataList();
                slidePanel.getAdapter().notifyDataSetChanged();
            }
        });

        slidePanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogUtils.d(TAG, "onGlobalLayout()");
                if (dataList.size() == 0) {
                    prepareDataList();  // Todo: 不知道为什么，必须在 View 都创建完后再和数据绑定，不然只会有两张卡片
                    slidePanel.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    private void prepareDataList() {
        for (int i = 0; i < 6; i++) {
            NormalCardItemData dataItem = new NormalCardItemData(names[i], imagePaths[i], (int) (Math.random() * 10), (int) (Math.random() * 6));
            dataList.add(dataItem);
        }
    }

    private void appendDataList() {
        for (int i = 0; i < 6; i++) {
            NormalCardItemData dataItem = new NormalCardItemData();
            dataList.add(dataItem);
        }
    }
}
