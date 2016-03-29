package com.fenjuly.axren.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fenjuly.axren.App;
import com.fenjuly.axren.R;
import com.fenjuly.axren.model.Comment;
import com.fenjuly.axren.model.ReplyComment;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.ui.adapter.EmotionAdapter;
import com.fenjuly.axren.utils.AccessTokenKeeper;
import com.fenjuly.axren.utils.DensityUtils;
import com.fenjuly.axren.utils.TaskUtils;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/3/28.
 */
public class CommentActivity extends AppCompatActivity implements EmotionAdapter.OnEmotionSelectedListener {

    String idstr;

    ImageView emotion;
    ImageView send;
    RelativeLayout send_and_emotion;
    EditText comment_text;
    GridView emotion_gridview;
    CheckBox checkBox;

    List<String> emotion_names;

    boolean isUp = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_comment_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("评论微博");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            idstr = intent.getStringExtra("id");
        }
        emotion = (ImageView) findViewById(R.id.emotion);
        send = (ImageView) findViewById(R.id.send);
        send_and_emotion = (RelativeLayout) findViewById(R.id.send_and_emotion);
        comment_text = (EditText) findViewById(R.id.comment_text);
        emotion_gridview = (GridView) findViewById(R.id.reply_comment_gridview);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        loadEmotion();

        emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUp) {
                    send_and_emotion.setTranslationY(0 - DensityUtils.dip2px(CommentActivity.this, 200));
                    checkBox.setTranslationY(0 - DensityUtils.dip2px(CommentActivity.this, 200));
                    emotion_gridview.setVisibility(View.VISIBLE);
                    isUp = true;
                }
            }
        });

        comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp) {
                    send_and_emotion.setTranslationY(0);
                    checkBox.setTranslationY(0);
                    emotion_gridview.setVisibility(View.GONE);
                    isUp = false;
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    @Override
    public void onOnEmotionSelected(String name) {
        if (comment_text != null) {
            comment_text.append(name);
        }
    }

    private void loadEmotion() {
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, List<String>>() {
            Properties properties;
            @Override
            protected List<String> doInBackground(Void... params) {
                InputStream in;
                emotion_names = new ArrayList<>();
                try {
                    in = App.getContext().getAssets().open("emotions.properties");
                    properties = new Properties();
                    properties.load(new InputStreamReader(in, "utf-8"));
                    Set<Object> keySet = properties.keySet();

                    for (Object key : keySet) {
                        emotion_names.add(key.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return emotion_names;
            }

            @Override
            protected void onPostExecute(List<String> list) {
                super.onPostExecute(list);
                EmotionAdapter emotionAdapter = new EmotionAdapter(CommentActivity.this, properties, list);
                emotion_gridview.setAdapter(emotionAdapter);
                emotionAdapter.setOnEmotionSeLectedListener(CommentActivity.this);
            }
        });
    }

    private void sendComment() {
        String content = comment_text.getText().toString();
        if (content.equals("")) {
            Snackbar.make(comment_text, "内容为空", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (content.length() > 140) {
            Snackbar.make(comment_text, "字数多余140限制", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            int isRepost = checkBox.isChecked() == true ? 1 : 0;
            RetrofitTool.getInstance().replyComment(AccessTokenKeeper.readAccessToken(this).getToken(), idstr, content, isRepost)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Comment>() {
                        @Override
                        public void onCompleted() {
                            Log.e("commentActivity", "onCompleted");
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("commentActivity", "onError");
                            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            NotificationCompat.Builder builder=new NotificationCompat.Builder(CommentActivity.this);

                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setTicker("A new Message");
                            builder.setContentTitle("发送评论");
                            builder.setContentText("评论失败");
                            builder.setAutoCancel(true);
                            builder.setWhen(System.currentTimeMillis());
                            Notification notification=builder.build();//notify(int id,notification对象);id用来标示每个notification
                            manager.notify(1,notification);
                        }

                        @Override
                        public void onNext(Comment comment) {
                            if (comment != null) {
                                NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationCompat.Builder builder=new NotificationCompat.Builder(CommentActivity.this);

                                builder.setSmallIcon(R.mipmap.ic_launcher);
                                builder.setTicker("A new Message");
                                builder.setContentTitle("发送评论");
                                builder.setContentText("评论成功");
                                builder.setAutoCancel(true);
                                builder.setWhen(System.currentTimeMillis());
                                Notification notification=builder.build();//notify(int id,notification对象);id用来标示每个notification
                                manager.notify(1,notification);
                            }
                        }
                    });
        }
    }
}
