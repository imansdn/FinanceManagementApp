package com.imandroid.financemanagement.screen.reportList;

import android.icu.util.LocaleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imandroid.financemanagement.R;
import com.imandroid.financemanagement.data.db.ExpenditureEntity;
import com.imandroid.financemanagement.util.TextHelper;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder> {

    private List<ExpenditureEntity> expenditures;
    private NumberFormat formatter;

    public ReportListAdapter(List<ExpenditureEntity> expenditures) {
        this.expenditures = expenditures;
        formatter = NumberFormat.getCurrencyInstance();
    }

    public void swapList(List<ExpenditureEntity> expenditureEntities) {
        this.expenditures=expenditureEntities;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_item_desc)
        TextView txt_item_desc;
        @BindView(R.id.txt_item_time)
        TextView txt_item_time;
        @BindView(R.id.txt_item_cost)
        TextView txt_item_cost;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bind(){
            ExpenditureEntity expenditure = expenditures.get(getAdapterPosition());
            txt_item_desc.setText(expenditure.getDescription());
            txt_item_time.setText(TextHelper.getDateReadable(Calendar.getInstance().getTime(),expenditure.getDate()));
            txt_item_cost.setText(formatter.format(expenditure.getCost()));
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_fragment,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.bind();
    }

    @Override
    public int getItemCount() {
        return expenditures.size();
    }



}
