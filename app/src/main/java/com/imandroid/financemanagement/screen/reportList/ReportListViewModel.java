package com.imandroid.financemanagement.screen.reportList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;

import java.util.List;

public class ReportListViewModel extends ViewModel {
    private  GeneralRepository repository;

    public ReportListViewModel(GeneralRepository repository) {
        this.repository = repository;
    }

    LiveData<List<ExpenditureEntity>> getAllExpenditures(){
        return repository.getAllExpenditure();
    }
}
