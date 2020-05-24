package com.imandroid.financemanagement.screen.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.screen.reportList.ReportListViewModel;


public class DashboardViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GeneralRepository repository;

    DashboardViewModelFactory(GeneralRepository repository) {
        super();
        this.repository = repository;
    }



    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardViewModel(repository);
    }
}
