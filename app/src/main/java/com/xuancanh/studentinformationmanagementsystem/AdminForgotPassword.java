package com.xuancanh.studentinformationmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminForgotPassword extends AppCompatActivity {
    private EditText edtAdminForgotPasswordEmail, edtAdminForgotPasswordId, edtAdminForgotPasswordNewPassword, edtAdminForgotPasswordRetypeNewPassword;
    private Button btnAdminForgotPasswordSave, btnAdminForgotPasswordExit;
    private String adminEmail, adminId, adminNewPassword, adminRetypeNewPassword;
    ImageView ivAdminForgotPasswordExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forgot_password);
        initUI();

        //Button Exit
        btnAdminForgotPasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminForgotPassword.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //ImageView Back
        ivAdminForgotPasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminForgotPassword.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Button Save
        btnAdminForgotPasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminEmail = edtAdminForgotPasswordEmail.getText().toString();
                adminId = edtAdminForgotPasswordId.getText().toString();
                adminNewPassword = edtAdminForgotPasswordNewPassword.getText().toString();
                adminRetypeNewPassword = edtAdminForgotPasswordRetypeNewPassword.getText().toString();
                if(adminId.length() > 0 && adminEmail.length() > 0 && adminNewPassword.length() > 0 && adminNewPassword.length() > 0 && adminRetypeNewPassword.length() > 0) {
                    if(!adminNewPassword.equals(adminRetypeNewPassword)) {
                        Toast.makeText(AdminForgotPassword.this, "New passwords and retype new passwords are not the same", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DataClient checkData = APIUtils.getData();
                        Call<String> callback = checkData.ForgotPasswordAdminData(adminId, adminEmail, adminNewPassword);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String res = response.body();
                                Log.d("Info Ok: ", res);
                                if(res.trim().equals("ADMIN_CHANGE_PASSWORD_SUCCESSFUL")){
                                    Toast.makeText(AdminForgotPassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminForgotPassword.this, AdminLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(res.trim().equals("ADMIN_CHANGE_PASSWORD_FAILED")){
                                    Toast.makeText(AdminForgotPassword.this, "Admin Email Or ID Is Incorrect", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(AdminForgotPassword.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Wrong:", t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private void initUI() {
        edtAdminForgotPasswordEmail = findViewById(R.id.edt_admin_forgot_password_email);
        edtAdminForgotPasswordId = findViewById(R.id.edt_admin_forgot_password_id);
        edtAdminForgotPasswordNewPassword = findViewById(R.id.edt_admin_forgot_password_new_password);
        edtAdminForgotPasswordRetypeNewPassword = findViewById(R.id.edt_admin_forgot_password_retype_new_password);
        btnAdminForgotPasswordSave = findViewById(R.id.btn_admin_forgot_password_save);
        btnAdminForgotPasswordExit = findViewById(R.id.btn_admin_forgot_password_exit);
        ivAdminForgotPasswordExit =findViewById(R.id.iv_admin_forgot_password_exit);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminForgotPassword.this, AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }
}