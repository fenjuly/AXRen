package com.fenjuly.axren.network;

import com.fenjuly.axren.api.Api;

/**
 * Created by liurongchan on 16/3/16.
 */
public class RetrofitTool {
    public static Api apiTool = null;

    public static Api getInstance() {
        if (apiTool == null) {
            synchronized (RetrofitTool.class) {
                if (apiTool == null) {
                    apiTool = new RetrofitService().createService(Api.class);
                }
            }
        }
        return apiTool;
    }
}
