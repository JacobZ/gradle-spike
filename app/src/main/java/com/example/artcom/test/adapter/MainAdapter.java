package com.example.artcom.test.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.artcom.test.R;

import java.util.ArrayList;

public class MainAdapter extends ArrayAdapter<String> {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<String> strings;
    private DisplayMetrics metrics_;
    private int mode;

    public MainAdapter(Context context, ArrayList<String> strings,
                       DisplayMetrics metrics) {
        super(context, 0, strings);
        this.context = context;
        this.mInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.strings = strings;
        this.metrics_ = metrics;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        final String str = this.strings.get(position);
        final Holder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(
                    android.R.layout.simple_list_item_1, null);
            convertView.setBackgroundColor(0xFF202020);

            holder = new Holder();
            holder.textview = (TextView) convertView
                    .findViewById(android.R.id.text1);
            holder.textview.setTextColor(0xFFFFFFFF);
            holder.textview.setBackgroundResource(R.drawable.background);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textview.setText(str);

        Animation animation = null;

        switch (mode) {
            case 1:
                animation = new TranslateAnimation(metrics_.widthPixels / 2, 0,
                        0, 0);
                animation.setDuration(500);
                break;

            case 2:
                animation = new TranslateAnimation(0, 0, metrics_.heightPixels,
                        0);
                animation.setDuration(500);
                break;

            case 3:
                animation = new ScaleAnimation((float) 1.0, (float) 1.0,
                        (float) 0, (float) 1.0);
                animation.setDuration(500);
                break;

            case 4:
                animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                break;
            case 5:
                animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);
                break;
            case 6:
                animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_out);
                break;
            case 7:
                animation = AnimationUtils.loadAnimation(context, R.anim.wave_scale);
                break;
            case 8:
                animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
                break;
            case 9:
                animation = AnimationUtils.loadAnimation(context, R.anim.push_left_out);
                break;
            case 10:
                animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
                break;
            case 11:
                animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
                break;
            case 12:
                animation = AnimationUtils.loadAnimation(context, R.anim.shake);
                break;
            case 13:
                animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        }

        convertView.startAnimation(animation);
        animation = null;

        return convertView;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private class Holder {
        public TextView textview;
    }
}
