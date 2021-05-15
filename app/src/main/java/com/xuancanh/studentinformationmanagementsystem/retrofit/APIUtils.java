package com.xuancanh.studentinformationmanagementsystem.retrofit;

public class APIUtils {
    public static final String BASE_URL = "http://192.168.31.7/SIMS/";
//    public static final String BASE_URL = "http://sims.0hi.me/";
    //Get and sent data from server
    public static DataClient getData() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
