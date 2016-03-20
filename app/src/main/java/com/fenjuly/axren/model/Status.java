package com.fenjuly.axren.model;

import java.util.List;

/**
 * Created by liurongchan on 16/3/16.
 */
public class Status {

    /**
     * {
     "created_at": "Wed Mar 16 18:28:50 +0800 2016",
     "id": 3953721522745693,
     "mid": "3953721522745693",
     "idstr": "3953721522745693",
     "text": "[直播ing] #被爱背叛终生孤寂#，点此进入>> http://t.cn/RGBBtl6",
     "textLength": 60,
     "source_allowclick": 0,
     "source_type": 1,
     "source": "<a href=\"http://app.weibo.com/t/feed/3ZjAmC\" rel=\"nofollow\">映客</a>",
     "favorited": false,
     "truncated": false,
     "in_reply_to_status_id": "",
     "in_reply_to_user_id": "",
     "in_reply_to_screen_name": "",
     "pic_urls": [
     {
     "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/6950b929jw1f1yvr80jtaj21kw1kwwsk.jpg"
     }
     ],
     "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/6950b929jw1f1yvr80jtaj21kw1kwwsk.jpg",
     "bmiddle_pic": "http://ww3.sinaimg.cn/bmiddle/6950b929jw1f1yvr80jtaj21kw1kwwsk.jpg",
     "original_pic": "http://ww3.sinaimg.cn/large/6950b929jw1f1yvr80jtaj21kw1kwwsk.jpg",
     "geo": null,
     "user": {
     "id": 1766897961,
     "idstr": "1766897961",
     "class": 1,
     "screen_name": "wangleWKer",
     "name": "wangleWKer",
     "province": "34",
     "city": "6",
     "location": "安徽 淮北",
     "description": "这世上除了神灵与野兽，我们都是害怕孤独的💕",
     "url": "",
     "profile_image_url": "http://tp2.sinaimg.cn/1766897961/50/5751986314/0",
     "profile_url": "u/1766897961",
     "domain": "",
     "weihao": "",
     "gender": "f",
     "followers_count": 98,
     "friends_count": 327,
     "pagefriends_count": 1,
     "statuses_count": 191,
     "favourites_count": 65,
     "created_at": "Sat Jul 03 01:26:04 +0800 2010",
     "following": false,
     "allow_all_act_msg": false,
     "geo_enabled": true,
     "verified": false,
     "verified_type": -1,
     "remark": "",
     "ptype": 0,
     "allow_all_comment": true,
     "avatar_large": "http://tp2.sinaimg.cn/1766897961/180/5751986314/0",
     "avatar_hd": "http://tva3.sinaimg.cn/crop.0.0.1242.1242.1024/6950b929jw8f1k3ew66wej20yi0yiq61.jpg",
     "verified_reason": "",
     "verified_trade": "",
     "verified_reason_url": "",
     "verified_source": "",
     "verified_source_url": "",
     "follow_me": false,
     "online_status": 0,
     "bi_followers_count": 32,
     "lang": "zh-cn",
     "star": 0,
     "mbtype": 0,
     "mbrank": 0,
     "block_word": 0,
     "block_app": 0,
     "credit_score": 80,
     "user_ability": 0,
     "urank": 14
     },
     "reposts_count": 0,
     "comments_count": 0,
     "attitudes_count": 0,
     "isLongText": false,
     "mlevel": 0,
     "visible": {
     "type": 0,
     "list_id": 0
     },
     "biz_feature": 0,
     "page_type": 32,
     "darwin_tags": [],
     "hot_weibo_tags": [],
     "text_tag_tips": [],
     "userType": 0
     }
     */

    String created_at;
    String idstr;
    String text;
    String reposts_count;
    String comments_count;
    String attitudes_count;
    String source;

    User user;
    Status retweeted_status;
    List<Picture> pic_urls;

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Status retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Picture> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<Picture> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public String getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(String reposts_count) {
        this.reposts_count = reposts_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(String attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Status{" +
                "created_at='" + created_at + '\'' +
                ", idstr='" + idstr + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", pic_urls=" + pic_urls +
                '}';
    }
}
