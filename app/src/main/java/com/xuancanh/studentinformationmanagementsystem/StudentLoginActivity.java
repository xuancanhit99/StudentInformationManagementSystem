package com.xuancanh.studentinformationmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.model.Student;
import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText edtStuLoginEmail, edtStuLoginPassword;
    private Button btnStuLogin;
    private TextView tvStuLoginForgotPassword, tvStuLoginToLoginAdmin, tvStuLoginToRegister;
    private ImageView ivStuLoginClose;
    private CheckBox cbStudentLoginRememberMe;
    private SharedPreferences.Editor loginPrefsEditor;

    ArrayList<Student> studentArr;
    String studentEmail, studentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //Connect layout
        initUI();

        //Login When Enter - Done
        edtStuLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    studentLogin();
                    rememberMe();
                }
                return false;
            }
        });

        //Remember Me
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean rememberMeCheck = loginPreferences.getBoolean("STUDENT_REMEMBER_ME", false);
        if (rememberMeCheck) {
            edtStuLoginEmail.setText(loginPreferences.getString("STUDENT_EMAIL", ""));
            edtStuLoginPassword.setText(loginPreferences.getString("STUDENT_PASSWORD", ""));
            cbStudentLoginRememberMe.setChecked(true);
        }

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
                if(isEmailValid(edtStuLoginEmail)) {
                    studentLogin();
                    rememberMe();
                }
                else {
                    edtStuLoginEmail.setError("Email address not valid");
                }
            }
        });

    }
    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if(email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void studentLogin() {
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
                        Toast.makeText(StudentLoginActivity.this, "Welcome " + studentArr.get(0).getStuName(), Toast.LENGTH_SHORT).show();
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

    private void rememberMe() {
        if (cbStudentLoginRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("STUDENT_REMEMBER_ME", true);
            loginPrefsEditor.putString("STUDENT_EMAIL", studentEmail);
            loginPrefsEditor.putString("STUDENT_PASSWORD", studentPassword);
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.apply();
    }

    private void initUI() {
        edtStuLoginEmail = findViewById(R.id.edt_stu_login_email);
        edtStuLoginPassword = findViewById(R.id.edt_stu_login_password);
        btnStuLogin = findViewById(R.id.btn_stu_login);
        tvStuLoginForgotPassword = findViewById(R.id.tv_stu_login_forgot_password);
        tvStuLoginToLoginAdmin = findViewById(R.id.tv_stu_login_to_login_admin);
        tvStuLoginToRegister = findViewById(R.id.tv_stu_login_to_register);
        ivStuLoginClose = findViewById(R.id.iv_stu_login_close);
        cbStudentLoginRememberMe = findViewById(R.id.cb_stu_login_remember_me);
    }
}