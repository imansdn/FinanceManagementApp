package com.imandroid.financemanagement.screen.dashboard;

import androidx.lifecycle.ViewModel;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.util.Constant;

import java.text.NumberFormat;

import timber.log.Timber;

public class DashboardViewModel extends ViewModel {
    private GeneralRepository repository;
    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subExp;
    private float othersExp;
    private float remainderBudget;
    int DEFAULT_TIME_PERIOD = 0;
    private NumberFormat formatter;

    public DashboardViewModel(GeneralRepository repository) {
        this.repository = repository;
        formatter = NumberFormat.getCurrencyInstance();
        updateValues(DEFAULT_TIME_PERIOD);

    }

    public float getFoodExp() {
        return foodExp;
    }

    public float getTransportExp() {
        return transportExp;
    }

    public float getEntertainmentExp() {
        return entertainmentExp;
    }

    public float getSubExp() {
        return subExp;
    }

    public float getOthersExp() {
        return othersExp;
    }

    float getMaxAngle(int timePeriod){
        return ((float)getBudget(timePeriod) - remainderBudget) / (float) getBudget(timePeriod) * 360f;
    }

    public void updateValues(int timePeriod) {
        foodExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Food.name(), timePeriod);

        transportExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Transport.name(), timePeriod);
        entertainmentExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), timePeriod);
        subExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Subscription.name(), timePeriod);
        othersExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Others.name(), timePeriod);
        remainderBudget = (float) repository.getBudget(Constant.TIME_PERIOD.values()[timePeriod].name()) - foodExp - transportExp - entertainmentExp - subExp - othersExp;
        Timber.i("remainderBudget -->"+remainderBudget);

//        remainderBudget = (MutableLiveData<Float>) Transformations.map(foodExp, foodExp -> {
//                final float[] remind = {0f};
//
//                if (foodExp!=null){
//                    remind[0] = repository.getBudget(timePeriod)-foodExp;
//                    Transformations.map(transportExp, transportExp -> {
//                        remind[0] = remind[0] -transportExp;
//                        Transformations.map(subExp, subExp -> {
//                            remind[0] = remind[0] -subExp;
//                            Transformations.map(othersExp, othersExp -> {
//                                remind[0] = remind[0] -othersExp;
//
//                                return remind[0];
//                            });
//                            return remind[0];
//                        });
//                        return remind[0];
//                    });
//                    return remind[0];
//                }
//                return repository.getBudget(timePeriod);
//
//            });


//        budget = repository.getBudgetGoal(timePeriod);
    }

    public float getRemainderBudget() {
        return remainderBudget;
    }

    public float getBudget(int timePeriod) {
        return repository.getBudget(Constant.TIME_PERIOD.values()[timePeriod].name());
    }


    public boolean isExistBudget() {
        return remainderBudget > 0f;
    }


    public CharSequence getBalanceText() {
        return "BALANCE: " + formatter.format(remainderBudget);
    }
}
