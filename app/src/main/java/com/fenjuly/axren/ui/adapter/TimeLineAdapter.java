package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.Picture;
import com.fenjuly.axren.model.Status;
import com.fenjuly.axren.model.Statuses;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.ui.WeiBoDetailActivity;
import com.fenjuly.axren.ui.view.AisenTextView;
import com.fenjuly.axren.utils.DensityUtils;
import com.fenjuly.axren.utils.TaskUtils;
import com.fenjuly.combinationimageview.CombinationImageView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liurongchan on 16/3/19.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineHolder> {

    private Statuses statuses;
    private Context mContext;

    private Drawable mDefaultImageDrawable;

    private static final int AVATAR_MAX_HEIGHT = 40;

    public TimeLineAdapter(Statuses statuses, Context mContext) {
        this.statuses = statuses;
        this.mContext = mContext;
    }

    @Override
    public TimeLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimeLineHolder(LayoutInflater.from(mContext).inflate(R.layout.timeline_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TimeLineHolder holder, final int position) {
        if (statuses != null) {
            List<Status> statusesList= statuses.getStatuses();
            if (statusesList != null) {
                final Status status = statusesList.get(position);
                if (status != null) {
                    holder.star.setText(status.getAttitudes_count());
                    holder.comment.setText(status.getComments_count());
                    holder.repost.setText(status.getReposts_count());

                    TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, List<String>>() {
                        @Override
                        protected List<String> doInBackground(Void... params) {
                            List<String> timeandplatform = new ArrayList<>();
                            try {
                                String stringDate = status.getCreated_at();
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss Z yyyy", Locale.US);
                                Date date =sdf.parse(stringDate);
                                sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeandplatform.add(sdf.format(date));
                                String  str = status.getSource();
                                Pattern p = Pattern.compile("<a[^>]*>(.*?)</a>");
                                Matcher m = p.matcher(str);
                                while(m.find()) {
                                    timeandplatform.add(m.group(1));
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return timeandplatform;
                        }

                        @Override
                        protected void onPostExecute(List<String> strings) {
                            super.onPostExecute(strings);
                            holder.time.setText(strings.get(0));
                            if (strings.size() > 1) {
                                holder.platform.setText(strings.get(1));
                            }
                        }
                    });


                    User user = status.getUser();
                    if (user != null) {
                        holder.name.setText(user.getName());
                        mDefaultImageDrawable = new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimary));
                        ImageCacheManager.loadImage(user.getAvatar_large(), ImageCacheManager
                                .getImageListener(holder.avatar, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(mContext, AVATAR_MAX_HEIGHT));
                    }
                    holder.text.setContent(status.getText());
                    if (status.getPic_urls() != null) {
                        holder.gridview.setAdapter(new PostPictureAdapter(mContext, status.getPic_urls()));
                        holder.gridview.setVisibility(View.VISIBLE);
                        setRetweetedInvisible(holder);
                    } else {
                        holder.gridview.setVisibility(View.GONE);
                    }

                    if (status.getRetweeted_status() != null) {
                        Status retweeted_status = status.getRetweeted_status();
                        holder.repost_comment_area.setText(retweeted_status.getAttitudes_count() + "赞 | " +
                        retweeted_status.getComments_count() + "评论 | " + retweeted_status.getReposts_count() + "转发");
                        User retweeted_user = retweeted_status.getUser();
                        if (retweeted_user != null) {
                            holder.nameandtext.setContent("@" + retweeted_user.getName() + ": " + retweeted_status.getText());
                        }
                        if (retweeted_status.getPic_urls() != null) {
                            holder.repostgridview.setAdapter(new PostPictureAdapter(mContext, retweeted_status.getPic_urls()));
                        }
                        setRetweetedVisible(holder);
                        holder.gridview.setVisibility(View.GONE);
                    } else {
                        setRetweetedInvisible(holder);
                    }
                }
            }
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WeiBoDetailActivity.class);
                intent.putExtra("weibo", (new Gson()).toJson(statuses.getStatuses().get(position)));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (statuses != null && statuses.getStatuses() != null) {
            return statuses.getStatuses().size();
        }
        return 0;
    }

    private void setRetweetedInvisible(TimeLineHolder holder) {
        holder.nameandtext.setVisibility(View.GONE);
        holder.reposticon.setVisibility(View.GONE);
        holder.repostgridview.setVisibility(View.GONE);
        holder.repost_comment_area.setVisibility(View.GONE);
    }

    private void setRetweetedVisible(TimeLineHolder holder) {
        holder.nameandtext.setVisibility(View.VISIBLE);
        holder.reposticon.setVisibility(View.VISIBLE);
        holder.repostgridview.setVisibility(View.VISIBLE);
        holder.repost_comment_area.setVisibility(View.VISIBLE);
    }

    static class TimeLineHolder extends RecyclerView.ViewHolder {
        View rootView;
        CircleImageView avatar;
        TextView name;
        TextView time;
        TextView platform;
        AisenTextView text;
        GridView gridview;
        GridView repostgridview;
        AisenTextView nameandtext;
        RelativeLayout repostitem;
        ImageView reposticon;
        TextView star;
        TextView comment;
        TextView repost;
        TextView repost_comment_area;
        public TimeLineHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            platform = (TextView) itemView.findViewById(R.id.platform);
            text = (AisenTextView) itemView.findViewById(R.id.text);
            gridview = (GridView) itemView.findViewById(R.id.gridview);
            repostgridview = (GridView) itemView.findViewById(R.id.repostgridview);
            nameandtext = (AisenTextView) itemView.findViewById(R.id.nameandtext);
            repostitem = (RelativeLayout) itemView.findViewById(R.id.repost_item);
            reposticon = (ImageView) itemView.findViewById(R.id.reposticon);
            star = (TextView) itemView.findViewById(R.id.star_count);
            comment = (TextView) itemView.findViewById(R.id.comment_count);
            repost = (TextView) itemView.findViewById(R.id.repost_count);
            repost_comment_area = (TextView) itemView.findViewById(R.id.repost_comment_area);
        }
    }
}
