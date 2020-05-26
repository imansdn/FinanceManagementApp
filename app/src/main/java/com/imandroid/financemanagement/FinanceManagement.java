package com.imandroid.financemanagement;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.imandroid.financemanagement.screen.MainActivity;

import java.util.concurrent.Executor;

import timber.log.Timber;

public class FinanceManagement extends Application {

    public static final String TAG = "Firebase";
    public static final String TOPIC_FIREBASE_UDACITY = "Udacity";

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_FIREBASE_UDACITY)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String msg = getString(R.string.topic_subscribed_msg,TOPIC_FIREBASE_UDACITY);
                            Timber.d(msg);
                        }
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG,"getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Timber.d(getString(R.string.token_is), token);
                    }
                });


    }

}
