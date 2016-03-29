package com.fenjuly.axren.model;

/**
 * Created by liurongchan on 16/3/29.
 */
public class ReplyComment {

    String access_token;
    String id;
    String comment;
    int comment_ori;

    public ReplyComment(String access_token, String id, String comment, int comment_ori) {
        this.access_token = access_token;
        this.id = id;
        this.comment = comment;
        this.comment_ori = comment_ori;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getComment_ori() {
        return comment_ori;
    }

    public void setComment_ori(int comment_ori) {
        this.comment_ori = comment_ori;
    }
}
