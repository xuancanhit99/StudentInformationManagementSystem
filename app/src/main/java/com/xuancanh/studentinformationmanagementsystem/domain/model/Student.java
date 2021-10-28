package com.xuancanh.studentinformationmanagementsystem.domain.model;


public class Student {

    private String stuId;
    private String stuEmail;
    private String stuPassword;
    private String stuName;
    private String stuNo;
    private String stuAvatar;
    private String stuDOB;
    private String stuClass;
    private String stuPhone;
    private String stuGender;
    private String stuActive;
    private String stuNotice;
    private String stuReport;
    private String stuReply;

    public Student(String stuId, String stuEmail, String stuPassword, String stuName, String stuNo, String stuAvatar, String stuDOB, String stuClass, String stuPhone, String stuGender, String stuActive, String stuNotice, String stuReport, String stuReply) {
        this.stuId = stuId;
        this.stuEmail = stuEmail;
        this.stuPassword = stuPassword;
        this.stuName = stuName;
        this.stuNo = stuNo;
        this.stuAvatar = stuAvatar;
        this.stuDOB = stuDOB;
        this.stuClass = stuClass;
        this.stuPhone = stuPhone;
        this.stuGender = stuGender;
        this.stuActive = stuActive;
        this.stuNotice = stuNotice;
        this.stuReport = stuReport;
        this.stuReply = stuReply;
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

}