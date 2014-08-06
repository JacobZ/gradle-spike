package com.example.artcom.test;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyActivity extends Activity implements View.OnClickListener {

    private LinearLayout mScrollView;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mScrollView = (LinearLayout) findViewById(R.id.lv_scroll_container);
        LayoutTransition transition = mScrollView.getLayoutTransition();
//        ViewPropertyAnimator animator = new ViewPropertyAnimator();
//        transition.setAnimator(LayoutTransition.APPEARING, animator);



        Button btn1 = (Button) findViewById(R.id.btn_btn1);
        btn1.setOnClickListener(this);



    }



    private void addItem() {
        View view = getLayoutInflater().inflate(R.layout.list_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_text);
        textView.setText("item " + mCount);

        mScrollView.addView(view);
        mCount++;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_btn1:
                addItem();
                break;
            case R.id.btn_btn2:
                break;
        }
    }
}
