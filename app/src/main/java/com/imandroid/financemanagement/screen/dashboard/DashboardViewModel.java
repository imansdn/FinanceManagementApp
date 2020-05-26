package com.imandroid.financemanagement.screen.dashboard;


import androidx.lifecycle.ViewModel;

import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.util.Constant;
import java.math.RoundingMode;
import java.text.NumberFormat;


public class DashboardViewModel extends ViewModel {
    private GeneralRepository repository;
    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subExp;
    private float othersExp;
    private float remainderBudget;
    private NumberFormat formatter;
    public DashboardViewModel(GeneralRepository repository) {
        this.repository = repository;
        formatter = NumberFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setRoundingMode(RoundingMode.DOWN);
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

    public void updateValue(int timePeriod) {

        foodExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Food.name(), timePeriod);
        transportExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Transport.name(), timePeriod);
        entertainmentExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Entertainment.name(), timePeriod);
        subExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Subscription.name(), timePeriod);
        othersExp = repository.getTotalExpenses(Constant.EXPENDITURE_CATEGORIES.Others.name(), timePeriod);
        remainderBudget = (float) repository.getBudget(Constant.TIME_PERIOD.values()[timePeriod].name()) - foodExp - transportExp - entertainmentExp - subExp - othersExp;


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
        return "BALANCE:\n" + formatter.format(remainderBudget);
    }

    public String formatFloatToString(final float f)
    {
        final int i=(int)f;
        if(f==i)
            return Integer.toString(i);
        return Float.toString(f);
    }

    public boolean isTheFirstRun() {

        return repository.isTheFirstRun();
    }
}
