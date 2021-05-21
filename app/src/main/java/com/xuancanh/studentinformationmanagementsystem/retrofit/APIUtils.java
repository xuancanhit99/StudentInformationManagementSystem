package com.xuancanh.studentinformationmanagementsystem.retrofit;

public class APIUtils {
    public static final String BASE_URL = "http://192.168.31.7/SIMS/";
//    public static final String BASE_URL = "https://xuancanh.000webhostapp.com/";
    //Get and sent data from server
    public static DataClient getData() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
