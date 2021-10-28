package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentReportActivity extends AppCompatActivity {

    private TextView tvAdStuReportNumOfNotification, tvAdStuReportFromStudents, tvAdStuReportContent;
    private EditText edtAdStuReportReplyContent;
    private Spinner snAdStuReportListStu;
    private Button btnAdStuReportReply, btnAdStuReportBack;
    private ImageView ivAdStuReportBack;

    ArrayList<Student> studentArr;
    ArrayList<String> studentNameArr;
    int posN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_report);

        //Connect layout
        initUI();

        //Get Data from server
        getData();

        //Set On View
        snAdStuReportListStu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos = String.valueOf(position);
                posN = Integer.parseInt(pos);
                tvAdStuReportContent.setText(studentArr.get(posN).getStuReport());
                edtAdStuReportReplyContent.setText(studentArr.get(posN).getStuReply());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdStuReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        ivAdStuReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        btnAdStuReportReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent = edtAdStuReportReplyContent.getText().toString();
                DataClient insertData = APIUtils.getData();
                Call<String> callbackInfo = insertData.AdminReplyStudentData(studentArr.get(posN).getStuId(), replyContent);
                callbackInfo.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if (res.trim().equals("STUDENT_REPLY_UPDATE_SUCCESSFUL")) {
                            hideKeyboard(v);
                            Toast.makeText(AdminStudentReportActivity.this, "Report answered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Error Updated Report", t.getMessage());
                    }
                });
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setNotification() {
        int numOfNotification = 0, numOfNotificationNeedReply = 0;
        StringBuilder nameStudentHasReport = new StringBuilder();
        for (int i = 0; i < studentArr.size(); i++) {
            if (!studentArr.get(i).getStuReport().equals("")) {
                numOfNotification++;
                if (nameStudentHasReport.toString().equals(""))
                    nameStudentHasReport.append(studentArr.get(i).getStuName());
                else
                    nameStudentHasReport.append(", ").append(studentArr.get(i).getStuName());
                if (studentArr.get(i).getStuReply().equals("")) {
                    numOfNotificationNeedReply++;
                }
            }
        }

        tvAdStuReportNumOfNotification.setText(" You have " + numOfNotification + " reports from students, " +
                "you need reply " + numOfNotificationNeedReply + " reports.");
        tvAdStuReportFromStudents.setText("Names of students reported: " + nameStudentHasReport + ".");
        Toast.makeText(AdminStudentReportActivity.this, "You have " + numOfNotification + " reports", Toast.LENGTH_SHORT).show();
    }

    private void adapterSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, studentNameArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snAdStuReportListStu.setAdapter(adapter);
    }

    private void studentArrToNameStringArrStudent() {
        studentNameArr = new ArrayList<>();
        for (int i = 0; i < studentArr.size(); i++) {
            studentNameArr.add(studentArr.get(i).getStuName());
        }
    }

    private void getData() {
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Student>> callback = dataClient.AdminViewAllStudentData();
        callback.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                studentArr = (ArrayList<Student>) response.body();
                if (studentArr.size() > 0) {
                    studentArrToNameStringArrStudent();
                    //Adapter Spinner
                    adapterSpinner();
                    setNotification();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.d("Error load all stu", t.getMessage());
            }
        });
    }

    private void backToMenu() {
        finish();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void initUI() {
        tvAdStuReportContent = findViewById(R.id.tv_ad_stu_report_content);
        tvAdStuReportNumOfNotification = findViewById(R.id.tv_ad_stu_report_num_of_notification);
        tvAdStuReportFromStudents = findViewById(R.id.tv_ad_stu_report_from_students);
        edtAdStuReportReplyContent = findViewById(R.id.edt_ad_stu_report_reply_content);
        snAdStuReportListStu = findViewById(R.id.sn_ad_stu_report_list_stu);
        btnAdStuReportReply = findViewById(R.id.btn_ad_stu_report_reply);
        btnAdStuReportBack = findViewById(R.id.btn_ad_stu_report_back);
        ivAdStuReportBack = findViewById(R.id.iv_ad_stu_report_back);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}