package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class StudentLearningResultsActivity extends AppCompatActivity {

    private Button btnReExam, btnExit;
    private ImageView ivExit, ivAvt;
    private TextView tvMath, tvEng, tvPhy, tvInfo, tvPro, tvEco, tvPhi;
    private TextView tvActive;

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_learning_results);

        //Receive Data from student menu
        receiveDataFromMenu();

        //Set on View
        initView();


        //Button Exit
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });


    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_LEARNING_RESULTS");
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        btnExit = findViewById(R.id.btn_stu_learning_results_exit);
        btnReExam = findViewById(R.id.btn_stu_learning_results_re_exam);
        ivExit = findViewById(R.id.iv_stu_learning_results_exit);
        ivAvt = findViewById(R.id.iv_stu_learning_results_avt);

        tvMath = findViewById(R.id.tv_stu_learning_results_math);
        tvEng = findViewById(R.id.tv_stu_learning_results_english);
        tvPhy = findViewById(R.id.tv_stu_learning_results_physical);
        tvInfo = findViewById(R.id.tv_stu_learning_results_informatics);
        tvPro = findViewById(R.id.tv_stu_learning_results_programming);
        tvEco = findViewById(R.id.tv_stu_learning_results_economy);
        tvPhi = findViewById(R.id.tv_stu_learning_results_philosophy);

        tvActive = findViewById(R.id.tv_stu_learning_results_active);


        tvMath.setText(studentArr.get(0).getStuMath());
        tvEng.setText(studentArr.get(0).getStuEng());
        tvPhy.setText(studentArr.get(0).getStuPhy());
        tvInfo.setText(studentArr.get(0).getStuInfo());
        tvPro.setText(studentArr.get(0).getStuPro());
        tvEco.setText(studentArr.get(0).getStuEco());
        tvPhi.setText(studentArr.get(0).getStuPhi());

        if (studentArr.get(0).getStuActive().equals("1")) {
            tvActive.setText("Status: Active");
        } else if (studentArr.get(0).getStuActive().equals("0")) {
            tvActive.setText("Status: InActive");
        }

        if (!studentArr.get(0).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAvt);
        } else {
            if (!studentArr.get(0).getStuGender().equals("-1")) {
                if (studentArr.get(0).getStuGender().equals("1")) {
                    ivAvt.setImageResource(R.drawable.male);
                } else {
                    ivAvt.setImageResource(R.drawable.female);
                }
            } else {
                ivAvt.setImageResource(R.drawable.graduated);
            }
        }
    }

    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("STUDENT_DATA_FROM_LEARNING_RESULTS_TO_MENU", studentArr);
        setResult(StudentMenuActivity.RESULT_STUDENT_VIEW_PROFILE_OK, intent);
        finish();
    }
}