package com.goalwise.taskapp.utils;

import android.content.Context;
import android.text.TextUtils;

import com.goalwise.taskapp.models.Fund;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private final String TAG_STATUS = "status";
    private final String TAG_DATA = "data";
    private final String TAG_FUND_NAME = "fundname";
    private final String TAG_MIN_SIP_AMOUNT = "minsipamount";
    private final String TAG_MIN_SIP_MULTIPLE = "minsipmultiple";
    private final String TAG_SIP_DATES = "sipdates";
    private Context superContext = null;

    public JsonUtils(Context context) {
        this.superContext = context;
    }

    public Object[] parseRequestResponse(JSONObject jsonObject) {
        String status = "";
        ArrayList<Fund> fundArrayList = new ArrayList<>();
        try {
            if (jsonObject.has(TAG_STATUS)) {
                String temp = jsonObject.getString(TAG_STATUS);
                if (!TextUtils.isEmpty(temp))
                    status = temp;
            }
        } catch (Exception e) {
            status = "";
        }

        if (jsonObject.has(TAG_DATA)) {
            try {
                JSONArray fundModelArray = jsonObject.getJSONArray(TAG_DATA);
                if (fundModelArray.length() != 0) {
                    for (int fundModelIndex = 0; fundModelIndex < fundModelArray.length(); fundModelIndex++) {
                        JSONObject fundModelJsonObject = fundModelArray.getJSONObject(fundModelIndex);
                        Fund fund = parseFundModel(fundModelJsonObject);
                        fundArrayList.add(fund);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Object[] returnObject = new Object[2];
        returnObject[0] = status;
        returnObject[1] = fundArrayList;
        return returnObject;
    }

    private Fund parseFundModel(JSONObject fundModelJsonObject) {
        String fundName = "";
        int minSipAmount = 0;
        int minSipMultiple = 0;
        int[] sipDates = new int[0];

        try {
            if (fundModelJsonObject.has(TAG_FUND_NAME)) {
                String temp = fundModelJsonObject.getString(TAG_FUND_NAME);
                if (!TextUtils.isEmpty(temp))
                    fundName = temp;
            }
        } catch (Exception e) {
            fundName = "";
        }

        try {
            if (fundModelJsonObject.has(TAG_MIN_SIP_AMOUNT)) {
                minSipAmount = fundModelJsonObject.getInt(TAG_MIN_SIP_AMOUNT);
            }
        } catch (Exception e) {
            minSipAmount = 0;
        }

        try {
            if (fundModelJsonObject.has(TAG_MIN_SIP_MULTIPLE)) {
                minSipMultiple = fundModelJsonObject.getInt(TAG_MIN_SIP_MULTIPLE);
            }
        } catch (Exception e) {
            minSipMultiple = 0;
        }

        if (fundModelJsonObject.has(TAG_SIP_DATES)) {
            try {
                JSONArray fundModelArray = fundModelJsonObject.getJSONArray(TAG_SIP_DATES);
                if (fundModelArray.length() != 0) {
                    sipDates = new int[fundModelArray.length()];
                    for (int fundModelIndex = 0; fundModelIndex < fundModelArray.length(); fundModelIndex++) {
                        int data = fundModelArray.getInt(fundModelIndex);
                        sipDates[fundModelIndex] = data;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Fund fund = new Fund();
        fund.setFundName(fundName);
        fund.setMinSipAmount(minSipAmount);
        fund.setMinSipMultiple(minSipMultiple);
        fund.setSipDates(sipDates);

        return fund;
    }
}
