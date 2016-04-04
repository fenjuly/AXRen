package com.fenjuly.axren.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fenjuly.axren.model.User;
import com.fenjuly.axren.model.Users;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.utils.AccessTokenKeeper;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/4/4.
 */
public class FansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void loadData() {
        RetrofitTool.getInstance().getFansCount(AccessTokenKeeper.readAccessToken(this).getToken(), AccessTokenKeeper.readAccessToken(this).getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Users>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Users users) {
                        if (users != null) {
                            List<User> userList = users.getUsers();
                        }
                    }
                });
    }
}
