package com.imandroid.financemanagement.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.widget.RemoteViews;

import com.imandroid.financemanagement.R;


import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BalanceStatementWidget extends AppWidgetProvider {

    public static final String UPDATE_ACTION = "UPDATE_ACTION";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.balance_statement_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Timber.i("UPDATE_ACTION widget is = "+intent.getAction());

        if (UPDATE_ACTION.equals(intent.getAction())){
            context.startService(new Intent(context, UpdateWidgetService.class));

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        context.startService(new Intent(context, UpdateWidgetService.class));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

