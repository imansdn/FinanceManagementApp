package com.imandroid.financemanagement.screen.reportList;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.imandroid.financemanagement.data.GeneralRepository;


public class ReportListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GeneralRepository repository;

    ReportListViewModelFactory(GeneralRepository repository) {
        super();
        this.repository = repository;
    }



    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReportListViewModel(repository);
    }
}
