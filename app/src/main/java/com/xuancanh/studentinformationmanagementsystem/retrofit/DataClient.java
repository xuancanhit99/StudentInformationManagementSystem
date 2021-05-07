package com.xuancanh.studentinformationmanagementsystem.retrofit;

import com.xuancanh.studentinformationmanagementsystem.model.Admin;
import com.xuancanh.studentinformationmanagementsystem.model.Students;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


//Quan ly cac phuong thuc se gui len server
public interface DataClient {
    @Multipart
    @POST("uploadimage.php")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("insert.php")
    Call<String> InsertStudentData(@Field("StudentName") String StudentName,
                            @Field("StudentEmail") String StudentEmail,
                            @Field("StudentPassword") String StudentPassword,
                            @Field("StudentAvatar") String StudentAvatar);

    @FormUrlEncoded
    @POST("info.php")
    Call<List<Students>> LoginStudentData(@Field("StudentEmail") String StudentEmail,
                                   @Field("StudentPassword") String StudentPassword);

    @FormUrlEncoded
    @POST("admin/login.php")
    Call<List<Admin>> LoginAdminData(@Field("AdminEmail") String AdminEmail,
                                @Field("AdminPassword") String AdminPassword);

    @GET("admin/delete.php")
    Call<String> DeleteAdminData(@Query("AdminId") String AdminId, @Query("AdminAvatar") String AdminAvatar);
}
