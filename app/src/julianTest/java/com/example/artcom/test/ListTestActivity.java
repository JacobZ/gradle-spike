package com.example.artcom.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListTestActivity extends Activity {

    private DisplayMetrics metrics;
    private ListView listview;
    private BaseAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        listview = new ListView(this);
        listview.setFadingEdgeLength(0);

        initSimpleListView();
//        initImageListView();
        setContentView(listview);
     }

    private void initImageListView() {
        mAdapter = new ImageAdapter(Arrays.asList(Constants.images), this);
        listview.setAdapter(mAdapter);
    }

    private void initSimpleListView() {
        ArrayList<String> strings = new ArrayList<String>();

        for (int i = 0; i < 300; i++) {
            strings.add("Item:#" + (i + 1));
        }

        mAdapter = new MainAdapter(this, strings, metrics);
        ((MainAdapter) mAdapter).setMode(1);
        listview.setAdapter(mAdapter);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 1, 0, "TranslateAnimation1");
        menu.add(Menu.NONE, 2, 0, "TranslateAnimation2");
        menu.add(Menu.NONE, 3, 0, "ScaleAnimation");
        menu.add(Menu.NONE, 4, 0, "fade_in");
        menu.add(Menu.NONE, 5, 0, "hyper_space_in");
        menu.add(Menu.NONE, 6, 0, "hyper_space_out");
        menu.add(Menu.NONE, 7, 0, "wave_scale");
        menu.add(Menu.NONE, 8, 0, "push_left_in");
        menu.add(Menu.NONE, 9, 0, "push_left_out");
        menu.add(Menu.NONE, 10, 0, "push_up_in");
        menu.add(Menu.NONE, 11, 0, "push_up_out");
        menu.add(Menu.NONE, 12, 0, "shake");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            ((MainAdapter) mAdapter).setMode(1);
        } catch (ClassCastException e) {

        }
        return super.onOptionsItemSelected(item);
    }

}
