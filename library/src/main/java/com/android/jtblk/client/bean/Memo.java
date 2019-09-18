package com.android.jtblk.client.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.jtblk.utils.Utils;

public class Memo implements Parcelable {
    private String memoData;
    private String memoType;

    public Memo() {
    }

    protected Memo(Parcel in) {
        memoData = in.readString();
        memoType = in.readString();
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel in) {
            return new Memo(in);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    public String getMemoData() {
        return memoData;
    }

    public void setMemoData(String memoData) {
        this.memoData = Utils.hexStrToStr(memoData);
        ;
    }

    public String getMemoType() {
        return memoType;
    }

    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memoData);
        dest.writeString(memoType);
    }
}
