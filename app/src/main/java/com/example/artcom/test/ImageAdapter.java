package com.example.artcom.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private class ViewHolder {
        ImageView displayImage;
    }

    private List<String> mImageUris;

    public ImageAdapter(List<String> imageUris, Context context) {
        mImageUris = imageUris;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImageUris.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageUris.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.image_item, null);
            viewHolder.displayImage = (ImageView) convertView.findViewById(R.id.iv_image_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        new ImageLoadTask(viewHolder).execute(mImageUris.get(i));

        return convertView;
    }



    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        private final ViewHolder mViewHolder;

        public ImageLoadTask(ViewHolder holder) {
            mViewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap == null) {
                return;
            }
            mViewHolder.displayImage.setImageBitmap(bitmap);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            animation.setDuration(500);
            mViewHolder.displayImage.startAnimation(animation);
            animation = null;
        }
    }
}
