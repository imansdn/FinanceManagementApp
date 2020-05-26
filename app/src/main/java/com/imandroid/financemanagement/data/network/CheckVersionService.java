package com.imandroid.financemanagement.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.imandroid.financemanagement.util.Constant;

import java.io.IOException;

import timber.log.Timber;

public class CheckVersionService extends IntentService {

    public CheckVersionService() {
        super("CheckVersionServiceThread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null){
            final ResultReceiver receiver = intent.getParcelableExtra(Constant.RECEIVER_KEY);
            String url = extras.getString(Constant.URL_KEY);
            receiver.send(Constant.LOADING_SATES.RUNNING.ordinal(), Bundle.EMPTY);
            Bundle b = new Bundle();
            try {
                String response = NetworkConnectionHelper.getResponseFromHttpUrl(url);
                VersionDTO versionDTO = JsonParserHelper.parseGenresJson(response);
                b.putParcelable(Constant.RESULT_KEY,versionDTO);
                receiver.send(Constant.LOADING_SATES.FINISHED.ordinal(), b);
                Timber.i("onHandleIntent: "+response);

            } catch (IOException e) {
                e.printStackTrace();
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(Constant.LOADING_SATES.ERROR.ordinal(), b);
                Timber.i("onHandleIntent: Error- can't get data "+e.getMessage());

            }
        }
    }
}
