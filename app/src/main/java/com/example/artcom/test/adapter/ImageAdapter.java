package com.example.artcom.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.artcom.test.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ImageAdapter extends BaseAdapter implements ModifiableAdapter<String> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private int mCacheSize = (int) ((Runtime.getRuntime().maxMemory() / 1024) / 8);
    private LruCache<String, Bitmap> mCache;
    private HashMap<String, Integer> mIdMap;
    private List<String> mImageUris;


    private class ViewHolder {
        ImageView displayImage;
        AsyncTask imageLoaderTask;
        Animation animation;
        int followUpAnimation = -1;

        public void runAnimation(int animationResource) {
            if(animation != null) {
                followUpAnimation = animationResource;
//                animation.cancel();
                return;
            }

            if(animationResource == followUpAnimation) {
                followUpAnimation = -1;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, animationResource);
            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(followUpAnimation != -1) {
                        runAnimation(followUpAnimation);
                    }
                    animation = null;
                    displayImage.setHasTransientState(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            animation = animation;
            displayImage.setHasTransientState(true);
            displayImage.startAnimation(animation);
        }

    }

    public ImageAdapter(List<String> imageUris, Context context) {
        mImageUris = imageUris;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mIdMap = new HashMap<String, Integer>();
        int length = mImageUris.size();
        for (int i = 0; i < length; i++) {
            mIdMap.put(mImageUris.get(i), i);
        }
        mCache = new LruCache<String, Bitmap>(mCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public int getCount() {
        return mImageUris.size();
    }

    @Override
    public String getItem(int i) {
        return mImageUris.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mIdMap.get(getItem(i));
    }

    @Override
    public boolean hasStableIds() {
        return true;
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

//        viewHolder.runAnimation(R.anim.push_left_out);

        if(viewHolder.imageLoaderTask != null) {
            viewHolder.imageLoaderTask.cancel(true);
        }
        viewHolder.imageLoaderTask = new ImageLoadTask(viewHolder).execute(mImageUris.get(i));

        return convertView;
    }

    @Override
    public void remove(String item) {
        mImageUris.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void add(String item) {
        mImageUris.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAtIndex(String item, int index) {
        mImageUris.add(index, item);
        notifyDataSetChanged();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mCache.get(key);
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        private final ViewHolder mViewHolder;

        public ImageLoadTask(ViewHolder holder) {
            mViewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
//            Bitmap bitmap;
            Bitmap bitmap = getBitmapFromMemCache(imageUrl);
            if(bitmap != null) {
                return bitmap;
            }
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                addBitmapToMemoryCache(imageUrl, bitmap);
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
            mViewHolder.imageLoaderTask = null;
//            mViewHolder.runAnimation(R.anim.push_left_in);

        }
    }
}
