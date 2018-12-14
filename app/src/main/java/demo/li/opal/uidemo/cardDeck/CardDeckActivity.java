package demo.li.opal.uidemo.cardDeck;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.List;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.Utils.DeviceUtils;
import demo.li.opal.uidemo.Utils.FileUtils;
import demo.li.opal.uidemo.Utils.LogUtils;
import demo.li.opal.uidemo.viewanimator.AnimationListener;
import demo.li.opal.uidemo.viewanimator.ViewAnimator;

public class CardDeckActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = CardDeckActivity.class.getSimpleName();
    public static final int GEN_1_CARD = 0;
    public static final int STOP_GEN_CARD = 1;

    public static final float CARD_H_RATIO = 0.786f;    // 卡片宽度最多占屏幕宽度的比例

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
    private CheckBox autoGenCard;
    private View btnLoadMore;
    private View btnRetake;
    private View btnDiscard;
    private View btnSave;
    private View icDiscard;
    private View icSave;
    private View icWand;
    private ViewGroup topPanel;
    private RelativeLayout hintPanel;
    private ViewGroup hintNet, hintEnd, hintLoading;

    private boolean hasDataReturned = false;

    private float availableCardDeckW = -1f, availableCardDeckH = -1f;

    private CardSlidePanel.CardDeckListener cardDeckListener;

    SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(20, 39);
    SpringSystem springSystem = SpringSystem.create();
    // Add a spring to the system.
    Spring springLeft = springSystem.createSpring().setSpringConfig(springConfig);
    Spring springRight = springSystem.createSpring().setSpringConfig(springConfig);
    Spring springCount = springSystem.createSpring().setSpringConfig(springConfig);

    private Handler genCardHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GEN_1_CARD:
                    // 移除所有的 msg.what 为 0 等消息，保证只有一个循环消息队列再跑
                    genCardHandler.removeMessages(GEN_1_CARD);
                    // app 的功能逻辑处理
                    genOneCard();
                    // 再次发出 msg，循环更新
                    genCardHandler.sendEmptyMessageDelayed(GEN_1_CARD, 1000);
                    break;

                case STOP_GEN_CARD:
                    // 直接移除，定时器停止
                    genCardHandler.removeMessages(GEN_1_CARD);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_deck);
        initView();
        showLoadingHint();
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

        icWand = findViewById(R.id.wand);

        // 加载更多数据
        btnLoadMore = findViewById(R.id.load_more);
        btnLoadMore.setEnabled(false);
        btnLoadMore.setOnClickListener(this);

        btnRetake = findViewById(R.id.change_photo);
        btnRetake.setOnClickListener(this);
        // 点击
        btnDiscard = findViewById(R.id.discard_container);
        btnDiscard.setOnClickListener(this);
        btnSave = findViewById(R.id.save_container);
        btnSave.setOnClickListener(this);
        // 缩放动画
        icDiscard = findViewById(R.id.btn_discard);
        icSave = findViewById(R.id.btn_save);

        topPanel = findViewById(R.id.top_panel);
        initHintPanel();

        // 对当前卡片的左右滑动监听
        cardDeckListener = new CardSlidePanel.CardDeckListener() {

            public void onShow(int index) {
                LogUtils.d("Index Error", "onShow(" + index + " / " + dataList.size() + ")");
                LogUtils.d("Card", "正在显示-" + dataList.get(index).getDescription());
            }

            @Override
            public void onCardVanish(int index, int type) {
                // Todo：当一秒生成一张卡片的时候，左右划（同一个方向也会）有时候会造成同一张卡片 Vanish 两次
                // 一堆一堆加载不会
                LogUtils.d("Index Error", "onCardVanish(" + index + " / " + dataList.size() + ", " + type + ")");
                LogUtils.d("Card", "正在消失-" + dataList.get(index).getDescription() + " 消失 type=" + type);
                if (type == CardSlidePanel.VANISH_TYPE_LEFT) {
                    icDiscard.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            springLeft.setCurrentValue(1.05);
                            springLeft.setEndValue(1);
                        }
                    }).start();
                } else {
                    icSave.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            springRight.setCurrentValue(1.05);
                            springRight.setEndValue(1);
                        }
                    }).start();
                }
                icWand.animate().scaleX(1.1f).scaleY(1.1f).setDuration(130).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        springCount.setCurrentValue(1.1f);
                        springCount.setEndValue(1.0f);
                    }
                }).start();
                if (isLastCard(index)) {
                    showEndHint();
                }
            }

            @Override
            public void onCardRecover(int index) {
                // Todo：当一秒生成一张卡片的时候，左右划（同一个方向也会）有时候会造成同一张卡片 Vanish 两次
                // 一堆一堆加载不会
                LogUtils.d("Index Error", "onCardRecover(" + index + " / " + dataList.size() + ")");
                if (icDiscard.getScaleX() != 1f) {
                    LogUtils.d("Index Error", "onCardRecover(icDiscard)");
                    icDiscard.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            springLeft.setCurrentValue(1.05);
                            springLeft.setEndValue(1);
                        }
                    }).start();
                }
                if (icSave.getScaleX() != 1f) {
                    LogUtils.d("Index Error", "onCardRecover(icSave)");
                    icSave.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            springRight.setCurrentValue(1.05);
                            springRight.setEndValue(1);
                        }
                    }).start();
                }
            }

            @Override
            public void onTopCardMoved(float ratio, int type) {
                LogUtils.d("Card", "顶部卡片拖动：" + ratio);
                if (icDiscard == null || icSave == null) {
                    return;
                }
                if (type == CardSlidePanel.VANISH_TYPE_LEFT) {
                    icDiscard.setScaleX(1 + 0.05f * ratio);
                    icDiscard.setScaleY(1 + 0.05f * ratio);
                    icSave.setScaleX(1f);
                    icSave.setScaleY(1f);
                } else {
                    icDiscard.setScaleX(1f);
                    icDiscard.setScaleY(1f);
                    icSave.setScaleX(1 + 0.05f * ratio);
                    icSave.setScaleY(1 + 0.05f * ratio);
                }
            }

            @Override
            public void onCardDeckLoadFinish() {
                LogUtils.d("Index Error", "onCardDeckLoadFinish()");
                LogUtils.d(TAG, "onCardDeckLoadFinish()");
                if (!btnLoadMore.isEnabled()) {
                    LogUtils.d(TAG, "onCardDeckLoadFinish() - enable btn");
                    btnLoadMore.setEnabled(true);
                }
            }
        };
        slidePanel.setCardDeckListener(cardDeckListener);

        // 2. 绑定 Adapter
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
                // Daily Cos 的作用区域是左上和有下装饰（牌的花色）围起来的区域，找设计确认过可以，无需扩大到整张牌的区域
                View contentView = view.findViewById(R.id.card_item);
                int left = view.getLeft() + contentView.getPaddingLeft();
                int right = view.getRight() - contentView.getPaddingRight();
                int top = view.getTop() + contentView.getPaddingTop();
                int bottom = view.getBottom() - contentView.getPaddingBottom();
                return new Rect(left, top, right, bottom);
            }

            @Override
            public void calcTopCardDimension() {
                /* 屏幕适配策略：
                 * 1. 可用宽度 = 屏幕宽度 * CARD_H_RATIO；
                 * 2. 可用高度 = 屏幕高度 - 顶部其他部分（标题、tab 等）高度 - 顶部 Margin（每日剩余次数面板高度）
                 *              - 底部按钮栏高度 - 底部按钮栏 Margin - 卡堆内所有卡片竖直方向总偏移；
                 * 3. 卡片保持宽高比例，在可用区域内缩放适配；
                 * 4. 如果（长屏手机上）缩放后的卡堆高度没有撑满可用高度，则竖直方向居中显示；
                 */
                if (availableCardDeckW < 0 || availableCardDeckH < 0) {
                    Resources res = getResources();
                    slidePanel.setTopCardW(res.getDimensionPixelSize(R.dimen.daily_cos_card_width));
                    slidePanel.setTopCardH(res.getDimensionPixelSize(R.dimen.daily_cos_card_height));

                    availableCardDeckW = DeviceUtils.getScreenWidth(CardDeckActivity.this) * CARD_H_RATIO;
                    availableCardDeckH = DeviceUtils.getScreenHeight(CardDeckActivity.this)
                            - res.getDimensionPixelSize(R.dimen.daily_cos_top_placeholder)  // 顶部其他区域（标题、tab 等）高度
                            - res.getDimensionPixelSize(R.dimen.daily_cos_top_panel_height) // 顶部 Margin（每日剩余次数面板高度）
                            - res.getDimensionPixelSize(R.dimen.daily_cos_bottom_bar_height)    // 底部按钮栏高度
                            - res.getDimensionPixelSize(R.dimen.daily_cos_bottom_bar_margin_bottom) // 底部按钮栏 Margin
                            - (CardSlidePanel.VIEW_COUNT - 2) * slidePanel.getYOffsetStep(); // 卡堆内所有卡片竖直方向总偏移（TODO：这里其实有漏洞，因为缩放后，这个值也应该偏移）
//                    availableCardDeckH = ((RelativeLayout) slidePanel.getParent()).getMeasuredHeight();
                    float cardDeckRatio = 1f * slidePanel.getTopCardW() / slidePanel.getTopCardH();
                    float deckScale;
                    if (availableCardDeckW / availableCardDeckH < cardDeckRatio) {
                        deckScale = availableCardDeckW / slidePanel.getTopCardW();
                    } else {
                        deckScale = availableCardDeckH / slidePanel.getTopCardH();
                    }
                    // 默认缩放中心是控件中心
                    slidePanel.setScaleX(deckScale);
                    slidePanel.setScaleY(deckScale);
                    // 竖直方向上居中显示
                    slidePanel.setItemMarginTop(slidePanel.getItemMarginTop() + (int) ((availableCardDeckH - slidePanel.getTopCardH()) / 2));
                    updateHintPanelDimen((int) (slidePanel.getTopCardW() * deckScale), (int) (slidePanel.getTopCardH() * deckScale));
                }
            }
        });
        slidePanel.doBindData();
        slidePanel.getAdapter().notifyDataSetChanged();

        autoGenCard = findViewById(R.id.auto_gen_card);
        autoGenCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    genCardHandler.sendEmptyMessage(GEN_1_CARD);
                } else {
                    genCardHandler.sendEmptyMessage(STOP_GEN_CARD);
                }

            }
        });

