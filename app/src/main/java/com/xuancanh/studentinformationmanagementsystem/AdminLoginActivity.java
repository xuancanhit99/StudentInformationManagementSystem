package com.xuancanh.studentinformationmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.model.Admin;
import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText edtAdminLoginEmail, edtAdminLoginPassword;
    private Button btnAdminLogin;
    private TextView tvAdminLoginForgotPassword, tvAdminLoginToLoginStudent;
    private ImageView ivAdminLoginClose;

    String AdminEmail, AdminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        //Anh xa
        initUI();

        // Close
        ivAdminLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To Login Student
        tvAdminLoginToLoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, StudentLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Button Login
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminEmail = edtAdminLoginEmail.getText().toString();
                AdminPassword = edtAdminLoginPassword.getText().toString();
                if(AdminEmail.length() > 0 && AdminPassword.length() > 0) {
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<List<Admin>> callback = dataClient.LoginAdminData(AdminEmail, AdminPassword);
                    callback.enqueue(new Callback<List<Admin>>() {
                        @Override
                        public void onResponse(Call<List<Admin>> call, Response<List<Admin>> response) {
                            ArrayList<Admin> arrAdmins = (ArrayList<Admin>)response.body();
                            if(arrAdmins.size() > 0) {
                                Intent intent = new Intent(AdminLoginActivity.this, HomeMenuActivity.class);
                                //Log.d("Admin", arrAdmins.get(0).getAdName());
                                intent.putExtra("ADMIN_DATA", arrAdmins);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Admin>> call, Throwable t) {
                            Toast.makeText(AdminLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                            //Log.d("Error", t.getMessage());
                        }
                    });
                }
            }
        });


    }

    private void initUI() {
        edtAdminLoginEmail = (EditText)findViewById(R.id.edt_admin_login_email);
        edtAdminLoginPassword = (EditText)findViewById(R.id.edt_admin_login_password);
        btnAdminLogin = (Button)findViewById(R.id.btn_admin_login);
        tvAdminLoginForgotPassword = (TextView)findViewById(R.id.tv_admin_login_forgot_password);
        tvAdminLoginToLoginStudent = (TextView)findViewById(R.id.tv_admin_login_to_login_student);
        ivAdminLoginClose = (ImageView)findViewById(R.id.iv_admin_login_close);
    }
}