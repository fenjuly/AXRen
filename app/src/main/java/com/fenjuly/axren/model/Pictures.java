package com.fenjuly.axren.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liurongchan on 16/3/22.
 */
public class Pictures implements Serializable{

    List<Picture> pictures;

    public Pictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "Pictures{" +
                "pictures=" + pictures +
                '}';
    }
}
