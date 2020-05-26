package com.imandroid.financemanagement.screen.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureSummation;
import com.imandroid.financemanagement.data.network.CheckVersionService;
import com.imandroid.financemanagement.data.network.MyResultReceiver;
import com.imandroid.financemanagement.data.network.VersionDTO;
import com.imandroid.financemanagement.data.sharedPref.SharedPrefHelper;
import com.imandroid.financemanagement.screen.MainActivity;
import com.imandroid.financemanagement.util.AppDeviceInfoHelper;
import com.imandroid.financemanagement.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment implements MyResultReceiver.Receiver{

    private DashboardViewModel mViewModel;
    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subExp;
    private float othersExp;
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    AlertDialog alertDialog = null;
    @BindView(R.id.chart)
    PieChart pieChart;
    @BindView(R.id.btn_new_record)
    Button btn_new_record;
    @BindView(R.id.btn_daily)
    Button btn_daily;
    @BindView(R.id.btn_weekly)
    Button btn_weekly;
    @BindView(R.id.btn_monthly)
    Button btn_monthly;
    private MyResultReceiver mReceiver;
    private View view;
    private ShowcaseView showcase;
    Toolbar toolbar;
    private int timePeriod = Constant.TIME_PERIOD.WEEKLY.ordinal();
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        if ((getActivity())!=null && ((MainActivity)getActivity()).getSupportActionBar()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        setSelectedBackground(btn_weekly);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    updateChart(timePeriod);
            }
        };



        return view;
    }

    private void checkVersionRequest() {
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), CheckVersionService.class);
        intent.putExtra(Constant.URL_KEY, Constant.CHECK_VERSION_URL);
        intent.putExtra(Constant.RECEIVER_KEY, mReceiver);
        if (getActivity()!=null)
        getActivity().startService(intent);
    }

    private void updateChart(int timePeriod) {
//        mViewModel.updateValue(timePeriod);
        new UpdateDataTask(timePeriod, new OnTaskRoomCompleted() {
            @Override
            public void onTaskCompleted(ExpenditureSummation expenditureSummation) {
                foodExp=expenditureSummation.getFoodExp();
                entertainmentExp=expenditureSummation.getEntertainmentExp();
                transportExp=expenditureSummation.getTransportExp();
                subExp=expenditureSummation.getSubscriptionExp();
                othersExp=expenditureSummation.getOthersExp();

                PieDataSet set = new PieDataSet(getPieEntries(), "");
                set.setDrawIcons(true);
                set.setDrawValues(false);
                set.setColors(new int[] {  R.color.color_food, R.color.color_transport, R.color.color_entertainment, R.color.color_subscription, R.color.color_other, R.color.color6 }, getActivity());
                PieData data = new PieData(set);

                if (mViewModel.isExistBudget()) {
                    pieChart.setMaxAngle((mViewModel.getMaxAngle(timePeriod)));
                    pieChart.setCenterText(mViewModel.getBalanceText());
                }else {
                    pieChart.setMaxAngle(360f);
                    pieChart.setCenterTextColor(Color.RED);
                    pieChart.setCenterText(getString(R.string.budget_exceeded));
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
                pieChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        String label = ((PieEntry) e).getLabel();
                        NavDirections action = DashboardFragmentDirections.actionDashboardFragmentToReportListFragment(label);
                        Navigation.findNavController(view).navigate(action);

                    }

                    @Override
                    public void onNothingSelected() {


                    }
                });

            }
        }).execute();



    }

    @Override
    public void onResume() {
        super.onResume();
        updateChart(timePeriod);
        SharedPrefHelper.getInstance(getActivity()).setOnSharedPreferenceChangeListener(listener);

        getActivity().setTitle(R.string.app_name);
    }

    public List<PieEntry> getPieEntries() {
//        initialValues(timePeriod);
        List<PieEntry> entries = new ArrayList<PieEntry>();
        if (getActivity()!=null){
            if ( foodExp != 0f) entries.add(new PieEntry(foodExp, Constant.EXPENDITURE_CATEGORIES.Food.name(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_food_black)));
            if ( transportExp != 0f) entries.add(new PieEntry(transportExp,Constant.EXPENDITURE_CATEGORIES.Transport.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_transport_black)));
            if (entertainmentExp != 0f) entries.add(new PieEntry(entertainmentExp,Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_entertainment_black)));
            if (subExp!= 0f) entries.add(new PieEntry(subExp,Constant.EXPENDITURE_CATEGORIES.Subscription.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_subscription_black)));
            if (othersExp!= 0f) entries.add(new PieEntry(othersExp,Constant.EXPENDITURE_CATEGORIES.Others.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_other_black)));
        }

        return entries;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this
                ,new DashboardViewModelFactory(GeneralRepository.getInstance((AppCompatActivity) getActivity())))
                .get(DashboardViewModel.class);

        updateChart(timePeriod);



        if(mViewModel.isTheFirstRun()){
            showHelp();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkVersionRequest();

    }

    @OnClick(R.id.btn_daily)
    void setDailyData(){
        clearBackgrounds();
        setSelectedBackground(btn_daily);
        timePeriod= Constant.TIME_PERIOD.DAILY.ordinal();
        updateChart(timePeriod);

    }

    void clearBackgrounds(){
        btn_daily.setBackgroundColor(getActivity().getResources().getColor(android.R.color.transparent));
        btn_weekly.setBackgroundColor(getActivity().getResources().getColor(android.R.color.transparent));
        btn_monthly.setBackgroundColor(getActivity().getResources().getColor(android.R.color.transparent));
        btn_daily.setTextColor(getActivity().getResources().getColor(android.R.color.black));
        btn_weekly.setTextColor(getActivity().getResources().getColor(android.R.color.black));
        btn_monthly.setTextColor(getActivity().getResources().getColor(android.R.color.black));

    }
    void setSelectedBackground(Button view){
        view.setTextColor(getActivity().getResources().getColor(android.R.color.white));
        view.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
    }
    @OnClick(R.id.btn_weekly)
    void setWeeklyData(){
        clearBackgrounds();
        setSelectedBackground(btn_weekly);
        timePeriod= Constant.TIME_PERIOD.WEEKLY.ordinal();
        updateChart(timePeriod);

    }


    @OnClick(R.id.btn_monthly)
    void setMonthlyData(){
        clearBackgrounds();
        setSelectedBackground(btn_monthly);
        timePeriod= Constant.TIME_PERIOD.MONTHLY.ordinal();
        updateChart(timePeriod);

    }

    @OnClick(R.id.btn_new_record)
    void addNewRecord(){
        NavDirections action = DashboardFragmentDirections.actionDashboardFragmentToAddExpensesFragment();
        Navigation.findNavController(view).navigate(action);

    }


    public void createBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(GeneralRepository.getInstance((AppCompatActivity) getActivity()), getActivity());
        budgetDialog.buildDialog();
    }

    @OnClick(R.id.budget_button)
    void changeBudget(){
        createBudgetDialog();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String label = "";
        switch (id) {
            case R.id.menu_show_help:
                showHelp();
                break;

        case R.id.menu_check_update:
                SharedPrefHelper.getInstance(getActivity()).write(Constant.SHOW_UPDATE_KEY,true);
                checkVersionRequest();
                break;

        case R.id.menu_report_food:
                label = Constant.EXPENDITURE_CATEGORIES.Food.name();

                break;

            case R.id.menu_report_transport:
                label = Constant.EXPENDITURE_CATEGORIES.Transport.name();
                break;

            case R.id.menu_report_subs:
                label = Constant.EXPENDITURE_CATEGORIES.Subscription.name();
                break;

            case R.id.menu_report_entertain:
                label = Constant.EXPENDITURE_CATEGORIES.Entertainment.name();
                break;

            case R.id.menu_report_other:
                label = Constant.EXPENDITURE_CATEGORIES.Others.name();
                break;
        }
        if (!label.isEmpty()){
            NavDirections action = DashboardFragmentDirections.actionDashboardFragmentToReportListFragment(label);
            Navigation.findNavController(view).navigate(action);
        }

        return true;

    }

    public void onPause() {
        super.onPause();
        mReceiver.setReceiver(null); // clear receiver so no leaks.
    }

    //received from Intent service
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 0:
                //show progress
                break;
            case 1:
                VersionDTO versionDTO = resultData.getParcelable(Constant.RESULT_KEY);

                if (versionDTO != null) {
                    if (AppDeviceInfoHelper.versionNumber(getActivity())<Integer.parseInt(versionDTO.getLast_version_number())){
                        //show dialog for update
                        if (SharedPrefHelper.getInstance(getActivity()).read(Constant.SHOW_UPDATE_KEY,true)){
                            showUpdateDialog(versionDTO);
                        }

                    }
                }
                // do something interesting
                // hide progress
                break;

        }

    }

    private void showUpdateDialog(VersionDTO versionDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.update_dialog_title)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(getString(R.string.update_dialog_message, AppDeviceInfoHelper.versionName(getActivity()),versionDTO.getLast_version_name()))
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getActivity().getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setNegativeButton(R.string.skip_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.dont_show_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefHelper.getInstance(getActivity()).write(Constant.SHOW_UPDATE_KEY,false);
                        dialog.dismiss();

                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void showHelp() {
        showcase = new ShowcaseView.Builder(getActivity(), true)
                .setTarget(new ViewTarget(R.id.budget_button, getActivity()))
                .setContentTitle(getString(R.string.show_case_title))
                .setContentText(getString(R.string.show_case_content))
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .build();
        showcase.setButtonText(getString(R.string.next));

        showcase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showcase.setTarget(new ViewTarget(R.id.toggle_parent, getActivity()));
                        showcase.setContentTitle(getString(R.string.show_case_title1));
                        showcase.setContentText(getString(R.string.show_case_desc_1));
                        break;
                    case 2:
                        showcase.setTarget(new ViewTarget(R.id.btn_new_record, getActivity()));
                        showcase.setContentTitle(getString(R.string.show_case_title2));
                        showcase.setContentText(getString(R.string.show_case_desc_2));
                        break;
                    case 3:

                            showcase.setTarget(new ViewTarget(R.id.chart, getActivity()));
                            showcase.setContentTitle(getString(R.string.show_case_title3));
                            showcase.setContentText(getString(R.string.show_case_desc_3));
                            showcase.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                            showcase.setButtonText(getString(R.string.done));
                            break;

                    default:
                        showcase.hide();
                        showcase = null;
                }
            }
        });
    }


    public interface OnTaskRoomCompleted{
        void onTaskCompleted(ExpenditureSummation expenditureSummation);
    }


    public class UpdateDataTask extends AsyncTask<Void,Void,ExpenditureSummation> {

        private int timePeriod;
        OnTaskRoomCompleted onTaskCompleted;

        UpdateDataTask(int timePeriod,OnTaskRoomCompleted onTaskCompleted) {
            this.timePeriod = timePeriod;
            this.onTaskCompleted = onTaskCompleted;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected ExpenditureSummation doInBackground(Void... objs) {
            mViewModel.updateValue(timePeriod);

            return new ExpenditureSummation(mViewModel.getFoodExp(),mViewModel.getTransportExp()
                    ,mViewModel.getEntertainmentExp(),mViewModel.getSubExp(),mViewModel.getOthersExp(),mViewModel.getRemainderBudget());
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(ExpenditureSummation expenditureSummation) {
            onTaskCompleted.onTaskCompleted(expenditureSummation);
        }

    }

}
