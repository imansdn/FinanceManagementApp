package com.imandroid.financemanagement.screen.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;
import com.imandroid.financemanagement.util.Constant;


public class BudgetDialog {
    private GeneralRepository repository;
    private Activity activity;

    public BudgetDialog(GeneralRepository repository, Activity activity) {
        this.repository = repository;
        this.activity = activity;
    }

    public void buildDialog() {
        final Activity thisActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity, R.style.AlertDialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = thisActivity.getLayoutInflater();

        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.budget_popup, null);
        builder.setView(layout);

        EditText daily = layout.findViewById(R.id.daily_budget);
        EditText weekly = layout.findViewById(R.id.weekly_budget);
        EditText monthly = layout.findViewById(R.id.monthly_budget);

        daily.setText(String.valueOf(repository.getBudget(Constant.TIME_PERIOD.DAILY.name())));
        weekly.setText(String.valueOf(repository.getBudget(Constant.TIME_PERIOD.WEEKLY.name())));
        monthly.setText(String.valueOf(repository.getBudget(Constant.TIME_PERIOD.MONTHLY.name())));

        daily.post(() -> daily.setSelection(daily.getText().length()));
        weekly.post(() -> weekly.setSelection(weekly.getText().length()));
        monthly.post(() -> monthly.setSelection(monthly.getText().length()));


        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                try {

                    EditText edited_daily = layout.findViewById(R.id.daily_budget);
                    EditText edited_weekly = layout.findViewById(R.id.weekly_budget);
                    EditText edited_monthly = layout.findViewById(R.id.monthly_budget);
                    repository.setBudget(Constant.TIME_PERIOD.DAILY.name(),Integer.parseInt(edited_daily.getText().toString()));
                    repository.setBudget(Constant.TIME_PERIOD.WEEKLY.name(),Integer.parseInt(edited_weekly.getText().toString()));
                    repository.setBudget(Constant.TIME_PERIOD.MONTHLY.name(),Integer.parseInt(edited_monthly.getText().toString()));
                    Toast.makeText(activity, R.string.budget_changes, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                } catch (NumberFormatException e) {
                    Toast.makeText(activity, R.string.the_budget_must_be_filled, Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
