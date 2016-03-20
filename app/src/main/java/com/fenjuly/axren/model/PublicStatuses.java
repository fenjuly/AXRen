package com.fenjuly.axren.model;

import java.util.List;

/**
 * Created by liurongchan on 16/3/16.
 */
public class PublicStatuses {

    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }


    @Override
    public String toString() {
        return "PublicStatuses{" +
                "statuses=" + statuses +
                '}';
    }
}
