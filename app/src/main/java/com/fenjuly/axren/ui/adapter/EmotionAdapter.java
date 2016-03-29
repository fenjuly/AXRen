package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fenjuly.axren.App;
import com.fenjuly.axren.R;
import com.fenjuly.axren.utils.TaskUtils;

import org.aisen.android.common.utils.FileUtils;
import org.aisen.android.component.bitmaploader.BitmapLoader;
import org.aisen.android.component.bitmaploader.core.MyBitmap;

import java.util.List;
import java.util.Properties;

/**
 * Created by liurongchan on 16/3/28.
 */
public class EmotionAdapter extends BaseAdapter {

    Context mContext;
    Properties properties;
    List<String> emotion_names;
    OnEmotionSelectedListener l;

    public EmotionAdapter(Context mContext, Properties properties, List<String> emotion_names) {
        this.mContext = mContext;
        this.properties = properties;
        this.emotion_names = emotion_names;
    }

    @Override
    public int getCount() {
        if (emotion_names != null) {
            return emotion_names.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (emotion_names != null) {
            return emotion_names.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.emotion_item, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.emotion_item);
            Holder holder = new Holder(imageView);
            convertView.setTag(holder);
        }
        final Holder holder = (Holder) convertView.getTag();
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Bitmap>() {
            String name;
            @Override
            protected Bitmap doInBackground(Void... params) {
                name = emotion_names.get(position);
                String value = properties.getProperty(name);
                byte[] emotion = null;
                try {
                    emotion = FileUtils.readStreamToBytes(App.getContext().getAssets().open(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (emotion == null)
                    return null;

                MyBitmap mb = BitmapLoader.getInstance().getImageCache().getBitmapFromMemCache(name, null);
                Bitmap b = null;
                if (mb != null) {
                    b = mb.getBitmap();
                }
                else {
                    b = BitmapFactory.decodeByteArray(emotion, 0, emotion.length);

                    // 添加到内存中
                    BitmapLoader.getInstance().getImageCache().addBitmapToMemCache(name, null, new MyBitmap(b, name));
                }
                return b;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.imageView.setImageBitmap(bitmap);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        l.onOnEmotionSelected(name);
                    }
                });
            }
        });
        return convertView;
    }

    public void setOnEmotionSeLectedListener(OnEmotionSelectedListener l) {
        this.l = l;
    }

    public interface OnEmotionSelectedListener {
        public void onOnEmotionSelected(String name);
    }

    class Holder {
        ImageView imageView;

        public Holder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
