package com.goalwise.taskapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fund implements Parcelable {
    public static final Creator<Fund> CREATOR = new Creator<Fund>() {
        @Override
        public Fund createFromParcel(Parcel in) {
            return new Fund(in);
        }

        @Override
        public Fund[] newArray(int size) {
            return new Fund[size];
        }
    };

    private String fundName;
    private int minSipAmount;
    private int minSipMultiple;
    private int[] sipDates;

    public Fund() {
        fundName = "";
        minSipAmount = 0;
        minSipMultiple = 0;
        sipDates = new int[0];
    }

    protected Fund(Parcel in) {
        fundName = in.readString();
        minSipAmount = in.readInt();
        minSipMultiple = in.readInt();
        sipDates = in.createIntArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fundName);
        dest.writeInt(minSipAmount);
        dest.writeInt(minSipMultiple);
        dest.writeIntArray(sipDates);
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public int getMinSipAmount() {
        return minSipAmount;
    }

    public void setMinSipAmount(int minSipAmount) {
        this.minSipAmount = minSipAmount;
    }

    public int getMinSipMultiple() {
        return minSipMultiple;
    }

    public void setMinSipMultiple(int minSipMultiple) {
        this.minSipMultiple = minSipMultiple;
    }

    public int[] getSipDates() {
        return sipDates;
    }

    public void setSipDates(int[] sipDates) {
        this.sipDates = sipDates;
    }
}
