package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class AdminStudentViewProfileActivity extends AppCompatActivity {

    private ImageView ivAdStuViewProfileAvatar, ivAdStuViewProfileExit;
    private TextView tvAdStuViewProfileNo, tvAdStuViewProfileName, tvAdStuViewProfileDOB, tvAdStuViewProfileClass, tvAdStuViewProfilePhone, tvAdStuViewProfileEmail, tvAdStuViewProfileActive, tvAdStuViewProfileGender;
    private Button btnAdStuViewProfileUpdate, btnAdStuViewProfileExit;

    ArrayList<Student> studentArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_profile);

        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromStudentAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdStuViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdStuViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdStuViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminStudentViewProfileActivity.this, AdminStudentUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", studentArr);
                bundle.putInt("STUDENT_DATA_POSITION", position);
                intent.putExtra("STUDENT_DATA_FROM_AD_STU_VIEW_PROFILE_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvAdStuViewProfileName.setText(studentArr.get(position).getStuName());
        tvAdStuViewProfileNo.setText(studentArr.get(position).getStuNo());
        tvAdStuViewProfileDOB.setText(studentArr.get(position).getStuDOB());
        tvAdStuViewProfilePhone.setText(studentArr.get(position).getStuPhone());
        tvAdStuViewProfileEmail.setText(studentArr.get(position).getStuEmail());
        tvAdStuViewProfileClass.setText(studentArr.get(position).getStuClass());
        if (studentArr.get(position).getStuGender().equals("1")) {
            tvAdStuViewProfileGender.setText("Male");
        } else if (studentArr.get(position).getStuGender().equals("0")) {
            tvAdStuViewProfileGender.setText("Female");
        }

        if (studentArr.get(position).getStuActive().equals("1")) {
            tvAdStuViewProfileActive.setText("Status: Active");
        } else if (studentArr.get(position).getStuActive().equals("0")) {
            tvAdStuViewProfileActive.setText("Status: InActive");
        }

        if (!studentArr.get(position).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(position).getStuAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdStuViewProfileAvatar);
        } else {
            if (!studentArr.get(position).getStuGender().equals("-1")) {
                if (studentArr.get(position).getStuGender().equals("1")) {
                    ivAdStuViewProfileAvatar.setImageResource(R.drawable.male);
                } else {
                    ivAdStuViewProfileAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivAdStuViewProfileAvatar.setImageResource(R.drawable.graduated);
            }
        }
    }

    private void receiveDataFromStudentAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE");
        if (bundle != null) {
            studentArr = bundle.getParcelableArrayList("STUDENT_DATA_ARRAY");
            position = bundle.getInt("STUDENT_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdStuViewProfileAvatar = findViewById(R.id.iv_ad_stu_view_profile_avt);
        ivAdStuViewProfileExit = findViewById(R.id.iv_ad_stu_view_profile_exit);
        tvAdStuViewProfileName = findViewById(R.id.tv_ad_stu_view_profile_name);
        tvAdStuViewProfileNo = findViewById(R.id.tv_ad_stu_view_profile_no);
        tvAdStuViewProfileDOB = findViewById(R.id.tv_ad_stu_view_profile_dob);
        tvAdStuViewProfilePhone = findViewById(R.id.tv_ad_stu_view_profile_phone);
        tvAdStuViewProfileEmail = findViewById(R.id.tv_ad_stu_view_profile_email);
        tvAdStuViewProfileClass = findViewById(R.id.tv_ad_stu_view_profile_class);
        tvAdStuViewProfileActive = findViewById(R.id.tv_ad_stu_view_profile_active);
        tvAdStuViewProfileGender = findViewById(R.id.tv_ad_stu_view_profile_gender);
        btnAdStuViewProfileExit = findViewById(R.id.btn_ad_stu_view_profile_exit);
        btnAdStuViewProfileUpdate = findViewById(R.id.btn_ad_stu_view_profile_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(AdminStudentViewProfileActivity.this, AdminStudentViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}