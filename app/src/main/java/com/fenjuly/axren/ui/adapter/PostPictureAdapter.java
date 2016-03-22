package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.Picture;
import com.fenjuly.axren.model.Pictures;
import com.fenjuly.axren.ui.PhotoActivity;
import com.fenjuly.axren.utils.DensityUtils;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by liurongchan on 16/3/19.
 */
public class PostPictureAdapter extends BaseAdapter {

    private Context mContext;
    private List<Picture> pictures;

    private Drawable mDefaultImageDrawable;

    private static final int AVATAR_MAX_HEIGHT = 40;

    public PostPictureAdapter(Context mContext, List<Picture> pictures) {
        this.mContext = mContext;
        this.pictures = pictures;
        mDefaultImageDrawable = new ColorDrawable(mContext.getResources().getColor(R.color.defaultPictureColor));
    }

    @Override
    public int getCount() {
        if (pictures != null) {
            return pictures.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (pictures != null) {
            return pictures.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostPictureHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.post_picture_item, parent, false);
            ImageView picture = (ImageView) convertView.findViewById(R.id.picture);
            holder = new PostPictureHolder(picture);
            convertView.setTag(holder);
        }
        holder = (PostPictureHolder) convertView.getTag();
        if (pictures != null) {
            ImageCacheManager.loadImage(pictures.get(position).getThumbnail_pic(), ImageCacheManager
                    .getImageListener(holder.picture, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(mContext, AVATAR_MAX_HEIGHT));
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putExtra("index", position);
                    intent.putExtra("pictures", (new Gson()).toJson(pictures));
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    static class PostPictureHolder {
        ImageView picture;

        public PostPictureHolder(ImageView picture) {
            this.picture = picture;
        }
    }
}
