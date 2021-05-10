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

import com.xuancanh.studentinformationmanagementsystem.model.Student;
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

    ArrayList<Student> studentArr;
    String studentEmail, studentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //Connect layout
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
                studentEmail = edtStuLoginEmail.getText().toString();
                studentPassword = edtStuLoginPassword.getText().toString();
                if(studentEmail.length() > 0 && studentPassword.length() > 0) {
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<List<Student>> callback = dataClient.LoginStudentData(studentEmail, studentPassword);
                    callback.enqueue(new Callback<List<Student>>() {
                        @Override
                        public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                            studentArr = (ArrayList<Student>)response.body();
                            if(studentArr.size() > 0) {
                                //Send Data and finish
                                Intent intent = new Intent(StudentLoginActivity.this, StudentMenuActivity.class);
                                intent.putExtra("STUDENT_DATA_FROM_LOGIN_TO_MENU", studentArr);
                                startActivity(intent);
                                finish();
                                Toast.makeText(StudentLoginActivity.this, "Welcome " + studentArr.get(0).getStuName(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Student>> call, Throwable t) {
                            Toast.makeText(StudentLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
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