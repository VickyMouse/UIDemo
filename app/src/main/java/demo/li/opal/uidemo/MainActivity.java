package demo.li.opal.uidemo;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import demo.li.opal.uidemo.swipe.FeedsSwipeRefreshLayout;
import demo.li.opal.uidemo.swipe.SwipeRefreshActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private View btn2SwipeRefresh;
    private View btn2NestedRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn2SwipeRefresh = findViewById(R.id.swipe_refresh);
        btn2SwipeRefresh.setOnClickListener(this);

        btn2NestedRecycler = findViewById(R.id.nested_recycler);
        btn2NestedRecycler.setOnClickListener(this);
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
        }
    }
}
