package com.example.a40782.mqtt;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by XingruiLi on 2018/4/23.
 */


public class EditTextViewBackgroundSwitcher implements View.OnFocusChangeListener {
    int id1;
    int id2;
    Activity activity;
    EditTextViewBackgroundSwitcher(Activity activity, int id1, int id2)
    {
        this.activity = activity;
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ((EditText) v).setCursorVisible(true);
        if (hasFocus) {
            ((EditText) v).setBackground(activity.getDrawable(id1));
        } else {
            if (((EditText) v).getText().toString().isEmpty()){
                ((EditText)v).setBackground(activity.getDrawable(id2));
            }
        }
    }
}
