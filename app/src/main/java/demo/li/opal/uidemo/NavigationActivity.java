package demo.li.opal.uidemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import demo.li.opal.uidemo.views.NavigationBar;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);

        NavigationBar nav = findViewById(R.id.nav);
        nav.setRightIconClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_right_icon:
                finish();
                break;
            case R.id.nav_left_icon:
                finish();
                break;
        }
    }
}
