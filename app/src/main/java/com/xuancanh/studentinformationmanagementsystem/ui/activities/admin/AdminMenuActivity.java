package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Admin;

import java.util.ArrayList;

public class AdminMenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    Button addStudent, viewStudent, noticeStudent, reportStudent, btnHomeMenuLogout, btnAdminEdit;
    ImageView ivAdminAvt, ivAdNavHeader;
    TextView tvAdminName, tvAdNavHeaderName, tvAdNavHeaderEmail;
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
        //Navigation Drawer
        navigationDrawer();


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
                startActivity(new Intent(AdminMenuActivity.this, AdminNoticeToStudentActivity.class));
            }
        });

        //Button Report
        reportStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminStudentReportActivity.class));
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

        //Nav
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Fragment fragment = null;
                switch (id) {
                    case R.id.it_ad_nav_dra_menu_dashboard:
//                        fragment = new DashBoardFragment();
//                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_add_student:
                        startActivity(new Intent(AdminMenuActivity.this, AdminStudentAddActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_view_all:
                        startActivity(new Intent(AdminMenuActivity.this, AdminStudentViewAllActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_notice:
                        startActivity(new Intent(AdminMenuActivity.this, AdminNoticeToStudentActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_report:
                        startActivity(new Intent(AdminMenuActivity.this, AdminStudentReportActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_admin_profile:
                        Intent intent = new Intent(AdminMenuActivity.this, AdminUpdateActivity.class);
                        intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", adminArr);
                        startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_change_password:
                        Intent intent1 = new Intent(AdminMenuActivity.this, AdminChangePasswordActivity.class);
                        //Replace
                        intent1.putExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", adminArr);
                        startActivityForResult(intent1, RESULT_OK);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_logout:
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

    //Load Fragment
//    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment).commit();
//        drawerLayout.closeDrawer(GravityCompat.START);
//        fragmentTransaction.addToBackStack(null);
//    }

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
        btnHomeMenuLogout = findViewById(R.id.btn_home_menu_logout);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
        noticeStudent = findViewById(R.id.btn_student_notice);
        reportStudent = findViewById(R.id.btn_student_report);
        drawerLayout = findViewById(R.id.dl_admin_drawer);
        toolbar = findViewById(R.id.tb_admin_toolBar);
        navigationView = findViewById(R.id.nv_admin);
        View hView = navigationView.getHeaderView(0);
        tvAdNavHeaderEmail = hView.findViewById(R.id.tv_admin_nav_header_email);
        tvAdNavHeaderName = hView.findViewById(R.id.tv_admin_nav_header_name);
        ivAdNavHeader = hView.findViewById(R.id.iv_ad_nav_header);
    }

    //Set on View
    private void initView() {
        tvAdminName.setText(adminArr.get(0).getAdName());
        tvAdNavHeaderName.setText(adminArr.get(0).getAdName());
        tvAdNavHeaderEmail.setText(adminArr.get(0).getAdEmail());
        if (!adminArr.get(0).getAdAvatar().equals("")) {
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdminAvt);
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdNavHeader);
        }
    }

    @Override
    protected void onResume() {
        navigationView.getMenu().findItem(R.id.it_ad_nav_dra_menu_dashboard).setChecked(true);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADMIN_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                adminArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU");
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

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(adminArr.get(0).getAdName() + ", are you sure want to logout?");
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