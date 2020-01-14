package com.example.basictest;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private Request(Parcel in){
        in.readInt();
    }
    public static Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>(){

        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
