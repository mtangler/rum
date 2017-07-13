package com.app.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trikita.log.Log;

public class QString {

    HashMap<String, String> hashMap = new HashMap<String, String>();
    public String customHost = "";
    public String apiName = "";
    public boolean showLoading = true;
    public boolean hideBadStatusMessage = false;
    public int callTimeout = 120 * 1000;

    public QString() {
        this.showLoading = true;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getCustomHost() {
        return customHost;
    }

    public void setCustomHost(String customHost) {
        this.customHost = customHost;
    }

    public void put(String k, String v) {
        hashMap.put(k, v);
    }

    public String buildQuery() {

        String query = "?";
        int i = 0;

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

            if (i != 0) {
                query += "&";
            }

            query += entry.getKey() + "=" + entry.getValue();
            i = 1;

        }

        Log.e(Globals.tag, query);
        return query;
    }

    public String getUrl() {
        String u = WebMan.FINAL_API_URL;
        if (customHost != "") {
            u = customHost;
        }
        return u + apiName + buildQuery();
    }

    public Map<String, List<String>> getBodyParams() {
        Map<String, List<String>> params = new HashMap<String, List<String>>();

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            params.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }

        return params;
    }
}
