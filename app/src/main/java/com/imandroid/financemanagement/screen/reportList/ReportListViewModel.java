package com.imandroid.financemanagement.screen.reportList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;
import com.imandroid.financemanagement.util.TextHelper;

import java.util.List;

public class ReportListViewModel extends ViewModel {
    private  GeneralRepository repository;

    public ReportListViewModel(GeneralRepository repository) {
        this.repository = repository;
    }

    LiveData<List<ExpenditureEntity>> getExpendituresByCategory(String category){
        return repository.getExpendituresByCategory(category);
    }

    public String getDayReportTitle(String category){
        return TextHelper.checkRecordsPlural(getExpCountForDay(category), (int) getSumExpForDay(category));
    }
    public String getWeekReportTitle(String category){
        return TextHelper.checkRecordsPlural(getExpCountForWeek(category), (int) getSumExpForWeek(category));
    }
    public String getMonthReportTitle(String category){
        return TextHelper.checkRecordsPlural(getExpCountForMonth(category), (int) getSumExpForMonth(category));
    }

    public float getSumExpForDay(String category){
        return repository.getSumExpForDay(category);
    }
    public float getSumExpForWeek(String category){
        return repository.getSumExpForWeek(category);
    }
    public float getSumExpForMonth(String category){
        return repository.getSumExpForMonth(category);
    }

    public int getExpCountForDay(String category){
        return repository.getExpCountForDay(category);
    }
    public int getExpCountForWeek(String category){
        return repository.getExpCountForWeek(category);
    }
    public int getExpCountForMonth(String category){
        return repository.getExpCountForMonth(category);
    }

}
