package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNoticeToStudentActivity extends AppCompatActivity {

    private Button btnAdNoticeToStuSend, btnAdNoticeToStuBack;
    private EditText edtAdNoticeToStuContent;
    private ImageView ivAdNoticeToStuBack;

    ArrayList<Student> studentArr;
    String notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice_to_student);

        //Connect layout
        initUI();

        //getData from server
        getData();

        //Button Send
        btnAdNoticeToStuSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice = edtAdNoticeToStuContent.getText().toString();
                DataClient insertData = APIUtils.getData();
                Call<String> callbackInfo = insertData.AdminNoticeToStudentData(notice);
                callbackInfo.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if (res.trim().equals("NOTICE_UPDATE_SUCCESSFUL")) {
                            hideKeyboard(v);
                            Toast.makeText(AdminNoticeToStudentActivity.this, "Successfully sent notification to all students", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Error Updated Notice", t.getMessage());
                    }
                });
            }
        });

        //Button Back
        btnAdNoticeToStuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Image View Back
        ivAdNoticeToStuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void backToMenu() {
        finish();
    }

    private void getData() {
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Student>> callback = dataClient.AdminViewAllStudentData();
        callback.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                studentArr = (ArrayList<Student>) response.body();
                if (studentArr.size() > 0) {
                    //Set on View
                    edtAdNoticeToStuContent.setText(studentArr.get(0).getStuNotice());
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.d("Error load all stu", t.getMessage());
            }
        });
    }

    private void initUI() {
        btnAdNoticeToStuBack = findViewById(R.id.btn_ad_notice_to_stu_back);
        btnAdNoticeToStuSend = findViewById(R.id.btn_ad_notice_to_stu_send);
        edtAdNoticeToStuContent = findViewById(R.id.edt_ad_notice_to_stu_content);
        ivAdNoticeToStuBack = findViewById(R.id.iv_ad_notice_to_stu_back);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}