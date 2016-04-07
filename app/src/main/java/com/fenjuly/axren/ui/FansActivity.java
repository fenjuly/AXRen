package com.fenjuly.axren.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.model.Users;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.ui.adapter.FansAadapter;
import com.fenjuly.axren.utils.AccessTokenKeeper;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/4/4.
 */
public class FansActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FansAadapter fansAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        progressDialog = ProgressDialog.show(this, "系统提示", "正在加载列表...");
        loadData();
    }

    private void loadData() {
        RetrofitTool.getInstance().getFansCount(AccessTokenKeeper.readAccessToken(this).getToken(), AccessTokenKeeper.readAccessToken(this).getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Users>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Users users) {
                        if (users != null) {
                            List<User> userList = users.getUsers();
                            fansAdapter = new FansAadapter(FansActivity.this, userList);
                            recyclerView.setAdapter(fansAdapter);
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
