package com.imandroid.financemanagement.util;

public class Constant {

    public static final String CHECK_VERSION_URL = "https://drive.google.com/u/0/uc?id=1k--yZwlZGysnnQ06SJFTw4wlkYGvQFYq&export=download";
    public static final String URL_KEY = "URL_KEY";
    public static final String RECEIVER_KEY = "RECEIVER_KEY";
    public static final String RESULT_KEY = "RESULT_KEY";
    public static final String FIRST_RUN_KEY = "FIRST_RUN_KEY";
    public static final String SHOW_UPDATE_KEY = "SHOW_UPDATE_KEY";
    public enum EXPENDITURE_CATEGORIES {
         Subscription
        ,Entertainment
        ,Food
        ,Transport
        ,Others}
    public enum TIME_PERIOD {
        DAILY
        ,WEEKLY
        ,MONTHLY
    }

    public enum LOADING_SATES {
        RUNNING
        ,FINISHED
        ,ERROR
    }


}
