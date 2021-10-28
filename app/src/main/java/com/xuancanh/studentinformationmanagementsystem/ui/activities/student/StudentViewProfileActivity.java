package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class StudentViewProfileActivity extends AppCompatActivity {

    public static final int STUDENT_UPDATE = 1;
    public static final int RESULT_UPDATE_OK = 2;

    private ImageView ivStuViewProfileAvatar, ivStuViewProfileExit;
    private TextView tvStuViewProfileNo, tvStuViewProfileName, tvStuViewProfileDOB, tvStuViewProfileClass, tvStuViewProfilePhone, tvStuViewProfileEmail, tvStuViewProfileActive, tvStuViewProfileGender;
    private Button btnStuViewProfileUpdate, btnStuViewProfileExit;

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_profile);
        //Connect layout
        initUI();

        //Receive Data from student menu
        receiveDataFromMenu();

        //Set on View
        initView();


        //Button Exit
        btnStuViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivStuViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnStuViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentViewProfileActivity.this, StudentUpdateActivity.class);
                //Replace STUDENT_DATA_FROM_MENU_TO_UPDATE for STUDENT_DATA_FROM_VIEW_PROFILE_TO_UPDATE
                //Update just receive 1 time
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_UPDATE", studentArr);
                startActivityForResult(intent, STUDENT_UPDATE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        //Replace STUDENT_DATA_FROM_MENU_TO_UPDATE for STUDENT_DATA_FROM_VIEW_PROFILE_TO_UPDATE
        //Update just receive 1 time
        if (requestCode == StudentMenuActivity.STUDENT_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                studentArr = data.getParcelableArrayListExtra("STUDENT_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
    }

    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("STUDENT_DATA_FROM_VIEW_PROFILE_TO_MENU", studentArr);
        setResult(StudentMenuActivity.RESULT_STUDENT_VIEW_PROFILE_OK, intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvStuViewProfileName.setText(studentArr.get(0).getStuName());
        tvStuViewProfileNo.setText(studentArr.get(0).getStuNo());
        tvStuViewProfileDOB.setText(studentArr.get(0).getStuDOB());
        tvStuViewProfilePhone.setText(studentArr.get(0).getStuPhone());
        tvStuViewProfileEmail.setText(studentArr.get(0).getStuEmail());
        tvStuViewProfileClass.setText(studentArr.get(0).getStuClass());
        if (studentArr.get(0).getStuGender().equals("1")) {
            tvStuViewProfileGender.setText("Male");
        } else if (studentArr.get(0).getStuGender().equals("0")) {
            tvStuViewProfileGender.setText("Female");
        }

        if (studentArr.get(0).getStuActive().equals("1")) {
            tvStuViewProfileActive.setText("Status: Active");
        } else if (studentArr.get(0).getStuActive().equals("0")) {
            tvStuViewProfileActive.setText("Status: InActive");
        }

        if (!studentArr.get(0).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivStuViewProfileAvatar);
        } else {
            if (!studentArr.get(0).getStuGender().equals("-1")) {
                if (studentArr.get(0).getStuGender().equals("1")) {
                    ivStuViewProfileAvatar.setImageResource(R.drawable.male);
                } else {
                    ivStuViewProfileAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivStuViewProfileAvatar.setImageResource(R.drawable.graduated);
            }
        }
    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_VIEW_PROFILE");
    }

    private void initUI() {
        ivStuViewProfileAvatar = findViewById(R.id.iv_stu_view_profile_avt);
        ivStuViewProfileExit = findViewById(R.id.iv_stu_view_profile_exit);
        tvStuViewProfileName = findViewById(R.id.tv_stu_view_profile_name);
        tvStuViewProfileNo = findViewById(R.id.tv_stu_view_profile_no);
        tvStuViewProfileDOB = findViewById(R.id.tv_stu_view_profile_dob);
        tvStuViewProfilePhone = findViewById(R.id.tv_stu_view_profile_phone);
        tvStuViewProfileEmail = findViewById(R.id.tv_stu_view_profile_email);
        tvStuViewProfileClass = findViewById(R.id.tv_stu_view_profile_class);
        tvStuViewProfileActive = findViewById(R.id.tv_stu_view_profile_active);
        tvStuViewProfileGender = findViewById(R.id.tv_stu_view_profile_gender);
        btnStuViewProfileExit = findViewById(R.id.btn_stu_view_profile_exit);
        btnStuViewProfileUpdate = findViewById(R.id.btn_stu_view_profile_update);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}