package com.xuancanh.studentinformationmanagementsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.model.Student;

import java.util.ArrayList;

public class StudentMenuActivity extends AppCompatActivity {
    // Activity need back home menu
    public static final int STUDENT_UPDATE_ACTIVITY = 1;

    final String ACTIVE = "Active";
    final String NO_ACTIVE = "InActive";
    ImageView ivStuAvatar;
    TextView tvStuName, tvStuNo, tvStuActive;
    Button btnStuViewProfile, btnUpdateProfile, btnStuChangePassword, btnStuLogout;
    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        //Connect layout
        initUI();
        //Receive Data From Login
        receiveDataFromLogin();
        //Set on View
        initView();

        //Logout Button
        btnStuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //Button Update Profile
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMenuActivity.this, StudentUpdateActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_UPDATE", studentArr);
                startActivityForResult(intent, STUDENT_UPDATE_ACTIVITY);
            }
        });

        //Button Change Password
        btnStuChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(studentArr.get(0).getStuName() + ", are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(StudentMenuActivity.this, StudentLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        tvStuName.setText(studentArr.get(0).getStuName());
        tvStuNo.setText(studentArr.get(0).getStuNo());
        if (studentArr.get(0).getStuActive().equals("1")) {
            tvStuActive.setText(ACTIVE);
        } else {
            tvStuActive.setText(NO_ACTIVE);
        }
        if (!studentArr.get(0).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
//                    .placeholder(R.drawable.graduated)
//                    .error(R.drawable.graduated)
                    .into(ivStuAvatar);
        } else {
            if (!studentArr.get(0).getStuGender().equals("-1")) {
                if (studentArr.get(0).getStuGender().equals("1")) {
                    ivStuAvatar.setImageResource(R.drawable.male);
                } else {
                    ivStuAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivStuAvatar.setImageResource(R.drawable.graduated);
            }
        }

    }

    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_LOGIN_TO_MENU");
    }

    private void initUI() {
        ivStuAvatar = findViewById(R.id.iv_stu_avt);
        tvStuName = findViewById(R.id.tv_stu_name);
        tvStuNo = findViewById(R.id.tv_stu_no);
        tvStuActive = findViewById(R.id.tv_stu_active);
        btnStuLogout = findViewById(R.id.btn_stu_logout);
        btnStuViewProfile = findViewById(R.id.btn_stu_view_profile);
        btnUpdateProfile = findViewById(R.id.btn_stu_update_profile);
        btnStuChangePassword = findViewById(R.id.btn_stu_change_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == STUDENT_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                studentArr = data.getParcelableArrayListExtra("STUDENT_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        logout();
    }
}