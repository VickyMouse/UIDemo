package demo.li.opal.uidemo.swipecard;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

import demo.li.opal.uidemo.R;


public class SwipeCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        SwipeCardLayout cardDeck = findViewById(R.id.scl_layout);

        Queue<Card> cards = new LinkedList<>();
        Card cardEntity1 = new Card(R.drawable.swipe_card_1, "这里是美丽的湖畔");
        Card cardEntity2 = new Card(R.drawable.swipe_card_2, "这里游泳比较好");
        Card cardEntity3 = new Card(R.drawable.swipe_card_3, "向往的蓝天白云");
        Card cardEntity4 = new Card(R.drawable.swipe_card_4, "繁华的都市");
        Card cardEntity5 = new Card(R.drawable.swipe_card_5, "草原象征着理想");
        cards.add(cardEntity1);
        cards.add(cardEntity2);
        cards.add(cardEntity3);
        cards.add(cardEntity4);
        cards.add(cardEntity5);

        cardDeck.setAdapter(new SwipeCardLayout.CardAdapter<Card>(cards) {
            @Override
            public View bindLayout() {
                return LayoutInflater.from(SwipeCardActivity.this).inflate(R.layout.card_layout, null);
            }

            @Override
            public void bindData(Card data, View convertView) {

                ImageView iv_card = convertView.findViewById(R.id.iv_card);
                TextView tv_card = convertView.findViewById(R.id.tv_card);
                iv_card.setImageResource(data.resId);
                tv_card.setText(data.content);
            }
        });
        cardDeck.setOnSwipeListener(new SwipeCardLayout.OnSwipeListener() {
            @Override
            public void onSwipe(int type) {
                switch (type) {
                    case SwipeCardLayout.TYPE_RIGHT:
                        Toast.makeText(SwipeCardActivity.this, "right", Toast.LENGTH_SHORT).show();

                        break;
                    case SwipeCardLayout.TYPE_LEFT:
                        Toast.makeText(SwipeCardActivity.this, "left", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    class Card {

        public Card(int resId, String content) {
            this.resId = resId;
            this.content = content;
        }

        public int resId;
        public String content;
    }
}
