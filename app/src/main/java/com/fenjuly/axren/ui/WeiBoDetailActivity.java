package com.fenjuly.axren.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.Status;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.ui.adapter.PostPictureAdapter;
import com.fenjuly.axren.ui.view.AisenTextView;
import com.fenjuly.axren.utils.DensityUtils;
import com.fenjuly.axren.utils.TaskUtils;
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
 * Created by liurongchan on 16/3/24.
 */
public class WeiBoDetailActivity extends AppCompatActivity {

    private Status status;

    private Drawable mDefaultImageDrawable;

    CircleImageView avatar;
    TextView name;
    TextView time;
    TextView platform;
    AisenTextView text;
    GridView gridview;
    GridView repostgridview;
    AisenTextView nameandtext;
    RelativeLayout repostitem;
    TextView repost_comment_area;
    ImageView reposticon;
    TextView weibo_detail_comment;
    TextView weibo_detail_repost;
    View comment_line;
    View repost_line;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_detail_activity);

        Intent intent = getIntent();
        if (intent != null) {
            status = new Gson().fromJson(intent.getStringExtra("weibo"), Status.class);
        }

        Log.e("status", status.toString());
        avatar = (CircleImageView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        platform = (TextView) findViewById(R.id.platform);
        text = (AisenTextView) findViewById(R.id.text);
        gridview = (GridView) findViewById(R.id.gridview);
        repostgridview = (GridView) findViewById(R.id.repostgridview);
        nameandtext = (AisenTextView) findViewById(R.id.nameandtext);
        repostitem = (RelativeLayout) findViewById(R.id.repost_item);
        repost_comment_area = (TextView) findViewById(R.id.repost_comment_area);
        reposticon = (ImageView) findViewById(R.id.reposticon);
        weibo_detail_comment = (TextView) findViewById(R.id.weibo_detail_comment);
        weibo_detail_repost = (TextView) findViewById(R.id.weibo_detail_repost);
        comment_line = findViewById(R.id.comment_line);
        repost_line = findViewById(R.id.repost_line);

        if (status != null) {
            commentFocused();
            weibo_detail_comment.setText("评论 " + status.getComments_count());
            weibo_detail_repost.setText("转发 " + status.getReposts_count());

            weibo_detail_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentFocused();
                }
            });

            weibo_detail_repost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repostFocused();
                }
            });

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
                    time.setText(strings.get(0));
                    if (strings.size() > 1) {
                        platform.setText(strings.get(1));
                    }
                }
            });


            User user = status.getUser();
            if (user != null) {
                name.setText(user.getName());
                mDefaultImageDrawable = new ColorDrawable(this.getResources().getColor(R.color.colorPrimary));
                ImageCacheManager.loadImage(user.getAvatar_large(), ImageCacheManager
                        .getImageListener(avatar, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(this, 40));
            }
            text.setContent(status.getText());
            if (status.getPic_urls() != null) {
                gridview.setAdapter(new PostPictureAdapter(this, status.getPic_urls()));
                gridview.setVisibility(View.VISIBLE);
                setRetweetedInvisible();
            } else {
                gridview.setVisibility(View.GONE);
            }

            if (status.getRetweeted_status() != null) {
                Status retweeted_status = status.getRetweeted_status();
                repost_comment_area.setText(retweeted_status.getAttitudes_count() + "赞 | " +
                        retweeted_status.getComments_count() + "评论 | " + retweeted_status.getReposts_count() + "转发");
                User retweeted_user = retweeted_status.getUser();
                if (retweeted_user != null) {
                    nameandtext.setContent("@" + retweeted_user.getName() + ": " + retweeted_status.getText());
                }
                if (retweeted_status.getPic_urls() != null) {
                    repostgridview.setAdapter(new PostPictureAdapter(this, retweeted_status.getPic_urls()));
                }
                setRetweetedVisible();
                gridview.setVisibility(View.GONE);
            } else {
                setRetweetedInvisible();
            }
        }
    }

    private void commentFocused() {
        weibo_detail_comment.setTextColor(Color.parseColor("#484761"));
        weibo_detail_repost.setTextColor(Color.parseColor("#000000"));
        comment_line.setBackgroundColor(Color.parseColor("#484761"));
        repost_line.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private void repostFocused() {
        weibo_detail_comment.setTextColor(Color.parseColor("#000000"));
        weibo_detail_repost.setTextColor(Color.parseColor("#484761"));
        comment_line.setBackgroundColor(Color.parseColor("#ffffff"));
        repost_line.setBackgroundColor(Color.parseColor("#484761"));
    }

    private void setRetweetedInvisible() {
        nameandtext.setVisibility(View.GONE);
        reposticon.setVisibility(View.GONE);
        repostgridview.setVisibility(View.GONE);
        repost_comment_area.setVisibility(View.GONE);
    }

    private void setRetweetedVisible() {
        nameandtext.setVisibility(View.VISIBLE);
        reposticon.setVisibility(View.VISIBLE);
        repostgridview.setVisibility(View.VISIBLE);
        repost_comment_area.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weibo_detail, menu);
        return true;
    }
}
