package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Admin;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminChangePasswordActivity extends AppCompatActivity {

    EditText edtAdminChangePasswordCurrentPassword, edtAdminChangePasswordNewPassword, edtAdminChangePasswordRetypeNewPassword;
    Button btnAdminChangePasswordSave, btnAdminChangePasswordExit;
    ImageView ivAdminChangePasswordExit;

    ArrayList<Admin> adminArr;
    String currentPassword, newPassword, retypeNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        //Connect layout
        initUI();

        // Receive data from AdminUpdateActivity
        receiveDataFromUpdate();

        //Button Exit
        btnAdminChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToUpdate();
            }
        });

        //ImageView Back
        ivAdminChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToUpdate();
            }
        });

        //Button Save
        btnAdminChangePasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = edtAdminChangePasswordCurrentPassword.getText().toString();
                newPassword = edtAdminChangePasswordNewPassword.getText().toString();
                retypeNewPassword = edtAdminChangePasswordRetypeNewPassword.getText().toString();
                if (currentPassword.length() > 0 && newPassword.length() > 0 && retypeNewPassword.length() > 0) {
                    if (currentPassword.equals(adminArr.get(0).getAdPassword()) && newPassword.equals(retypeNewPassword)) {

                        DataClient checkData = APIUtils.getData();
                        Call<String> callback = checkData.ChangePasswordAdminData(adminArr.get(0).getAdId(), newPassword);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String res = response.body();
                                if (res.trim().equals("ADMIN_CHANGE_PASSWORD_SUCCESSFUL")) {
                                    Toast.makeText(AdminChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    //Update password and Sent Data to Update
                                    adminArr.get(0).setAdPassword(newPassword);
                                    backToUpdate();
                                } else if (res.trim().equals("ADMIN_CHANGE_PASSWORD_FAILED")) {
                                    Toast.makeText(AdminChangePasswordActivity.this, "Admin Email Or ID Is Incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AdminChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Wrong:", t.getMessage());
                            }
                        });
                    } else if (currentPassword.equals(adminArr.get(0).getAdPassword()) && !newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(AdminChangePasswordActivity.this, "New passwords and retype new passwords are not the same", Toast.LENGTH_SHORT).show();
                    } else if (!currentPassword.equals(adminArr.get(0).getAdPassword()) && newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(AdminChangePasswordActivity.this, "The current password Incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminChangePasswordActivity.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Receive data from ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD
    private void receiveDataFromUpdate() {
        Intent intent = getIntent();
        adminArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD");
    }

    private void initUI() {
        edtAdminChangePasswordCurrentPassword = findViewById(R.id.edt_admin_change_password_current_password);
        edtAdminChangePasswordNewPassword = findViewById(R.id.edt_admin_change_password_new_password);
        edtAdminChangePasswordRetypeNewPassword = findViewById(R.id.edt_admin_change_password_retype_new_password);
        btnAdminChangePasswordSave = findViewById(R.id.btn_admin_change_password_save);
        btnAdminChangePasswordExit = findViewById(R.id.btn_admin_change_password_exit);
        ivAdminChangePasswordExit = findViewById(R.id.iv_admin_change_password_exit);
    }

    @Override
    public void onBackPressed() {
        backToUpdate();
    }

    private void backToUpdate() {
        Intent intent = getIntent();
        intent.putExtra("ADMIN_DATA_FROM_CHANGE_PASSWORD_TO_UPDATE", adminArr);
        setResult(AdminUpdateActivity.RESULT_CHANGE_PASSWORD_OK, intent);
        finish();
    }
}