package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.User;

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
            User user = users.get(position);
            holder.name.setText(user.getName());
            if (user.getFollowing()) {
                holder.focus_unfocus.setText("取消关注");
            } else {
                holder.focus_unfocus.setText("关注");
            }
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
        CircleImageView avatar;
        TextView name;
        TextView focus_unfocus;
        public FansHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            focus_unfocus = (TextView) itemView.findViewById(R.id.focus_unfocus);
        }
    }
}
