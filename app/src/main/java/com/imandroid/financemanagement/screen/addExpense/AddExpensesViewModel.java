package com.imandroid.financemanagement.screen.addExpense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;

import java.sql.Timestamp;

public class AddExpensesViewModel extends ViewModel {
    private GeneralRepository repository;
    private MutableLiveData<AddExpenseFormState> formState = new MutableLiveData<>();

    public AddExpensesViewModel(GeneralRepository repository) {
        this.repository=repository;
    }

    public LiveData<AddExpenseFormState> getFormState() {
        return formState;
    }

    public void addExpense(String cost, String desc,String category){
        String date = new Timestamp(System.currentTimeMillis()).toString();

        ExpenditureEntity expenditure = new ExpenditureEntity(null,Float.parseFloat(cost),category,desc,date);

        repository.addExpenditure(expenditure);
    }


    public void addExpenseDataChanged(String cost, String desc) {
        if (!isCostValid(cost)) {
            formState.setValue(new AddExpenseFormState(R.string.invalid_cost, null));
        } else if (!isDescriptionValid(desc)) {
            formState.setValue(new AddExpenseFormState(null, R.string.invalid_desc));
        } else {
            formState.setValue(new AddExpenseFormState(true));
        }
    }


    private boolean isDescriptionValid(String desc) {
        return desc != null && !desc.isEmpty();
    }

    private boolean isCostValid(String cost) {
        try {
            return Float.parseFloat(cost) != 0f;
        }catch (Exception e){
            return false;
        }

    }
}
