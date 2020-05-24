package com.imandroid.financemanagement.screen.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.imandroid.financemanagement.FinanceManagement;
import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private float remainderBudget;
    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subExp;
    private float othersExp;
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @BindView(R.id.chart)
    PieChart pieChart;
    @BindView(R.id.btn_new_record)
    Button btn_new_record;
    View view;
    private int timePeriod = Constant.TIME_PERIOD.WEEKLY.ordinal();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this,view);


        return view;
    }

    private void updateChart(int timePeriod) {
        mViewModel.updateValues(timePeriod);
        PieDataSet set = new PieDataSet(getPieEntries(), "");
        set.setDrawIcons(true);
        set.setDrawValues(false);
        set.setColors(new int[] {  R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5, R.color.color6 }, getActivity());
        PieData data = new PieData(set);

        if (mViewModel.isExistBudget()) {
            pieChart.setMaxAngle(mViewModel.getMaxAngle(timePeriod));
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
//                Intent intent = new Intent(context, ListExpActivity.class);
//                intent.putExtra("EXP_TYPE", label);
//                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        updateChart(timePeriod);
    }

    public List<PieEntry> getPieEntries() {
//        initialValues(timePeriod);
        List<PieEntry> entries = new ArrayList<PieEntry>();
        if ( foodExp != 0f) entries.add(new PieEntry(foodExp, Constant.EXPENDITURE_CATEGORIES.Food.name(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_access_time_black_24dp)));
        if ( transportExp != 0f) entries.add(new PieEntry(transportExp,Constant.EXPENDITURE_CATEGORIES.Transport.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_access_time_black_24dp)));
        if (entertainmentExp != 0f) entries.add(new PieEntry(entertainmentExp,Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_access_time_black_24dp)));
        if (subExp!= 0f) entries.add(new PieEntry(subExp,Constant.EXPENDITURE_CATEGORIES.Subscription.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_access_time_black_24dp)));
        if (othersExp!= 0f) entries.add(new PieEntry(othersExp,Constant.EXPENDITURE_CATEGORIES.Others.name(), ContextCompat.getDrawable(getActivity(),R.drawable.ic_access_time_black_24dp)));
        return entries;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this
                ,new DashboardViewModelFactory(GeneralRepository.getInstance((AppCompatActivity) getActivity())))
                .get(DashboardViewModel.class);
        mViewModel.updateValues(timePeriod);

        remainderBudget = mViewModel.getRemainderBudget();
        foodExp=mViewModel.getFoodExp();
        entertainmentExp=mViewModel.getEntertainmentExp();
        transportExp=mViewModel.getTransportExp();
        subExp=mViewModel.getSubExp();
        othersExp=mViewModel.getOthersExp();




    }

    @OnClick(R.id.btn_daily)
    void setDailyData(){
        timePeriod = Constant.TIME_PERIOD.DAILY.ordinal();
        updateChart(timePeriod);
    }

    @OnClick(R.id.btn_weekly)
    void setWeeklyData(){
        timePeriod = Constant.TIME_PERIOD.WEEKLY.ordinal();
        updateChart(timePeriod);
    }


    @OnClick(R.id.btn_monthly)
    void setMonthlyData(){
        timePeriod = Constant.TIME_PERIOD.MONTHLY.ordinal();
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

}