//        slidePanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                LogUtils.d(TAG, "onGlobalLayout()");
//
//                slidePanel.doBindData();
//                slidePanel.getAdapter().notifyDataSetChanged();
//            }
//        });

        // Add a listener to observe the motion of the spring.
        springLeft.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                icDiscard.setScaleX(value);
                icDiscard.setScaleY(value);
            }
        });

        // Add a listener to observe the motion of the spring.
        springRight.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                icSave.setScaleX(value);
                icSave.setScaleY(value);
            }
        });

        // Add a listener to observe the motion of the spring.
        springCount.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                icWand.setScaleX(value);
                icWand.setScaleY(value);
            }
        });

        slidePanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasDataReturned) {
                    prepareDataList();
//                    slidePanel.doBindData();
                    slidePanel.getAdapter().notifyDataSetChanged();
                    hasDataReturned = true;
                }
                animHide(hintPanel);
                animShowTopPanel();
            }
        }, 2000);
    }

    private void initHintPanel() {
        hintPanel = findViewById(R.id.daily_cos_hint_container);
        hintNet = hintPanel.findViewById(R.id.hint_network);
        hintEnd = hintPanel.findViewById(R.id.hint_end);
        hintLoading = hintPanel.findViewById(R.id.hint_fetching);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(FileUtils.getUriByRes(R.drawable.anim_daily_cos_loading))
                .setAutoPlayAnimations(true)
                .build();
        SimpleDraweeView loadingAnim = hintLoading.findViewById(R.id.pic_fetching);
        if (loadingAnim != null) {
            loadingAnim.setController(controller);
        }
    }

    private void updateHintPanelDimen(int w, int h) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) hintPanel.getLayoutParams();
        lp.width = w;
        lp.height = h;
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public void showNetHint() {
        animShow(hintPanel);
        show(hintNet);
        hide(hintEnd);
        hide(hintLoading);
    }

    public void showEndHint() {
        animShow(hintPanel);
        hide(hintNet);
        show(hintEnd);
        hide(hintLoading);
    }

    public void showLoadingHint() {
        animShow(hintPanel);
        hide(hintNet);
        hide(hintEnd);
        show(hintLoading);
    }

    public void animShow(View v) {
        if (v != null && v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
            ViewAnimator.animate(v)
                    .alpha(0, 1)
                    .duration(300)
                    .start();
        }
    }

    public void animHide(final View v) {
        if (v != null && v.getVisibility() == View.VISIBLE) {
            ViewAnimator.animate(v)
                    .alpha(1, 0)
                    .duration(300)
                    .onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            v.setVisibility(View.INVISIBLE);
                        }
                    })
                    .start();
        }
    }

    public void show(View v) {
        if (v != null && v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public void hide(View v) {
        if (v != null && v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareDataList() {
//        dataList.clear();
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
        if (!hasDataReturned) {
            return;
        }
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

    public void genOneCard() {
        int i = (int) (System.currentTimeMillis() % imagePaths.length);
        CosCardItemData dataItem = new CosCardItemData(imagePaths[i], dataList.size(), "造型设计 by " + names[i]);
        dataList.add(dataItem);
        notifyDataChangeOnUIThread();
    }

    private void notifyDataChangeOnUIThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                slidePanel.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void animShowTopPanel() {
        if (topPanel == null) {
            return;
        }
        topPanel.setVisibility(View.VISIBLE);
        ViewAnimator.animate(topPanel)
                .duration(500)
                .translationY(-topPanel.getHeight(), 0)
                .alpha(0, 1)
                .decelerate()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        animShowBottomBtns();
                    }
                })
                .start();
    }

    public void animShowBottomBtns() {
        final int hOffset = getResources().getDimensionPixelSize(R.dimen.daily_cos_card_y_offset);
        if (btnRetake != null) {
            btnRetake.setVisibility(View.VISIBLE);
            ViewAnimator.animate(btnRetake)
                    .duration(330)
                    .translationY(btnRetake.getTop() + hOffset, btnRetake.getY())
                    .alpha(0, 1)
                    .decelerate()
                    .start();
        }

        if (btnDiscard != null) {
            btnDiscard.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnDiscard.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(btnDiscard)
                            .duration(330)
                            .translationY(btnDiscard.getY() + hOffset, btnDiscard.getY())
                            .alpha(0, 1)
                            .decelerate()
                            .start();
                }
            }, 180);
        }

        if (btnSave != null) {
            btnSave.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSave.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(btnSave)
                            .duration(330)
                            .translationY(btnSave.getY() + hOffset, btnSave.getY())
                            .alpha(0, 1)
                            .decelerate()
                            .start();
                }
            }, 360);
        }
    }

    public boolean isLastCard(int index) {
        if (index == dataList.size() - 1) {
            return true;
        }
        return false;
    }
}
