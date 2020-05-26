package com.imandroid.financemanagement.data.network;

import android.os.Parcel;
import android.os.Parcelable;

public class VersionDTO implements Parcelable {
    private String last_version_number;
    private String last_version_name;

    public VersionDTO(String last_version_number, String last_version_name) {
        this.last_version_number = last_version_number;
        this.last_version_name = last_version_name;
    }

    public String getLast_version_number() {
        return last_version_number;
    }

    public void setLast_version_number(String last_version_number) {
        this.last_version_number = last_version_number;
    }

    public String getLast_version_name() {
        return last_version_name;
    }

    public void setLast_version_name(String last_version_name) {
        this.last_version_name = last_version_name;
    }

    public VersionDTO(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.last_version_name = data[0];
        this.last_version_number = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.last_version_name,
                this.last_version_number});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public VersionDTO createFromParcel(Parcel in) {
            return new VersionDTO(in);
        }

        public VersionDTO[] newArray(int size) {
            return new VersionDTO[size];
        }
    };
}
