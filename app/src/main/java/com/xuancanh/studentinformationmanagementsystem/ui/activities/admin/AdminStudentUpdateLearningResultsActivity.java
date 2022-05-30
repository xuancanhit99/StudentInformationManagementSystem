package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.xuancanh.studentinformationmanagementsystem.R;

import java.util.Arrays;
import java.util.List;

public class AdminStudentUpdateLearningResultsActivity extends AppCompatActivity {

    private Spinner snMath, snEng, snPhy, snInfo, snPro, snEco, snPhi;
    private Button btnSave, btnExit;
    private ImageView ivExit;

    ArrayAdapter<String> adapter;
    private final List<String> scores = Arrays.asList("Отлично","Хорошо","Удовлетворительно","Неудовлетворительно");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_update_learning_results);
        initView();
    }


    private void adapterSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snMath.setAdapter(adapter);
        snEng.setAdapter(adapter);
        snPhy.setAdapter(adapter);
        snInfo.setAdapter(adapter);
        snPro.setAdapter(adapter);
        snEco.setAdapter(adapter);
        snPhi.setAdapter(adapter);


        //String defaultScore = "Неудовлетворительно";
////        ArrayAdapter arrayAdapter = (ArrayAdapter) snTheyGet.getAdapter();
////        int spinnerPosition = arrayAdapter.getPosition(defaultCurrency);
        //int spinnerPositionYouSend = adapter.getPosition(defaultScore);
        //snYouSend.setSelection(spinnerPositionYouSend);

//        String defaultCurrencyTheyGet = "RUB";
//        int spinnerPositionTheyGet = adapter.getPosition(defaultCurrencyTheyGet);
//        snTheyGet.setSelection(spinnerPositionTheyGet);
    }


    private void initView() {

        snMath = findViewById(R.id.sn_ad_stu_up_result_math);
        snEng = findViewById(R.id.sn_ad_stu_up_result_eng);
        snPhy= findViewById(R.id.sn_ad_stu_up_result_phy);
        snInfo = findViewById(R.id.sn_ad_stu_up_result_info);
        snPro = findViewById(R.id.sn_ad_stu_up_result_pro);
        snEco = findViewById(R.id.sn_ad_stu_up_result_eco);
        snPhi = findViewById(R.id.sn_ad_stu_up_result_phi);

        btnExit = findViewById(R.id.btn_ad_stu_up_result_exit);
        btnSave = findViewById(R.id.btn_ad_stu_up_result_save);
        ivExit = findViewById(R.id.iv_ad_stu_up_result_exit);
    }
}