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
import android.view.View;
import android.view.ViewGroup;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListFragment extends Fragment {

    private ReportListViewModel mViewModel;
    private List<ExpenditureEntity> expenditures;
    ReportListAdapter reportListAdapter;
    @BindView(R.id.recycler_report)
    RecyclerView recycler_report;

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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this,new ReportListViewModelFactory(
                GeneralRepository.getInstance((AppCompatActivity) getActivity())))
                .get(ReportListViewModel.class);

        mViewModel.getAllExpenditures().observe(getViewLifecycleOwner(), new Observer<List<ExpenditureEntity>>() {
            @Override
            public void onChanged(List<ExpenditureEntity> expenditureEntities) {
                reportListAdapter.swapList(expenditureEntities);
            }
        });

    }

}
