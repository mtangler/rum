package com.app.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import com.app.rum.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import trikita.log.Log;

public class WebMan {

    public static int TP_NO_INTERNET = 1;
    public static int TP_STRING_RESPONSE = 2;
    public static int TP_BAD_STATUS = 3;

    // webservice manager
    private static String live = "https://your_api_backend_url";

    public static String FINAL_API_URL = live;
    private static String host = FINAL_API_URL + "";

    // imp strings
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static String method = POST;
    private static boolean SYNC_DATA = true;

    // useful strings
    private static String serverError = "Server Error Please Try Again";
    private static String noNet = "Please turn on internet connection.";
    private static String TAG = "WEBMAN";

    // statics
    public static Context appctx = null;
    private static Activity activityContext = null;

    public static void setActivity(Activity mactivityContext) {
        activityContext = mactivityContext;
    }

    public static void getJsonFromApi(final Context con, final QString q, final ApiCallBack callBack) {
        try {

            appctx = con;
            if (!checkNet()) {
                callBack.onResponse(TP_NO_INTERNET, "");

                if (SYNC_DATA) {
                    Log.i("Syncing with latest response");
                    callBack.onReceiveJSON(new JSONObject(SyncerMan.getLatestResponse(q.getUrl())));
                }

                return;
            }

            String url = WebMan.host;
            if (q.getCustomHost() != "") {
                url = q.getCustomHost();
            }

            final SweetAlertDialog pDialog = new SweetAlertDialog(con, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(con.getString(R.string.loading));
            pDialog.setCancelable(false);

            if (q.showLoading)
                pDialog.show();

            //for api name eg: user/login
            url += q.getApiName();

            if (WebMan.method.equals(GET)) {

                //USING GET
                url += q.buildQuery();
                Log.e(TAG, WebMan.method + " " + url);
                Ion.with(con).load(WebMan.method, url).setTimeout(q.callTimeout)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception arg0, String arg1) {
                                pDialog.dismiss();
                                postResponse(q, arg0, arg1, callBack);
                            }
                        });
            } else {

                //USING POST
                Log.e(TAG, WebMan.method + " " + url + q.buildQuery());
                Ion.with(con).load(WebMan.method, url).setTimeout(q.callTimeout)
                        .setBodyParameters(q.getBodyParams())
                        .asByteArray()
                        .setCallback(new FutureCallback<byte[]>() {
                            @Override
                            public void onCompleted(Exception e, byte[] result) {
                                try {

                                    pDialog.dismiss();
                                    if (result.length > 0) {
                                        String data = new String(result, "UTF-8");
                                        postResponse(q, e, data, callBack);
                                    }

                                } catch (Exception ea) {
                                    ea.printStackTrace();
                                }
                            }

                        });
            }

        } catch (Exception e) {
            LogEx.print(e);
            toast(WebMan.serverError);
        }
    }

    public static void postDataWithFile(final Context con, final QString q, ArrayList<Part> filesParts, final ApiCallBack callBack) {
        try {

            appctx = con;
            if (!checkNet()) {
                callBack.onResponse(TP_NO_INTERNET, "");
                return;
            }

            String url = WebMan.host;
            if (q.getCustomHost() != "") {
                url = q.getCustomHost();
            }

            final SweetAlertDialog pDialog = new SweetAlertDialog(con, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(con.getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();

            //for api name eg: user/login
            url += q.getApiName();

            //USING POST
            Log.e(TAG, WebMan.method + " " + url + q.buildQuery());

            Ion.with(con)
                    .load(WebMan.POST, url)
                    .setMultipartParameters(q.getBodyParams())
                    .addMultipartParts(filesParts)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {

                                pDialog.dismiss();
                                postResponse(q, e, result.toString(), callBack);

                            } catch (Exception ea) {
                                ea.printStackTrace();
                            }
                        }
                    });

        } catch (Exception e) {
            LogEx.print(e);
            toast(WebMan.serverError);
        }
    }

    private static void postResponse(QString q, Exception arg0, String arg1, final ApiCallBack callBack) {
        try {

            if (arg0 != null) {
                arg0.printStackTrace();
                toast(WebMan.serverError);
                return;
            }

            Log.e(TAG, "Response : " + arg1);

            JSONObject json = new JSONObject(arg1);
            if (json.optString("status").equals("1")) {

                callBack.onReceiveJSON(json);

                if (SYNC_DATA) {
                    SyncerMan.putResponseForSync(q.getUrl().trim().toLowerCase(), json.toString());
                }

            } else {

                callBack.onResponse(TP_BAD_STATUS, arg1);

                if (json.optString("message").length() > 1 && !q.hideBadStatusMessage) {
                    toast(json.optString("message"));
                }
            }

        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public static boolean checkNet() {

        boolean ret = isNetworkAvailable();
        if (!ret && !noNetMsgSeen) {
            toast(noNet);
            noNetMsgSeen = true;
        } else {
            if (ret)
                noNetMsgSeen = false;
        }

        return ret;
    }

    public static boolean publicCheckNet() {
        boolean ret = isNetworkAvailable();
        return ret;
    }

    private static boolean isNetworkAvailable() {

        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) appctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            ret = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
        }

        return ret;
    }

    public static void toast(final String msg) {
        Log.e(TAG, msg);

        try {
            Toast toast = Toast.makeText(appctx, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public static boolean noNetMsgSeen = false;
}



