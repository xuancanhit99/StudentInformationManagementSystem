package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentReportActivity extends AppCompatActivity {

    private Button btnStuReport, btnStuReportBack;
    private ImageView ivStuReportBack;
    private TextView tvStuReportReply;
    private EditText edtStuReportContent;

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report);

        //Connect layout
        initUI();

        //Receive data from menu
        receiveDataFromMenu();

        //Set on View
        initView();

        //Button back
        btnStuReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        ivStuReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button Report
        btnStuReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String report = edtStuReportContent.getText().toString();
                DataClient checkData = APIUtils.getData();
                Call<String> callback = checkData.ReportStudentData(studentArr.get(0).getStuId(), report);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if (res.trim().equals("STUDENT_REPORT_SUCCESSFUL")) {
                            hideKeyboard(v);
                            Toast.makeText(StudentReportActivity.this, "Reported", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Report Fail:", t.getMessage());
                    }
                });
            }
        });
    }

    private void backToMenu() {
        finish();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void initView() {
        tvStuReportReply.setText(studentArr.get(0).getStuReply());
        edtStuReportContent.setText(studentArr.get(0).getStuReport());
    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_REPORT");
    }

    private void initUI() {
        btnStuReport = findViewById(R.id.btn_stu_report);
        btnStuReportBack = findViewById(R.id.btn_stu_report_back);
        ivStuReportBack = findViewById(R.id.iv_stu_report_back);
        tvStuReportReply = findViewById(R.id.tv_stu_report_reply_content);
        edtStuReportContent = findViewById(R.id.edt_stu_report_content);
    }
}