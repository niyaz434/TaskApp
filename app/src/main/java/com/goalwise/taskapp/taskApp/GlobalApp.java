package com.goalwise.taskapp.taskApp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import okhttp3.OkHttpClient;

public class GlobalApp extends Application {
    private static GlobalApp mGlobalApp = null;

    private final String TAG = "GlobalApp";
    private Snackbar snackbar;
    private Toast toast;
    private Handler mHandler = null;
    private OkHttpClient okHttpClient;

    public static synchronized GlobalApp getInstance() {
        if (mGlobalApp == null) {
            return mGlobalApp = new GlobalApp();
        }
        return mGlobalApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalApp = this;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void globalToast(String message, Context globalContext) {
        try {
            if (message != null && !message.equals("")) {
                if (toast == null && globalContext != null) {
                    toast = Toast.makeText(globalContext, "", Toast.LENGTH_LONG);
                }
                if (toast != null) {
                    toast.setText(message);
                    toast.show();
                }

            } else if (message != null && message.equals("") && toast != null) {
                toast.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissGlobalSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            snackbar = null;
        }
    }

    public void dismissGlobalToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    public int globalSnackbar(final View view, final String message, final int length, final String actionMessage, final View.OnClickListener mOnClickListener) {
        int returnValue = 0;
        try {
            if (message != null && !message.equals("")) {
                if (snackbar == null && view != null) {
                    snackbar = Snackbar.make(view, message, length);
                }
                if (snackbar != null) {
                    if (!TextUtils.isEmpty(actionMessage) && mOnClickListener != null && snackbar != null) {
                        snackbar.setAction(actionMessage, mOnClickListener);
                    }
                    snackbar.setText(message);
                    snackbar.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /*common global instance of okhttp for the app */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }

    /**
     * common global instance of handler for the app
     *
     * @return handler instance
     */
    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public void removeGlobalToast() {
        if (toast != null) {
            toast.cancel();
        }
        toast = null;

    }
}
