package com.imandroid.financemanagement.screen.reportList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;
import com.imandroid.financemanagement.screen.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ReportListFragment extends Fragment {

    private ReportListViewModel mViewModel;
    private List<ExpenditureEntity> expenditures;
    ReportListAdapter reportListAdapter;
    @BindView(R.id.recycler_report)
    RecyclerView recycler_report;
    @BindView(R.id.txt_today)
    TextView txt_today;
    @BindView(R.id.txt_week)
    TextView txt_week;
    @BindView(R.id.txt_month)
    TextView txt_month;

    String category="";


    public static ReportListFragment newInstance() {
        return new ReportListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_list_fragment, container, false);
        ButterKnife.bind(this,view);
        expenditures = new ArrayList<>();
        reportListAdapter = new ReportListAdapter(expenditures);
        recycler_report.setAdapter(reportListAdapter);
        recycler_report.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getArguments() != null) {
            category = ReportListFragmentArgs.fromBundle(getArguments()).getCategory();
            getActivity().setTitle("Expenses on: " + category);
        }
        if ((getActivity())!=null && ((MainActivity)getActivity()).getSupportActionBar()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this,new ReportListViewModelFactory(
                GeneralRepository.getInstance((AppCompatActivity) getActivity())))
                .get(ReportListViewModel.class);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String day_title =mViewModel.getDayReportTitle(category);
                String week_title =mViewModel.getWeekReportTitle(category);
                String month_title =mViewModel.getMonthReportTitle(category);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_today.setText(day_title);
                        txt_week.setText(week_title);
                        txt_month.setText(month_title);
                    }
                });

            }
        });
        thread.start();



        mViewModel.getExpendituresByCategory(category).observe(getViewLifecycleOwner(), new Observer<List<ExpenditureEntity>>() {
            @Override
            public void onChanged(List<ExpenditureEntity> expenditureEntities) {
                reportListAdapter.swapList(expenditureEntities);
            }
        });


    }


}
