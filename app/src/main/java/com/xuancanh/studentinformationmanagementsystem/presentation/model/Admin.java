package com.xuancanh.studentinformationmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Admin implements Parcelable {

    @SerializedName("AdId")
    @Expose
    private String adId;
    @SerializedName("AdEmail")
    @Expose
    private String adEmail;
    @SerializedName("AdPassword")
    @Expose
    private String adPassword;
    @SerializedName("AdName")
    @Expose
    private String adName;
    @SerializedName("AdAvatar")
    @Expose
    private String adAvatar;

    protected Admin(Parcel in) {
        adId = in.readString();
        adEmail = in.readString();
        adPassword = in.readString();
        adName = in.readString();
        adAvatar = in.readString();
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdEmail() {
        return adEmail;
    }

    public void setAdEmail(String adEmail) {
        this.adEmail = adEmail;
    }

    public String getAdPassword() {
        return adPassword;
    }

    public void setAdPassword(String adPassword) {
        this.adPassword = adPassword;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdAvatar() {
        return adAvatar;
    }

    public void setAdAvatar(String adAvatar) {
        this.adAvatar = adAvatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adId);
        dest.writeString(adEmail);
        dest.writeString(adPassword);
        dest.writeString(adName);
        dest.writeString(adAvatar);
    }
}
