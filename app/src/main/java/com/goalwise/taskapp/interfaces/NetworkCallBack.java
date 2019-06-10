package com.goalwise.taskapp.interfaces;

public interface NetworkCallBack {

    void onSuccess(int type, Object... object);

    void onError(Exception exception);

}
