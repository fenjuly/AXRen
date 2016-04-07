package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.ui.ProfileActivity;
import com.fenjuly.axren.utils.DensityUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liurongchan on 16/4/4.
 */
public class FansAadapter extends RecyclerView.Adapter<FansAadapter.FansHolder>{

    Context mContext;
    List<User> users;

    public FansAadapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public FansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FansHolder(LayoutInflater.from(mContext).inflate(R.layout.fans_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FansHolder holder, int position) {
        if (users != null) {
            final User user = users.get(position);
            holder.name.setText(user.getName());
            if (user.getFollowing()) {
                holder.focus_unfocus.setText("取消关注");
            } else {
                holder.focus_unfocus.setText("关注");
            }
            Drawable mDefaultImageDrawable;
            mDefaultImageDrawable = new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimary));
            ImageCacheManager.loadImage(user.getAvatar_large(), ImageCacheManager
                    .getImageListener(holder.avatar, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(mContext, 40));
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("id", user.getIdstr());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    class FansHolder extends RecyclerView.ViewHolder {
        View v;
        CircleImageView avatar;
        TextView name;
        TextView focus_unfocus;
        public FansHolder(View itemView) {
            super(itemView);
            v = itemView;
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            focus_unfocus = (TextView) itemView.findViewById(R.id.focus_unfocus);
        }
    }
}
