package demo.li.opal.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import demo.li.opal.uidemo.cardDeck.CardDeckActivity;
import demo.li.opal.uidemo.nestedRecycler.NestedRecyclerActivity;
import demo.li.opal.uidemo.swipe.SwipeRefreshActivity;
import demo.li.opal.uidemo.swipeCard.SwipeCardActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private View btn2SwipeRefresh;
    private View btn2NestedRecycler;
    private View btn2SwipeCard;
    private View btn2CardDeck;
    private View btn2Webp;
    private View btn2Grid;
    private View btn2Unity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn2SwipeRefresh = findViewById(R.id.swipe_refresh);
        btn2SwipeRefresh.setOnClickListener(this);

        btn2NestedRecycler = findViewById(R.id.nested_recycler);
        btn2NestedRecycler.setOnClickListener(this);

        btn2SwipeCard = findViewById(R.id.swipe_card);
        btn2SwipeCard.setOnClickListener(this);

        btn2CardDeck = findViewById(R.id.card_deck);
        btn2CardDeck.setOnClickListener(this);

        btn2Webp = findViewById(R.id.webp);
        btn2Webp.setOnClickListener(this);

        btn2Grid = findViewById(R.id.grid);
        btn2Grid.setOnClickListener(this);

        btn2Unity = findViewById(R.id.unity);
        btn2Unity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swipe_refresh:
                Intent iSR = new Intent(MainActivity.this, SwipeRefreshActivity.class);
                startActivity(iSR);
                break;
            case R.id.nested_recycler:
                Intent iNR = new Intent(MainActivity.this, NestedRecyclerActivity.class);
                startActivity(iNR);
                break;
            case R.id.swipe_card:
                Intent iSC = new Intent(MainActivity.this, SwipeCardActivity.class);
                startActivity(iSC);
                break;
            case R.id.card_deck:
                Intent iCD = new Intent(MainActivity.this, CardDeckActivity.class);
                startActivity(iCD);
                break;
            case R.id.webp:
                Intent iWebP = new Intent(MainActivity.this, WebPActivity.class);
                startActivity(iWebP);
                break;
            case R.id.grid:
                Intent iGrid = new Intent(MainActivity.this, GridActivity.class);
                startActivity(iGrid);
                break;
            case R.id.unity:
                Intent iUnity = new Intent(MainActivity.this, UnityActivity.class);
                startActivity(iUnity);
                break;
        }
    }
}
