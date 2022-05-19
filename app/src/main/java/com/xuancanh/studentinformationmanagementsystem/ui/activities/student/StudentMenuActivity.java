package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class StudentMenuActivity extends AppCompatActivity {

    // Activity need back home menu
    public static final int STUDENT_UPDATE_ACTIVITY = 1;
    public static final int STUDENT_CHANGE_PASSWORD_ACTIVITY = 2;
    public static final int STUDENT_VIEW_PROFILE_ACTIVITY = 7;
    public static final int RESULT_STUDENT_CHANGE_PASSWORD_OK = 10;
    public static final int RESULT_STUDENT_VIEW_PROFILE_OK = 10;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    ImageView ivStuAvatar, ivStuNavHeader;
    TextView tvStuName, tvStuClass, tvStuNavHeaderName, tvStuNavHeaderEmail, tvStuNavHeaderClass;
    Button btnStuViewProfile, btnUpdateProfile, btnStuChangePassword, btnStuLogout, btnStuNotice, btnStuReport;
    ArrayList<Student> studentArr;


    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        //Connect layout
        initUI();

        //Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Receive Data From Login
        receiveDataFromLogin();
        //Set on View
        initView();

        //Navigation Drawer
        navigationDrawer();

        //Logout Button
        btnStuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

                //Sign Out google
                switch (v.getId()) {
                    case R.id.btn_stu_logout:
                        signOut();
                        break;
                }
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
                Intent intent = new Intent(StudentMenuActivity.this, StudentChangePasswordActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_CHANGE_PASSWORD", studentArr);
                startActivityForResult(intent, STUDENT_CHANGE_PASSWORD_ACTIVITY);
            }
        });

        //Button View Profile
        btnStuViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMenuActivity.this, StudentViewProfileActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_VIEW_PROFILE", studentArr);
                startActivityForResult(intent, STUDENT_VIEW_PROFILE_ACTIVITY);
            }
        });

        //Button Notice
        btnStuNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMenuActivity.this, StudentNoticeFromAdminActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_NOTICE", studentArr);
                startActivity(intent);
            }
        });

        //Button Report
        btnStuReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMenuActivity.this, StudentReportActivity.class);
                intent.putExtra("STUDENT_DATA_FROM_MENU_TO_REPORT", studentArr);
                startActivity(intent);
            }
        });

        //Nav
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Fragment fragment = null;
                switch (id) {
                    case R.id.it_stu_nav_dra_menu_dashboard:
//                        fragment = new DashBoardFragment();
//                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_update_profile:
                        Intent intent = new Intent(StudentMenuActivity.this, StudentUpdateActivity.class);
                        intent.putExtra("STUDENT_DATA_FROM_MENU_TO_UPDATE", studentArr);
                        startActivityForResult(intent, STUDENT_CHANGE_PASSWORD_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_notice:
                        Intent intent1 = new Intent(StudentMenuActivity.this, StudentNoticeFromAdminActivity.class);
                        intent1.putExtra("STUDENT_DATA_FROM_MENU_TO_NOTICE", studentArr);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_report:
                        Intent intent2 = new Intent(StudentMenuActivity.this, StudentReportActivity.class);
                        intent2.putExtra("STUDENT_DATA_FROM_MENU_TO_REPORT", studentArr);
                        startActivity(intent2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_stu_profile:
                        Intent intent3 = new Intent(StudentMenuActivity.this, StudentViewProfileActivity.class);
                        intent3.putExtra("STUDENT_DATA_FROM_MENU_TO_VIEW_PROFILE", studentArr);
                        startActivityForResult(intent3, STUDENT_VIEW_PROFILE_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_change_password:
                        Intent intent4 = new Intent(StudentMenuActivity.this, StudentChangePasswordActivity.class);
                        intent4.putExtra("STUDENT_DATA_FROM_MENU_TO_CHANGE_PASSWORD", studentArr);
                        startActivityForResult(intent4, STUDENT_CHANGE_PASSWORD_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_logout:
                        logout();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void navigationDrawer() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Sign Out Google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
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
                Toast.makeText(StudentMenuActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
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
        tvStuNavHeaderName.setText(studentArr.get(0).getStuName());
        tvStuNavHeaderEmail.setText(studentArr.get(0).getStuEmail());
        tvStuNavHeaderClass.setText(studentArr.get(0).getStuClass());
        tvStuName.setText(studentArr.get(0).getStuName());
        tvStuClass.setText(studentArr.get(0).getStuClass());
        if (!studentArr.get(0).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
                    .placeholder(R.drawable.graduated)
                    .error(R.drawable.graduated)
                    .into(ivStuAvatar);
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
                    .placeholder(R.drawable.graduated)
                    .error(R.drawable.graduated)
                    .into(ivStuNavHeader);
        } else {
            if (!studentArr.get(0).getStuGender().equals("-1")) {
                if (studentArr.get(0).getStuGender().equals("1")) {
                    ivStuAvatar.setImageResource(R.drawable.male);
                    ivStuNavHeader.setImageResource(R.drawable.male);
                } else {
                    ivStuAvatar.setImageResource(R.drawable.female);
                    ivStuNavHeader.setImageResource(R.drawable.female);
                }
            } else {
                ivStuAvatar.setImageResource(R.drawable.graduated);
                ivStuNavHeader.setImageResource(R.drawable.graduated);
            }
        }

    }

    @Override
    protected void onResume() {
        navigationView.getMenu().findItem(R.id.it_stu_nav_dra_menu_dashboard).setChecked(true);
        super.onResume();
    }

    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_LOGIN_TO_MENU");
    }

    private void initUI() {
        ivStuAvatar = findViewById(R.id.iv_stu_avt);
        tvStuName = findViewById(R.id.tv_stu_name);
        tvStuClass = findViewById(R.id.tv_stu_home_class);
        btnStuLogout = findViewById(R.id.btn_stu_logout);
        btnStuViewProfile = findViewById(R.id.btn_stu_view_profile);
        btnUpdateProfile = findViewById(R.id.btn_stu_update_profile);
        btnStuChangePassword = findViewById(R.id.btn_stu_change_password);
        btnStuNotice = findViewById(R.id.btn_stu_menu_notice);
        btnStuReport = findViewById(R.id.btn_stu_menu_report);

        drawerLayout = findViewById(R.id.dl_student_drawer);
        toolbar = findViewById(R.id.tb_student_toolBar);
        navigationView = findViewById(R.id.nv_student);
        View hView = navigationView.getHeaderView(0);
        tvStuNavHeaderEmail = hView.findViewById(R.id.tv_stu_nav_header_email);
        tvStuNavHeaderName = hView.findViewById(R.id.tv_stu_nav_header_name);
        tvStuNavHeaderClass = hView.findViewById(R.id.tv_stu_nav_header_class);
        ivStuNavHeader = hView.findViewById(R.id.iv_stu_nav_header);
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
        //Change password
        if (resultCode == RESULT_STUDENT_CHANGE_PASSWORD_OK) {
            if (requestCode == STUDENT_CHANGE_PASSWORD_ACTIVITY) {
                studentArr = data.getParcelableArrayListExtra("STUDENT_DATA_FROM_CHANGE_PASSWORD_TO_MENU");
            }
        }
        //View Profile(maybe don't need)
        if (resultCode == RESULT_STUDENT_VIEW_PROFILE_OK) {
            if (requestCode == STUDENT_VIEW_PROFILE_ACTIVITY) {
                studentArr = data.getParcelableArrayListExtra("STUDENT_DATA_FROM_VIEW_PROFILE_TO_MENU");
                initView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }

    }
}