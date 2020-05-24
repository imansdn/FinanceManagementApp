package com.imandroid.financemanagement.data;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.imandroid.financemanagement.FinanceManagement;
import com.imandroid.financemanagement.data.sharedPref.SharedPrefHelper;
import com.imandroid.financemanagement.data.db.DatabaseGenerator;
import com.imandroid.financemanagement.data.db.ExpenditureDao;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;
import com.imandroid.financemanagement.util.Constant;

import java.util.List;

import timber.log.Timber;

public class GeneralRepository {

    private static final int DEFAULT_BUDGET = 25;

    private  static volatile GeneralRepository INSTANCE = null;
    private static final Object lock = new Object();
    private ExpenditureDao expenditureDao ;
    private SharedPrefHelper sharedPrefHelper;
    public static GeneralRepository getInstance(AppCompatActivity appCompatActivity) {
        GeneralRepository instance = INSTANCE;
        synchronized(lock) {
            if (INSTANCE == null){
                instance = new GeneralRepository(appCompatActivity);

                INSTANCE = instance;
            }
        }
        return instance;
    }

    private GeneralRepository(AppCompatActivity appCompatActivity) {
        this.expenditureDao = DatabaseGenerator.getInstance(appCompatActivity).expenditureDao();
        this.sharedPrefHelper = SharedPrefHelper.getInstance(appCompatActivity);
    }

    public LiveData<List<ExpenditureEntity>> getAllExpenditure(){
        return expenditureDao.getAllExp();
    }
    public LiveData<ExpenditureEntity> getExpenditureById(int id){
        return expenditureDao.getExpById(id);
    }

    public void addExpenditure(ExpenditureEntity expenditureEntity){
        expenditureDao.addExp(expenditureEntity);
    }

    public void updateExpenditure(ExpenditureEntity expenditureEntity){
        expenditureDao.updateExp(expenditureEntity);
    }

    public void deleteExpenditure(ExpenditureEntity expenditureEntity){
        expenditureDao.deleteExp(expenditureEntity);
    }
    public void deleteAllExpenditure(){
        expenditureDao.deleteAllExp();
    }

    public float getSumExpForDay(String category){
        return expenditureDao.getExpSumByCategoryForDay(category);
    }
    public float getSumExpForWeek(String category){
        return expenditureDao.getExpSumByCategoryForWeek(category);
    }
    public float getSumExpForMonth(String category){
        return expenditureDao.getExpSumByCategoryForMonth(category);
    }

    public float getTotalExpenses(String category,int timePeriod){

        if (timePeriod == Constant.TIME_PERIOD.DAILY.ordinal()){

            return getSumExpForDay(category);

        }else if (timePeriod == Constant.TIME_PERIOD.WEEKLY.ordinal()){
            return getSumExpForWeek(category);

        }else if (timePeriod == Constant.TIME_PERIOD.MONTHLY.ordinal()){
            return getSumExpForMonth(category);

        }else {
            return 0.0f;
        }


    }

    public int getBudget(String timePeriod_key) {
        return sharedPrefHelper.read(timePeriod_key, DEFAULT_BUDGET);

    }

    public void setBudget(String timePeriod_key,int value){

        sharedPrefHelper.write(timePeriod_key,value);
    }





}
