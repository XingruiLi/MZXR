package com.example.a40782.mqtt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class StartActivity extends Activity {
    ViewFlipper viewFlipper = null;
    float startX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        MyApp.sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        Button login = (Button) findViewById(R.id.start_bt_login);
        Button signup = (Button) findViewById(R.id.start_bt_signup);
        Button skip = (Button)findViewById(R.id.start_bt_skip);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(StartActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login.setOnTouchListener(new TouchDark());
        signup.setOnTouchListener(new TouchDark());
        skip.setOnTouchListener(new TouchDark());

        init();
    }

    private void init() {
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:

                if (event.getX() > startX) { // 向右滑动
                    viewFlipper.setInAnimation(this, R.anim.in_leftright);
                    viewFlipper.setOutAnimation(this, R.anim.out_leftright);
                    viewFlipper.showNext();
                } else if (event.getX() < startX) { // 向左滑动
                    viewFlipper.setInAnimation(this, R.anim.in_rightleft);
                    viewFlipper.setOutAnimation(this, R.anim.out_rightleft);
                    viewFlipper.showPrevious();
                }
                break;
        }

        return super.onTouchEvent(event);
    }


}
