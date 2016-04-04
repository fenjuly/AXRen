package com.fenjuly.axren.api;

import com.fenjuly.axren.model.Comment;
import com.fenjuly.axren.model.Comments;
import com.fenjuly.axren.model.PublicStatuses;
import com.fenjuly.axren.model.Status;
import com.fenjuly.axren.model.Statuses;
import com.fenjuly.axren.model.User;
import com.fenjuly.axren.model.Users;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by liurongchan on 16/3/16.
 */
public interface Api {

    public static final String URL = "https://api.weibo.com/2";

    @GET("/statuses/public_timeline.json")
    Observable<PublicStatuses> getPublicTimeLine(@Query("access_token") String access_token);

    @GET("/statuses/friends_timeline.json")
    Observable<Statuses> getTimeLine(@Query("access_token") String access_token, @Query("page") int page);

    @GET("/comments/show.json")
    Observable<Comments> getWeiBoComments(@Query("access_token") String access_token, @Query("id") String id);

    @FormUrlEncoded
    @POST("/comments/create.json")
    Observable<Comment> replyComment(@Field("access_token") String access_token, @Field("id") String id,
                                     @Field("comment") String comment, @Field("comment_ori") int comment_ori);

    @FormUrlEncoded
    @POST("/comments/reply.json")
    Observable<Comment> commentReply(@Field("access_token") String access_token, @Field("cid") String cid,
                                     @Field("id") String id, @Field("comment") String comment,
                                     @Field("comment_ori") int comment_ori);

    @FormUrlEncoded
    @POST("/statuses/repost.json")
    Observable<Status> repostWeiBo(@Field("access_token") String access_token, @Field("id") String id,
                                   @Field("status") String status, @Field("is_comment") int is_comment);

    @GET("/users/show.json")
    Observable<User> getUserProfile(@Query("access_token") String access_token, @Query("uid") String uid,
                                    @Query("screen_name") String screen_name);

    @GET("/users/show.json")
    Observable<User> getUserProfile(@Query("access_token") String access_token, @Query("uid") String uid);

    Observable<Users> getFansCount(@Query("access_token") String access_token, @Query("uid") String uid);
}
