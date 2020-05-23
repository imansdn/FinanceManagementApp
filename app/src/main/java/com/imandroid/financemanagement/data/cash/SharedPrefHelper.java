package com.imandroid.financemanagement.data.cash;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static final String DAILY_BUDGET = "DAILY_BUDGET";
    private static final String WEEKLY_BUDGET = "WEEKLY_BUDGET";
    private static final String MONTHLY_BUDGET = "MONTHLY_BUDGET";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    private static  SharedPrefHelper instance;

    private SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (instance == null){
            instance = new SharedPrefHelper(context);
        }

        return instance;
    }


    public  String read(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public  boolean read(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }
    public  Integer read(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public  void write(String key, String value) {
        editor.putString(key, value).apply();
    }

    public  void write(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public  void write(String key, Integer value) {
        editor.putInt(key, value).apply();
    }

    public  void remove(String key){
        editor.remove(key).apply();
    }

}
