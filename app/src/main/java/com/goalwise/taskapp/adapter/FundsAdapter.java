package com.goalwise.taskapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    public void onBindViewHolder(@NonNull final FundsViewHolder fundsViewHolder, int position) {
        fundsViewHolder.setIsRecyclable(false);
        final Fund fund = fundArrayList.get(position);
        if (fund != null) {
            fundsViewHolder.fund_name.setText(fund.getFundName());
            fundsViewHolder.min_sip_amount.setText("Min SIP Amount : ₹" + String.valueOf(fund.getMinSipAmount()));
            fundsViewHolder.min_sip_multiple.setText("Min SIP Multiple : ₹" + String.valueOf(fund.getMinSipMultiple()));
            fundsViewHolder.sip_dates.setText("Sip Dates : " + Arrays.toString(fund.getSipDates()).replaceAll("[\\[\\](){}]", "") + " of every month");

            fundsViewHolder.tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fundsViewHolder.tv_add.setVisibility(View.GONE);
                    fundsViewHolder.ll_enter_sip_amount.setVisibility(View.VISIBLE);
                }
            });

            fundsViewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fund != null) {
                        if (!TextUtils.isEmpty(fundsViewHolder.et_sip_amount.getText().toString().trim())) {
                            int amount = Integer.parseInt(fundsViewHolder.et_sip_amount.getText().toString().trim());
                            if (amount > fund.getMinSipAmount()) {
                                if (amount % fund.getMinSipMultiple() == 0) {
                                    final Dialog dialog = new Dialog(superContext);
                                    dialog.setContentView(R.layout.dialog_layout);

                                    //Initialize your widgets from layout here
                                    TextView tv_got_it = dialog.findViewById(R.id.tv_got_it);
                                    tv_got_it.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            fundsViewHolder.et_sip_amount.setError(null);
                                            fundsViewHolder.et_sip_amount.setText("");
                                            fundsViewHolder.tv_add.setVisibility(View.VISIBLE);
                                            fundsViewHolder.ll_enter_sip_amount.setVisibility(View.GONE);
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();

                                } else {
                                    fundsViewHolder.et_sip_amount.setError("Amount should be multiple of sip multiple");
                                }
                            } else {
                                fundsViewHolder.et_sip_amount.setError("Amount should be greater than sip amount");
                            }
                        } else {
                            fundsViewHolder.et_sip_amount.setError("Field should not be empty");
                        }
                    }

                }
            });
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
        private TextView tv_add;
        private EditText et_sip_amount;
        private View ll_enter_sip_amount;
        private Button btn_add;

        public FundsViewHolder(@NonNull View itemView) {
            super(itemView);
            fund_name = itemView.findViewById(R.id.fund_name);
            min_sip_amount = itemView.findViewById(R.id.min_sip_amount);
            min_sip_multiple = itemView.findViewById(R.id.min_sip_multiple);
            sip_dates = itemView.findViewById(R.id.sip_dates);
            tv_add = itemView.findViewById(R.id.tv_add);
            btn_add = itemView.findViewById(R.id.btn_add);
            et_sip_amount = itemView.findViewById(R.id.et_sip_amount);

            ll_enter_sip_amount = itemView.findViewById(R.id.ll_enter_sip_amount);
            ll_enter_sip_amount.setVisibility(View.GONE);
        }
    }
}
