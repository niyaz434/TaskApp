package com.goalwise.taskapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goalwise.taskapp.R;
import com.goalwise.taskapp.models.Fund;

import java.util.ArrayList;
import java.util.Arrays;

public class FundsAdapter extends RecyclerView.Adapter<FundsAdapter.FundsViewHolder> {
    private Context superContext = null;
    private ArrayList<Fund> fundArrayList;

    public FundsAdapter(Context superContext, ArrayList<Fund> fundArrayList) {
        this.superContext = superContext;
        if (this.fundArrayList == null) {
            this.fundArrayList = new ArrayList<>();
        }
        this.fundArrayList = fundArrayList;
    }

    @NonNull
    @Override
    public FundsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(superContext).inflate(R.layout.funds_layout, viewGroup, false);
        return new FundsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundsViewHolder fundsViewHolder, int position) {
        Fund fund = fundArrayList.get(position);
        if (fund != null) {
            fundsViewHolder.fund_name.setText(fund.getFundName());
            fundsViewHolder.min_sip_amount.setText("Min SIP Amount : ₹" + String.valueOf(fund.getMinSipAmount()));
            fundsViewHolder.min_sip_multiple.setText("Min SIP Multiple : ₹" + String.valueOf(fund.getMinSipMultiple()));
            fundsViewHolder.sip_dates.setText("Sip Dates : " + Arrays.toString(fund.getSipDates()).replaceAll("[\\[\\](){}]", "") + "of every month");
        }
    }

    @Override
    public int getItemCount() {
        return fundArrayList != null ? fundArrayList.size() : 0;
    }

    public void update(ArrayList<Fund> fundArrayList) {
        if (this.fundArrayList == null) {
            this.fundArrayList = new ArrayList<>();
        }
        this.fundArrayList = fundArrayList;
        notifyDataSetChanged();
    }

    public class FundsViewHolder extends RecyclerView.ViewHolder {
        private TextView fund_name;
        private TextView min_sip_amount;
        private TextView min_sip_multiple;
        private TextView sip_dates;

        public FundsViewHolder(@NonNull View itemView) {
            super(itemView);
            fund_name = itemView.findViewById(R.id.fund_name);
            min_sip_amount = itemView.findViewById(R.id.min_sip_amount);
            min_sip_multiple = itemView.findViewById(R.id.min_sip_multiple);
            sip_dates = itemView.findViewById(R.id.sip_dates);
        }
    }
}
