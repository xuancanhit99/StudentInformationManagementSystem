package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

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

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.ui.activities.student.StudentLoginActivity;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Admin;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText edtAdminLoginEmail, edtAdminLoginPassword;
    private Button btnAdminLogin;
    private TextView tvAdminLoginForgotPassword, tvAdminLoginToLoginStudent;
    private ImageView ivAdminLoginClose;
    private CheckBox cbAdminLoginRememberMe;
    private SharedPreferences.Editor loginPrefsEditor;

    ArrayList<Admin> adminArr;
    String AdminEmail, AdminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //Connect layout
        initUI();

        //Login When Enter - Done
        edtAdminLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    adminLogin();
                    rememberMe();
                }
                return false;
            }
        });


        //Remember Me
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean rememberMeCheck = loginPreferences.getBoolean("ADMIN_REMEMBER_ME", false);
        if (rememberMeCheck) {
            edtAdminLoginEmail.setText(loginPreferences.getString("ADMIN_EMAIL", ""));
            edtAdminLoginPassword.setText(loginPreferences.getString("ADMIN_PASSWORD", ""));
            cbAdminLoginRememberMe.setChecked(true);
        }

        // TView Forgot Password
        tvAdminLoginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, AdminForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

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
                if (isEmailValid(edtAdminLoginEmail)) {
                    adminLogin();
                    rememberMe();
                } else {
                    edtAdminLoginEmail.setError("Email address not valid");
                }
            }
        });
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if (email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void rememberMe() {
        if (cbAdminLoginRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("ADMIN_REMEMBER_ME", true);
            loginPrefsEditor.putString("ADMIN_EMAIL", AdminEmail);
            loginPrefsEditor.putString("ADMIN_PASSWORD", AdminPassword);
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.apply();
    }

    private void adminLogin() {
        AdminEmail = edtAdminLoginEmail.getText().toString();
        AdminPassword = edtAdminLoginPassword.getText().toString();
        if (AdminEmail.length() > 0 && AdminPassword.length() > 0) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<List<Admin>> callback = dataClient.LoginAdminData(AdminEmail, AdminPassword);
            callback.enqueue(new Callback<List<Admin>>() {
                @Override
                public void onResponse(Call<List<Admin>> call, Response<List<Admin>> response) {
                    adminArr = (ArrayList<Admin>) response.body();
                    if (adminArr.size() > 0) {
                        Intent intent = new Intent(AdminLoginActivity.this, AdminMenuActivity.class);
                        intent.putExtra("ADMIN_DATA_FROM_LOGIN_TO_MENU", adminArr);
                        startActivity(intent);
                        finish();
                        Toast.makeText(AdminLoginActivity.this, "Welcome " + adminArr.get(0).getAdName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Admin>> call, Throwable t) {
                    Log.d("Err Login: ", t.getMessage());
                    Toast.makeText(AdminLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (AdminEmail.length() == 0 && AdminPassword.length() == 0) {
            Toast.makeText(AdminLoginActivity.this, "Please type your email and password", Toast.LENGTH_SHORT).show();
        } else if (AdminEmail.length() > 0 && AdminPassword.length() == 0) {
            Toast.makeText(AdminLoginActivity.this, "Please type your password", Toast.LENGTH_SHORT).show();
        } else if (AdminEmail.length() == 0 && AdminPassword.length() > 0) {
            Toast.makeText(AdminLoginActivity.this, "Please type your email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AdminLoginActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        edtAdminLoginEmail = findViewById(R.id.edt_admin_login_email);
        edtAdminLoginPassword = findViewById(R.id.edt_admin_login_password);
        btnAdminLogin = findViewById(R.id.btn_admin_login);
        tvAdminLoginForgotPassword = findViewById(R.id.tv_admin_login_forgot_password);
        tvAdminLoginToLoginStudent = findViewById(R.id.tv_admin_login_to_login_student);
        ivAdminLoginClose = findViewById(R.id.iv_admin_login_close);
        cbAdminLoginRememberMe = findViewById(R.id.cb_admin_login_remember_me);
    }
}