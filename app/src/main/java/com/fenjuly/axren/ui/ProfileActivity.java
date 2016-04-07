package com.fenjuly.axren.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.utils.AccessTokenKeeper;
import com.fenjuly.axren.utils.DensityUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/3/30.
 */
public class ProfileActivity extends AppCompatActivity {

    String id;

    CircleImageView imageView;
    TextView name;
    TextView place;
    TextView fans;
    TextView follow;
    TextView post;
    TextView description;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView = (CircleImageView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        place = (TextView) findViewById(R.id.place);
        fans = (TextView) findViewById(R.id.fans_text);
        follow = (TextView) findViewById(R.id.follow_text);
        post = (TextView) findViewById(R.id.post_text);
        description = (TextView) findViewById(R.id.description);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                if (percentage > 0.75) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        loadUserProfile();
        fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FansActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserProfile() {
        progressDialog = ProgressDialog.show(this, "系统提示", "正在加载，请稍后...");
        RetrofitTool.getInstance().getUserProfile(AccessTokenKeeper.readAccessToken(this).getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            name.setText(user.getName());
                            place.setText(user.getLocation());
                            fans.setText(user.getFollowers_count());
                            follow.setText(user.getFriends_count());
                            post.setText(user.getStatuses_count());
                            description.setText(user.getDescription());
                            Drawable mDefaultImageDrawable;
                            mDefaultImageDrawable = new ColorDrawable(ProfileActivity.this.getResources().getColor(R.color.colorPrimary));
                            ImageCacheManager.loadImage(user.getAvatar_large(), ImageCacheManager
                                    .getImageListener(imageView, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(ProfileActivity.this, 40));
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
