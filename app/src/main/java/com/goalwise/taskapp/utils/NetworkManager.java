package com.goalwise.taskapp.utils;

import android.content.Context;

import com.goalwise.taskapp.interfaces.NetworkCallBack;
import com.goalwise.taskapp.taskApp.GlobalApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NetworkManager {
    private final String TAG_SEARCH_TEXT = "keyword";
    private final String TAG_API_KEY = "GQCqJDU1hZ3e4aXOhLG905HbKoiBV1ZU6ipCB9Oc";
    private final String REQUEST_FETCH_FUNDS_URL = "https://gord6bd0zi.execute-api.ap-southeast-1.amazonaws.com/dev/search";
    private Context superContext;
    private MediaType JSON;

    public NetworkManager(Context superContext) {
        this.superContext = superContext;
        JSON = MediaType.parse("application/json; charset=utf-8");
    }

    public void fetchFund(String searchText, final NetworkCallBack networkCallBack) {
        JSONObject fundSearchJson = new JSONObject();
        try {
            fundSearchJson.put(TAG_SEARCH_TEXT, searchText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            RequestBody requestBody = RequestBody.create(JSON, fundSearchJson.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(REQUEST_FETCH_FUNDS_URL)
                    .post(requestBody)
                    .addHeader("x-api-key", TAG_API_KEY)
                    .build();
            GlobalApp.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException ioException) {
                    GlobalApp.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            networkCallBack.onError(ioException);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                    final String stringResponse = response.body().string();
                    Object[] returnObject = new Object[2];
                    try {
                        returnObject = new JsonUtils(superContext).parseRequestResponse(new JSONObject(stringResponse));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final Object[] finalReturnObject = returnObject;
                    GlobalApp.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            networkCallBack.onSuccess(-1, finalReturnObject);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
