package com.imandroid.financemanagement.screen.addExpense;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.screen.dashboard.DashboardViewModel;


public class AddExpensesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GeneralRepository repository;

    AddExpensesViewModelFactory(GeneralRepository repository) {
        super();
        this.repository = repository;
    }



    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddExpensesViewModel(repository);
    }
}
