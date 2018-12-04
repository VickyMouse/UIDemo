package demo.li.opal.uidemo.cardDeck;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
            CardItemData dataItem = new CardItemData(names[i], imagePaths[i], (int) (Math.random() * 10), (int) (Math.random() * 6));
            dataList.add(dataItem);
        }
    }

    private void appendDataList() {
        for (int i = 0; i < 6; i++) {
            CardItemData dataItem = new CardItemData();
            dataList.add(dataItem);
        }
    }

    class CardVH {

        SimpleDraweeView cardImage;
        View maskView;
        TextView userName;
        TextView picCount;
        TextView likeCount;

        public CardVH(View view) {
            cardImage = view.findViewById(R.id.card_image_view);
            maskView = view.findViewById(R.id.maskView);
            userName = view.findViewById(R.id.card_user_name);
            picCount = view.findViewById(R.id.card_pic_num);
            likeCount = view.findViewById(R.id.card_like);
        }

        public void bindData(CardItemData itemData) {
//            Glide.with(CardDeckActivity.this).load(itemData.imagePath).into(cardImage);

            ResizeOptions options = new ResizeOptions(cardImage.getWidth(), cardImage.getHeight());
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(FileUtils.getUri(itemData.getImagePath()))
                    .setResizeOptions(options)
                    .build();

            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(cardImage.getController()).build();
            cardImage.setController(controller);

            userName.setText(itemData.getUserName());
            picCount.setText(itemData.getImageNum() + "");
            likeCount.setText(itemData.getLikeNum() + "");
        }
    }

}
