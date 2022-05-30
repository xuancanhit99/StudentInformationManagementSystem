package com.xuancanh.studentinformationmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student implements Parcelable {

    @SerializedName("StuId")
    @Expose
    private String stuId;
    @SerializedName("StuEmail")
    @Expose
    private String stuEmail;
    @SerializedName("StuPassword")
    @Expose
    private String stuPassword;
    @SerializedName("StuName")
    @Expose
    private String stuName;
    @SerializedName("StuNo")
    @Expose
    private String stuNo;
    @SerializedName("StuAvatar")
    @Expose
    private String stuAvatar;
    @SerializedName("StuDOB")
    @Expose
    private String stuDOB;
    @SerializedName("StuClass")
    @Expose
    private String stuClass;
    @SerializedName("StuPhone")
    @Expose
    private String stuPhone;
    @SerializedName("StuGender")
    @Expose
    private String stuGender;
    @SerializedName("StuActive")
    @Expose
    private String stuActive;
    @SerializedName("StuNotice")
    @Expose
    private String stuNotice;
    @SerializedName("StuReport")
    @Expose
    private String stuReport;
    @SerializedName("StuReply")
    @Expose
    private String stuReply;

    @SerializedName("StuMath")
    @Expose
    private String stuMath;

    @SerializedName("StuEng")
    @Expose
    private String stuEng;

    @SerializedName("StuPhy")
    @Expose
    private String stuPhy;

    @SerializedName("StuInfo")
    @Expose
    private String stuInfo;

    @SerializedName("StuPro")
    @Expose
    private String stuPro;

    @SerializedName("StuEco")
    @Expose
    private String stuEco;

    @SerializedName("StuPhi")
    @Expose
    private String stuPhi;

    public Student(Parcel in) {
        stuId = in.readString();
        stuEmail = in.readString();
        stuPassword = in.readString();
        stuName = in.readString();
        stuNo = in.readString();
        stuAvatar = in.readString();
        stuDOB = in.readString();
        stuClass = in.readString();
        stuPhone = in.readString();
        stuGender = in.readString();
        stuActive = in.readString();
        stuNotice = in.readString();
        stuReport = in.readString();
        stuReply = in.readString();

        stuMath = in.readString();
        stuEng = in.readString();
        stuPhy = in.readString();
        stuInfo = in.readString();
        stuPro = in.readString();
        stuEco = in.readString();
        stuPhi = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student() {

    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuEmail() {
        return stuEmail;
    }

    public void setStuEmail(String stuEmail) {
        this.stuEmail = stuEmail;
    }

    public String getStuPassword() {
        return stuPassword;
    }

    public void setStuPassword(String stuPassword) {
        this.stuPassword = stuPassword;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuAvatar() {
        return stuAvatar;
    }

    public void setStuAvatar(String stuAvatar) {
        this.stuAvatar = stuAvatar;
    }

    public String getStuDOB() {
        return stuDOB;
    }

    public void setStuDOB(String stuDOB) {
        this.stuDOB = stuDOB;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuPhone() {
        return stuPhone;
    }

    public void setStuPhone(String stuPhone) {
        this.stuPhone = stuPhone;
    }

    public String getStuGender() {
        return stuGender;
    }

    public void setStuGender(String stuGender) {
        this.stuGender = stuGender;
    }

    public String getStuActive() {
        return stuActive;
    }

    public void setStuActive(String stuActive) {
        this.stuActive = stuActive;
    }

    public String getStuNotice() {
        return stuNotice;
    }

    public void setStuNotice(String stuNotice) {
        this.stuNotice = stuNotice;
    }

    public String getStuReport() {
        return stuReport;
    }

    public void setStuReport(String stuReport) {
        this.stuReport = stuReport;
    }

    public String getStuReply() {
        return stuReply;
    }

    public void setStuReply(String stuReply) {
        this.stuReply = stuReply;
    }

    public String getStuMath() {
        return stuMath;
    }

    public void setStuMath(String stuMath) {
        this.stuMath = stuMath;
    }

    public String getStuEng() {
        return stuEng;
    }

    public void setStuEng(String stuEng) {
        this.stuEng = stuEng;
    }

    public String getStuPhy() {
        return stuPhy;
    }

    public void setStuPhy(String stuPhy) {
        this.stuPhy = stuPhy;
    }

    public String getStuInfo() {
        return stuInfo;
    }

    public void setStuInfo(String stuInfo) {
        this.stuInfo = stuInfo;
    }

    public String getStuPro() {
        return stuPro;
    }

    public void setStuPro(String stuPro) {
        this.stuPro = stuPro;
    }

    public String getStuEco() {
        return stuEco;
    }

    public void setStuEco(String stuEco) {
        this.stuEco = stuEco;
    }

    public String getStuPhi() {
        return stuPhi;
    }

    public void setStuPhi(String stuPhi) {
        this.stuPhi = stuPhi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stuId);
        dest.writeString(stuEmail);
        dest.writeString(stuPassword);
        dest.writeString(stuName);
        dest.writeString(stuNo);
        dest.writeString(stuAvatar);
        dest.writeString(stuDOB);
        dest.writeString(stuClass);
        dest.writeString(stuPhone);
        dest.writeString(stuGender);
        dest.writeString(stuActive);
        dest.writeString(stuNotice);
        dest.writeString(stuReport);
        dest.writeString(stuReply);

        dest.writeString(stuMath);
        dest.writeString(stuEng);
        dest.writeString(stuPhy);
        dest.writeString(stuInfo);
        dest.writeString(stuPro);
        dest.writeString(stuEco);
        dest.writeString(stuPhi);
    }
}