package com.imandroid.financemanagement.screen.addExpense;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class AddExpenseFormState {
    @Nullable
    private Integer costError;
    @Nullable
    private Integer descriptionError;
    private boolean isDataValid;

    AddExpenseFormState(@Nullable Integer costError, @Nullable Integer descriptionError) {
        this.costError = costError;
        this.descriptionError = descriptionError;
        this.isDataValid = false;
    }

    AddExpenseFormState(boolean isDataValid) {
        this.costError = null;
        this.descriptionError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getCostError() {
        return costError;
    }

    @Nullable
    Integer getDescriptionError() {
        return descriptionError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
