package com.fenjuly.axren.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.Comment;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.ui.view.AisenTextView;
import com.fenjuly.axren.utils.DensityUtils;
import com.fenjuly.axren.utils.TaskUtils;

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
 * Created by liurongchan on 16/3/24.
 */
public class CommentAndRepostAdapter extends RecyclerView.Adapter<CommentAndRepostAdapter.CommentAndRepostHolder> {

    private Context mContext;

    private Drawable mDefaultImageDrawable;

    private List<Comment> comments;

    public CommentAndRepostAdapter(Context mContext, List<Comment> comments) {
        this.mContext = mContext;
        this.comments = comments;
    }

    @Override
    public CommentAndRepostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentAndRepostHolder(LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommentAndRepostHolder holder, int position) {
        if (comments != null) {
            final Comment comment = comments.get(position);
            if (comment != null) {
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        PopupMenu popup = new PopupMenu(mContext, holder.menu);
                        popup.getMenuInflater().inflate(R.menu.comment_and_repost, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.comment:
                                        Snackbar.make(v, "评论成功", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        break;
                                    case R.id.repost:
                                        Snackbar.make(v, "转发成功", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });
                TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, List<String>>() {
                    @Override
                    protected List<String> doInBackground(Void... params) {
                        List<String> timeandplatform = new ArrayList<>();
                        try {
                            String stringDate = comment.getCreated_at();
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss Z yyyy", Locale.US);
                            Date date =sdf.parse(stringDate);
                            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            timeandplatform.add(sdf.format(date));
                            String  str = comment.getSource();
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
                holder.text.setContent(comment.getText());
                User user = comment.getUser();
                if (user != null) {
                    holder.name.setText(user.getName());
                    mDefaultImageDrawable = new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimary));
                    ImageCacheManager.loadImage(user.getAvatar_large(), ImageCacheManager
                            .getImageListener(holder.avatar, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(mContext, 40));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (comments != null) {
            return comments.size();
        }
        return 0;
    }

    class CommentAndRepostHolder extends RecyclerView.ViewHolder{

        CircleImageView avatar;
        TextView name;
        TextView time;
        TextView platform;
        AisenTextView text;
        ImageView menu;

        public CommentAndRepostHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            platform = (TextView) itemView.findViewById(R.id.platform);
            text = (AisenTextView) itemView.findViewById(R.id.text);
            menu = (ImageView) itemView.findViewById(R.id.menu);
        }
    }
}
