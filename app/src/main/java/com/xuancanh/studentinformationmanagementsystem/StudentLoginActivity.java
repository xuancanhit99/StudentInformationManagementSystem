package com.xuancanh.studentinformationmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.model.Students;
import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText edtStuLoginEmail, edtStuLoginPassword;
    private Button btnStuLogin;
    private TextView tvStuLoginForgotPassword, tvStuLoginToLoginAdmin, tvStuLoginToRegister;
    private ImageView ivStuLoginClose;

    String StudentEmail, StudentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //Anh xa
        initUI();

        // Close
        ivStuLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To Login Admin
        tvStuLoginToLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //To Register
        tvStuLoginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Button Login
        btnStuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentEmail = edtStuLoginEmail.getText().toString();
                StudentPassword = edtStuLoginPassword.getText().toString();
                if(StudentEmail.length() > 0 && StudentPassword.length() > 0) {
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<List<Students>> callback = dataClient.LoginStudentData(StudentEmail, StudentPassword);
                    callback.enqueue(new Callback<List<Students>>() {
                        @Override
                        public void onResponse(Call<List<Students>> call, Response<List<Students>> response) {
                            ArrayList<Students> arrStudent = (ArrayList<Students>)response.body();
                            if(arrStudent.size() > 0) {
                                Log.d("Login", arrStudent.get(0).getStuEmail());
                                Log.d("Login", arrStudent.get(0).getStuPassword());
                                Log.d("Login", arrStudent.get(0).getStuAvatar());
                                Log.d("Login", arrStudent.get(0).getStuName());
                                Log.d("Login", arrStudent.get(0).getStuNo());
                                Log.d("Login", arrStudent.get(0).getStuDOB());
                                Log.d("Login", arrStudent.get(0).getStuClass());
                                Log.d("Login", arrStudent.get(0).getStuPhone());
                                Log.d("Login", arrStudent.get(0).getStuGender());
                                Log.d("Login", arrStudent.get(0).getStuActive());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Students>> call, Throwable t) {
                            Toast.makeText(StudentLoginActivity.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                            //Log.d("Error", t.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void initUI() {
        edtStuLoginEmail = (EditText) findViewById(R.id.edt_stu_login_email);
        edtStuLoginPassword = (EditText) findViewById(R.id.edt_stu_login_password);
        btnStuLogin = (Button) findViewById(R.id.btn_stu_login);
        tvStuLoginForgotPassword = (TextView) findViewById(R.id.tv_stu_login_forgot_password);
        tvStuLoginToLoginAdmin = (TextView) findViewById(R.id.tv_stu_login_to_login_admin);
        tvStuLoginToRegister = (TextView) findViewById(R.id.tv_stu_login_to_register);
        ivStuLoginClose = (ImageView) findViewById(R.id.iv_stu_login_close);
    }
}