package com.example.artcom.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.artcom.test.adapter.ImageAdapter;
import com.example.artcom.test.adapter.ListViewHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ListTestActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

    private DisplayMetrics metrics;
    private ListView mListView;
    private ImageAdapter mAdapter;
    private ListViewHandler<String> mListViewHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mListView = new ListView(this);
        mListView.setFadingEdgeLength(0);
        mListView.setOnItemClickListener(this);

        initImageListView();
        setContentView(mListView);
        mListViewHandler = new ListViewHandler<String>(mListView, mAdapter);
     }

    private void initImageListView() {
        List<String> imageUris = new LinkedList<String>(Arrays.asList(Constants.images));

        mAdapter = new ImageAdapter(imageUris, this);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        mListViewHandler.removeItem(view, item);
//        mAdapter.notifyDataSetChanged();
    }
}
