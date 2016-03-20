package com.fenjuly.axren.model;

/**
 * Created by liurongchan on 16/3/16.
 */
public class User {
    /**
     * {
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
     }
     */

    String idstr;
    String screen_name;
    String name;
    String location;
    String description;
    String followers_count;
    String friends_count;
    String statuses_count;
    boolean following;
    String avatar_large;
    String avatar_hd;
    String follow_me;

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(String statuses_count) {
        this.statuses_count = statuses_count;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public String getFollow_me() {
        return follow_me;
    }

    public void setFollow_me(String follow_me) {
        this.follow_me = follow_me;
    }

    @Override
    public String toString() {
        return "User{" +
                "idstr='" + idstr + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", followers_count='" + followers_count + '\'' +
                ", friends_count='" + friends_count + '\'' +
                ", statuses_count='" + statuses_count + '\'' +
                ", following=" + following +
                ", avatar_large='" + avatar_large + '\'' +
                ", avatar_hd='" + avatar_hd + '\'' +
                ", follow_me='" + follow_me + '\'' +
                '}';
    }
}
