package com.app.core;

import org.json.JSONObject;

public interface ApiCallBack {

    void onReceiveJSON(JSONObject json);

    void onResponse(int type, String resp);
}
