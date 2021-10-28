package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class StudentNoticeFromAdminActivity extends AppCompatActivity {

    private Button btnStuNoticeFromAdBack, btnStuNoticeFromAdReport;
    private ImageView ivStuNoticeFromAdBack;
    private TextView tvStuNoticeFromAdContent;

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notice_from_admin);

        //Connect layout
        initUI();

        //receive Data form menu
        receiveDataFromMenu();


        //Button back
        btnStuNoticeFromAdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        ivStuNoticeFromAdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        btnStuNoticeFromAdReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open
                Intent intent = new Intent(StudentNoticeFromAdminActivity.this, StudentReportActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_REPORT", studentArr);
                startActivity(intent);
            }
        });
    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_NOTICE");
        tvStuNoticeFromAdContent.setText(studentArr.get(0).getStuNotice());
    }


    private void backToHome() {
        finish();
    }

    private void initUI() {
        btnStuNoticeFromAdBack = findViewById(R.id.btn_stu_notice_from_ad_back);
        btnStuNoticeFromAdReport = findViewById(R.id.btn_stu_notice_from_ad_report);
        ivStuNoticeFromAdBack = findViewById(R.id.iv_stu_notice_from_ad_back);
        tvStuNoticeFromAdContent = findViewById(R.id.tv_stu_notice_from_ad_content);
    }
}