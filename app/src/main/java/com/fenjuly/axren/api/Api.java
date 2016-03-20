package com.fenjuly.axren.api;

import com.fenjuly.axren.model.PublicStatuses;
import com.fenjuly.axren.model.Statuses;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by liurongchan on 16/3/16.
 */
public interface Api {

    public static final String URL = "https://api.weibo.com/2";

    @GET("/statuses/public_timeline.json")
    public Observable<PublicStatuses> getPublicTimeLine(@Query("access_token") String access_token);

    @GET("/statuses/bilateral_timeline.json")
    public Observable<Statuses> getTimeLine(@Query("access_token") String access_token);
}
