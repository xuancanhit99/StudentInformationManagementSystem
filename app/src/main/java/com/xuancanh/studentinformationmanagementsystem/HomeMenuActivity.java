package com.xuancanh.studentinformationmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.model.Admin;

import java.util.ArrayList;

public class HomeMenuActivity extends AppCompatActivity {

    Button addStudent, viewStudent, exitApp, btnAdminEdit;
    ImageView ivAdminAvt;
    TextView tvAdminName, tvAdminEmail;
    ArrayList<Admin> adminArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        // Anh xa
        initUI();

        initView();





        //Exit Button
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        //View All Button
//        viewStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeMenuActivity.this, ViewAllActivity.class));
//            }
//        });
//
//        //Add Student Button
//        addStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeMenuActivity.this, AddActivity.class));
//            }
//        });

        //Button Edit
        btnAdminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenuActivity.this, AdminUpdateActivity.class);
                intent.putExtra("ADMIN_DATA_TO_UPDATE", adminArrayList);
                startActivity(intent);
                finish();
//                String nameFolder = adminArrayList.get(0).getAdAvatar();
//                nameFolder = nameFolder.substring(nameFolder.lastIndexOf("/"));
//                DataClient dataClient = APIUtils.getData();
//                retrofit2.Call<String> callback = dataClient.DeleteAdminData(adminArrayList.get(0).getAdId(), nameFolder);
//                callback.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        String res = response.body();
//                        if(res.trim().equals("Ok")) {
//                            Toast.makeText(HomeMenuActivity.this, "Delete Admin " + adminArrayList.get(0).getAdName() + " Successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(HomeMenuActivity.this, AdminLoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        else {
//                            //Toast.makeText(HomeMenuActivity.this, "Fail " + res.trim(), Toast.LENGTH_SHORT).show();
//                            Log.d("Delete Err", res.trim());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Log.d("Error Retrofit response", t.getMessage());
//                    }
//                });
            }
        });
    }

    // anh xa
    public void initUI() {

        btnAdminEdit = findViewById(R.id.btn_admin_edit);
        ivAdminAvt = findViewById(R.id.iv_admin_avt);
        tvAdminName = findViewById(R.id.tv_admin_name);
        tvAdminEmail = findViewById(R.id.tv_admin_email);

        exitApp = findViewById(R.id.btn_app_exit);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
    }

    private void initView() {
        Intent intent = getIntent();
        adminArrayList = intent.getParcelableArrayListExtra("ADMIN_DATA");
        tvAdminName.setText(adminArrayList.get(0).getAdName());
        tvAdminEmail.setText(adminArrayList.get(0).getAdEmail());
        Picasso.get()
                .load(adminArrayList.get(0).getAdAvatar())
                .placeholder(R.drawable.admin)
                .error(R.drawable.admin)
                .into(ivAdminAvt);
    }
}