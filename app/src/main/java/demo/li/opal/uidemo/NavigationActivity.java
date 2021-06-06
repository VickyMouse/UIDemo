package demo.li.opal.uidemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import demo.li.opal.uidemo.views.NavigationBar;
import demo.li.opal.uidemo.views.TuitionCard;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);

        NavigationBar nav = findViewById(R.id.nav);
        nav.setRightIconClickListener(this);

        TuitionCard card = findViewById(R.id.tuition_card);
        card.setButtonClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_right_icon:
                TuitionCard tc = (TuitionCard)findViewById(R.id.tuition_card);
                tc.switchCardState();
                break;
            case R.id.nav_left_icon:
                finish();
                break;
            case R.id.tuition_btn:
                Toast.makeText(this, "Clicked! Do Sth.", Toast.LENGTH_SHORT).show();
        }
    }
}
