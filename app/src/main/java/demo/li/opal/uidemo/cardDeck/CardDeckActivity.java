package demo.li.opal.uidemo.cardDeck;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.LogUtils;

public class CardDeckActivity extends FragmentActivity {

    private CardSlidePanel.CardDeckListener cardSwitchListener;

    private String imagePaths[] = {
            "file:///android_asset/cards/wall01.jpg",
            "file:///android_asset/cards/wall02.jpg",
            "file:///android_asset/cards/wall03.jpg",
            "file:///android_asset/cards/wall04.jpg",
            "file:///android_asset/cards/wall05.jpg",
            "file:///android_asset/cards/wall06.jpg",
            "file:///android_asset/cards/wall07.jpg",
            "file:///android_asset/cards/wall08.jpg",
            "file:///android_asset/cards/wall09.jpg",
            "file:///android_asset/cards/wall10.jpg",
            "file:///android_asset/cards/wall11.jpg",
            "file:///android_asset/cards/wall12.jpg"
    }; // 12 个图片资源

    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋",
            "李易峰", "霍建华", "胡歌", "曾志伟", "吴孟达", "梁朝伟"}; // 12个人名

    private List<CardItemData> dataList = new ArrayList<>();
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
                LogUtils.d("Card", "正在显示-" + dataList.get(index).userName);
            }

            @Override
            public void onCardVanish(int index, int type) {
                LogUtils.d("Card", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
            }

            @Override
            public void onCardDeckLoadFinish() {
                if (!btnLoadMore.isEnabled()) {
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnLoadMore.setEnabled(false);
                prepareDataList();
                slidePanel.getAdapter().notifyDataSetChanged();
            }
        }, 500);

        // 加载更多数据
        btnLoadMore = findViewById(R.id.load_more);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoadMore.setEnabled(false);
                appendDataList();
                slidePanel.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private void prepareDataList() {
        for (int i = 0; i < 6; i++) {
            CardItemData dataItem = new CardItemData();
            dataItem.userName = names[i];
            dataItem.imagePath = imagePaths[i];
            dataItem.likeNum = (int) (Math.random() * 10);
            dataItem.imageNum = (int) (Math.random() * 6);
            dataList.add(dataItem);
        }
    }

    private void appendDataList() {
        for (int i = 0; i < 6; i++) {
            CardItemData dataItem = new CardItemData();
            dataItem.userName = "From Append";
            dataItem.imagePath = imagePaths[8];
            dataItem.likeNum = (int) (Math.random() * 10);
            dataItem.imageNum = (int) (Math.random() * 6);
            dataList.add(dataItem);
        }
    }

    class CardVH {

        ImageView imageView;
        View maskView;
        TextView userName;
        TextView picCount;
        TextView likeCount;

        public CardVH(View view) {
            imageView = view.findViewById(R.id.card_image_view);
            maskView = view.findViewById(R.id.maskView);
            userName = view.findViewById(R.id.card_user_name);
            picCount = view.findViewById(R.id.card_pic_num);
            likeCount = view.findViewById(R.id.card_like);
        }

        public void bindData(CardItemData itemData) {
            Glide.with(CardDeckActivity.this).load(itemData.imagePath).into(imageView);
            userName.setText(itemData.userName);
            picCount.setText(itemData.imageNum + "");
            likeCount.setText(itemData.likeNum + "");
        }
    }

}
