package demo.li.opal.uidemo.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.li.opal.uidemo.R;
import demo.li.opal.uidemo.swipe.FeedsSwipeRefreshLayout;

public class SwipeRefreshActivity extends AppCompatActivity implements FeedsSwipeRefreshLayout.OnRefreshListener {

    private FeedsSwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);

        refreshLayout = (FeedsSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.main_color);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 20000);
    }
}
