package com.xuancanh.studentinformationmanagementsystem.presentation.retrofit;

public class APIUtils {
//    public static final String BASE_URL = "http://192.168.31.14/SIMS/";
    public static final String BASE_URL = "https://xuancanhit.000webhostapp.com/SIMS/";
//    public static final String BASE_URL = "https://xuancanhit.000webhostapp.com/HMS/";
    //Get and sent data from server
    public static DataClient getData() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
