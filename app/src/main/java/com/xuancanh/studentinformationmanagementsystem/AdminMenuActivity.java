package com.xuancanh.studentinformationmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.model.Admin;

import java.util.ArrayList;

public class AdminMenuActivity extends AppCompatActivity {

    Button addStudent, viewStudent, noticeStudent, btnHomeMenuLogout, btnAdminEdit;
    ImageView ivAdminAvt;
    TextView tvAdminName, tvAdminEmail;
    ArrayList<Admin> adminArr;

    // Activity need back home menu
    public static final int ADMIN_UPDATE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        // Connect Layout
        initUI();
        //Receive Data From Login
        receiveDataFromLogin();
        // Set on View
        initView();


        //Logout Button
        btnHomeMenuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //View All Button
        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminStudentViewAllActivity.class));
            }
        });

        //Add Student Button
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminStudentAddActivity.class));
            }
        });

        //Button Notice
        noticeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminNoticeToStudentActivity.class);
                startActivity(intent);
            }
        });

        //Button Edit
        btnAdminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminUpdateActivity.class);
                intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", adminArr);
                startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
            }
        });
    }

    //Receive Data From Login
    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        adminArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_LOGIN_TO_MENU");
    }

    // Connect Layout
    public void initUI() {
        btnAdminEdit = findViewById(R.id.btn_admin_edit);
        ivAdminAvt = findViewById(R.id.iv_admin_avt);
        tvAdminName = findViewById(R.id.tv_admin_name);
        tvAdminEmail = findViewById(R.id.tv_admin_email);
        btnHomeMenuLogout = findViewById(R.id.btn_home_menu_logout);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
        noticeStudent = findViewById(R.id.btn_student_notice);
    }

    //Set on View
    private void initView() {
        tvAdminName.setText(adminArr.get(0).getAdName());
        tvAdminEmail.setText(adminArr.get(0).getAdEmail());
        if(!adminArr.get(0).getAdAvatar().equals("")) {
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdminAvt);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if (requestCode == ADMIN_UPDATE_ACTIVITY) {
            if(resultCode == RESULT_OK) {
                adminArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(adminArr.get(0).getAdName()+", are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminLoginActivity.class);
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
}