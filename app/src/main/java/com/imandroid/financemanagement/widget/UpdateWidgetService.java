package com.imandroid.financemanagement.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.screen.MainActivity;
import com.imandroid.financemanagement.util.Constant;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class UpdateWidgetService extends IntentService {
    public static final String CLICK_ON_WIDGET_ACTION = "ClickOnWidget";
    public static final String UPDATE_ACTION = "UPDATE_ACTION";
    public UpdateWidgetService() {
        super("UpdateWidgetServiceThread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        Timber.v("Start intent service.");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                BalanceStatementWidget.class));

                for (int appWidgetId : appWidgetIds) {

                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.balance_statement_widget);
                    //use an ImageView which is supported in a widget and Create a Bitmap and draw with a Canvas on it
                    Bitmap bitmap = getBitmap(UpdateWidgetService.this);
                    //Assign this bitmap to ImageView in the widget
                    views.setImageViewBitmap(R.id.img_chart_widget, bitmap);


                    Intent intent_open_app = new Intent(this, MainActivity.class);
                    intent_open_app.setAction(CLICK_ON_WIDGET_ACTION);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent_open_app, 0);
                    views.setOnClickPendingIntent(R.id.lyt_root_widget,pendingIntent);

                    Intent intent_update = new Intent(this, BalanceStatementWidget.class);
                    intent_update.setAction(UPDATE_ACTION);
                    PendingIntent pendingIntent_update = PendingIntent.getBroadcast(this, 0, intent_update, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.txt_title_widget,pendingIntent_update);




                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }




    }





    private static Bitmap getBitmap(Context context) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(0);
        GeneralRepository repository = GeneralRepository.getInstance(context);

        float foodExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Food.name(), Constant.TIME_PERIOD.WEEKLY.ordinal());
        float entertainmentExp =repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), Constant.TIME_PERIOD.WEEKLY.ordinal());
        float transportExp =repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Transport.name(), Constant.TIME_PERIOD.WEEKLY.ordinal());
        float subExp =repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Subscription.name(), Constant.TIME_PERIOD.WEEKLY.ordinal());
        float othersExp =repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Others.name(), Constant.TIME_PERIOD.WEEKLY.ordinal());
        float remainderBudget = (float) repository.getBudget(Constant.TIME_PERIOD.WEEKLY.name()) - foodExp - transportExp - entertainmentExp - subExp - othersExp;

        PieChart pieChart = new PieChart(context);

        pieChart.measure(700,700);
        pieChart.layout(0,0,700,700);
        pieChart.setDrawingCacheEnabled(true);


        List<PieEntry> entries = new ArrayList<PieEntry>();
        if ( foodExp != 0f) entries.add(new PieEntry(foodExp, Constant.EXPENDITURE_CATEGORIES.Food.name(), ContextCompat.getDrawable(context, R.drawable.ic_food_black)));
        if ( transportExp != 0f) entries.add(new PieEntry(transportExp,Constant.EXPENDITURE_CATEGORIES.Transport.name(), ContextCompat.getDrawable(context,R.drawable.ic_transport_black)));
        if (entertainmentExp != 0f) entries.add(new PieEntry(entertainmentExp,Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), ContextCompat.getDrawable(context,R.drawable.ic_entertainment_black)));
        if (subExp!= 0f) entries.add(new PieEntry(subExp,Constant.EXPENDITURE_CATEGORIES.Subscription.name(), ContextCompat.getDrawable(context,R.drawable.ic_subscription_black)));
        if (othersExp!= 0f) entries.add(new PieEntry(othersExp,Constant.EXPENDITURE_CATEGORIES.Others.name(), ContextCompat.getDrawable(context,R.drawable.ic_other_black)));

        PieDataSet set = new PieDataSet(entries, "");
        set.setDrawIcons(true);
        set.setDrawValues(false);
        set.setColors(new int[] {  R.color.color_food, R.color.color_transport, R.color.color_entertainment, R.color.color_subscription, R.color.color_other, R.color.color6 }, context);
        PieData data = new PieData(set);

        float budget = repository.getBudget(Constant.TIME_PERIOD.WEEKLY.name());
        float maxAngle = ((float)budget - remainderBudget) / (float) budget * 360f;

        //checkBudget is exist
        if (remainderBudget>0) {
            pieChart.setMaxAngle(maxAngle);
            String balanceText = "BALANCE: " + formatter.format(remainderBudget);
            pieChart.setCenterText(balanceText);
        }else {
            pieChart.setMaxAngle(360f);
            pieChart.setCenterTextColor(Color.RED);
            pieChart.setCenterText(context.getString(R.string.budget_exceeded));
        }

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setCenterTextSize(20f);
        pieChart.setTransparentCircleColor(Color.LTGRAY);
        pieChart.setTransparentCircleAlpha(10);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);


        //use an ImageView which is supported in a widget and Create a Bitmap and draw with a Canvas on it
        return pieChart.getDrawingCache();
    }
}
