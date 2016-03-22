package com.fenjuly.axren.model;

import java.io.Serializable;

/**
 * Created by liurongchan on 16/3/16.
 */
public class Picture implements Serializable{

    String thumbnail_pic;

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "thumbnail_pic='" + thumbnail_pic + '\'' +
                '}';
    }
}
