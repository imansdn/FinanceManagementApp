package com.imandroid.financemanagement.data.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.google.gson.annotations.SerializedName;

public class MyResultReceiver extends ResultReceiver {
    @SerializedName("localReceiver")
    private Receiver mReceiver;

    public MyResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
