package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

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
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentChangePasswordActivity extends AppCompatActivity {
    EditText edtStuChangePasswordCurrentPassword, edtStuChangePasswordNewPassword, edtStuChangePasswordRetypeNewPassword;
    Button btnStuChangePasswordSave, btnStuChangePasswordExit;
    ImageView ivStuChangePasswordExit;

    ArrayList<Student> studentArr;
    String currentPassword, newPassword, retypeNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_change_password);

        //Connect layout
        initUI();

        // Receive data from StuUpdateActivity
        receiveDataFromMenu();

        //Button Exit
        btnStuChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivStuChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button Save
        btnStuChangePasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = edtStuChangePasswordCurrentPassword.getText().toString();
                newPassword = edtStuChangePasswordNewPassword.getText().toString();
                retypeNewPassword = edtStuChangePasswordRetypeNewPassword.getText().toString();
                if (currentPassword.length() > 0 && newPassword.length() > 0 && retypeNewPassword.length() > 0) {
                    if (currentPassword.equals(studentArr.get(0).getStuPassword()) && newPassword.equals(retypeNewPassword)) {

                        DataClient checkData = APIUtils.getData();
                        Call<String> callback = checkData.ChangePasswordStudentData(studentArr.get(0).getStuId(), newPassword);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String res = response.body();
                                if (res.trim().equals("STUDENT_CHANGE_PASSWORD_SUCCESSFUL")) {
                                    Toast.makeText(StudentChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    //Update password and Sent Data to Update
                                    studentArr.get(0).setStuPassword(newPassword);
                                    backToMenu();
                                } else if (res.trim().equals("STUDENT_CHANGE_PASSWORD_FAILED")) {
                                    Toast.makeText(StudentChangePasswordActivity.this, "Student Email Or ID Is Incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StudentChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Wrong:", t.getMessage());
                            }
                        });
                    } else if (currentPassword.equals(studentArr.get(0).getStuPassword()) && !newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(StudentChangePasswordActivity.this, "New passwords and retype new passwords are not the same", Toast.LENGTH_SHORT).show();
                    } else if (!currentPassword.equals(studentArr.get(0).getStuPassword()) && newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(StudentChangePasswordActivity.this, "The current password Incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentChangePasswordActivity.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Receive data from STUDENT_DATA_FROM_MENU_TO_CHANGE_PASSWORD
    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_CHANGE_PASSWORD");
    }

    private void initUI() {
        edtStuChangePasswordCurrentPassword = findViewById(R.id.edt_stu_change_password_current_password);
        edtStuChangePasswordNewPassword = findViewById(R.id.edt_stu_change_password_new_password);
        edtStuChangePasswordRetypeNewPassword = findViewById(R.id.edt_stu_change_password_retype_new_password);
        btnStuChangePasswordSave = findViewById(R.id.btn_stu_change_password_save);
        btnStuChangePasswordExit = findViewById(R.id.btn_stu_change_password_exit);
        ivStuChangePasswordExit = findViewById(R.id.iv_stu_change_password_exit);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }

    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("STUDENT_DATA_FROM_CHANGE_PASSWORD_TO_MENU", studentArr);
        setResult(StudentMenuActivity.RESULT_STUDENT_CHANGE_PASSWORD_OK, intent);
        finish();
    }
}