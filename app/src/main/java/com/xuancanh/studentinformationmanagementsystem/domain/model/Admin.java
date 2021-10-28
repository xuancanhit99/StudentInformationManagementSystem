package com.xuancanh.studentinformationmanagementsystem.domain.model;


public class Admin {
    private String adId;
    private String adEmail;
    private String adPassword;
    private String adName;
    private String adAvatar;

    public Admin(String adId, String adEmail, String adPassword, String adName, String adAvatar) {
        this.adId = adId;
        this.adEmail = adEmail;
        this.adPassword = adPassword;
        this.adName = adName;
        this.adAvatar = adAvatar;
    }

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

}
