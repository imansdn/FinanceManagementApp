package com.imandroid.financemanagement.screen.addExpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.GeneralRepository;

import com.imandroid.financemanagement.screen.MainActivity;
import com.imandroid.financemanagement.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExpensesFragment extends Fragment {

    private AddExpensesViewModel mViewModel;
    @BindView(R.id.radio_group_cat)
    RadioGroup radio_group_cat;
    @BindView(R.id.edt_desc)
    EditText edt_desc;
    @BindView(R.id.edt_cost)
    EditText edt_cost;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    private String category= Constant.EXPENDITURE_CATEGORIES.Food.name();
    private View view;
    public static AddExpensesFragment newInstance() {
        return new AddExpensesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_expenses_fragment, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        btn_confirm.setEnabled(false);

        if ((getActivity())!=null && ((MainActivity)getActivity()).getSupportActionBar()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity()!=null)
        getActivity().setTitle(getString(R.string.new_expense));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this,new AddExpensesViewModelFactory(GeneralRepository.getInstance((AppCompatActivity) getActivity()))).get(AddExpensesViewModel.class);


        mViewModel.getFormState().observe(getViewLifecycleOwner(), new Observer<AddExpenseFormState>() {
            @Override
            public void onChanged(@Nullable AddExpenseFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                btn_confirm.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getCostError() != null) {
                    edt_cost.setError(getString(loginFormState.getCostError()));
                }
                if (loginFormState.getDescriptionError() != null) {
                    edt_desc.setError(getString(loginFormState.getDescriptionError()));
                }
            }
        });


        radio_group_cat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radio_food:
                        category= Constant.EXPENDITURE_CATEGORIES.Food.name();
                        break;
                    case R.id.radio_transport:
                        category= Constant.EXPENDITURE_CATEGORIES.Transport.name();
                        break;

                    case R.id.radio_entertian:
                        category= Constant.EXPENDITURE_CATEGORIES.Entertainment.name();
                        break;
                    case R.id.radio_subscript:
                        category= Constant.EXPENDITURE_CATEGORIES.Subscription.name();
                        break;
                    case R.id.radio_other:
                        category= Constant.EXPENDITURE_CATEGORIES.Others.name();
                        break;
                }


            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.addExpenseDataChanged(edt_cost.getText().toString(),
                        edt_desc.getText().toString());
            }
        };



        edt_cost.addTextChangedListener(afterTextChangedListener);
        edt_desc.addTextChangedListener(afterTextChangedListener);


    }


    @OnClick(R.id.btn_confirm)
    void addExpense(){

        mViewModel.addExpense(edt_cost.getText().toString(),
                edt_desc.getText().toString(),category);
       NavDirections action = AddExpensesFragmentDirections.actionAddExpensesFragmentToDashboardFragment();
        Navigation.findNavController(view).navigate(action);
    }

}
