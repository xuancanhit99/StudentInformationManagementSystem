package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentUpdateLearningResultsActivity extends AppCompatActivity {

    private Spinner snMath, snEng, snPhy, snInfo, snPro, snEco, snPhi;
    private Button btnSave, btnExit;
    private ImageView ivExit;

    ArrayAdapter<String> adapter;
    private final List<String> scores = Arrays.asList("Отлично", "Хорошо", "Удовлетворительно", "Неудовлетворительно", "");

    ArrayList<Student> studentArr;
    int position;

    String scoMath, scoEng, scoPhy, scoInfo, scoPro, scoEco, scoPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_update_learning_results);

        //Receive Data from AdStuViewProfile
        receiveDataFromAdStuViewProfile();

        initView();

        adapterSpinner();


    }

    private void receiveDataFromAdStuViewProfile() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("STUDENT_DATA_FROM_AD_STU_VIEW_PROFILE_TO_RESULTS");
        if (bundle != null) {
            studentArr = bundle.getParcelableArrayList("STUDENT_DATA_ARRAY");
            position = bundle.getInt("STUDENT_DATA_POSITION");

            //Toast.makeText(this, studentArr.get(position).getStuMath().toString(), Toast.LENGTH_SHORT).show();
        }
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

        String defaultScore = "";
        int spinnerPosition = adapter.getPosition(defaultScore);
        snMath.setSelection(spinnerPosition);
        snEng.setSelection(spinnerPosition);
        snPhy.setSelection(spinnerPosition);
        snInfo.setSelection(spinnerPosition);
        snPro.setSelection(spinnerPosition);
        snEco.setSelection(spinnerPosition);
        snPhi.setSelection(spinnerPosition);

        snMath.setSelection(adapter.getPosition(studentArr.get(position).getStuMath().trim()));
        snEng.setSelection(adapter.getPosition(studentArr.get(position).getStuEng().trim()));
        snPhy.setSelection(adapter.getPosition(studentArr.get(position).getStuPhy().trim()));
        snInfo.setSelection(adapter.getPosition(studentArr.get(position).getStuInfo().trim()));
        snPro.setSelection(adapter.getPosition(studentArr.get(position).getStuPro().trim()));
        snEco.setSelection(adapter.getPosition(studentArr.get(position).getStuEco().trim()));
        snPhi.setSelection(adapter.getPosition(studentArr.get(position).getStuPhi().trim()));
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminStudentUpdateLearningResultsActivity.this, AdminStudentViewProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", studentArr);
        bundle.putInt("STUDENT_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
        startActivity(intent);
        finish();
    }

    private void uploadScores() {
        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.AdminUpdateStudentScore(studentArr.get(position).getStuId(), scoMath, scoEng, scoPhy, scoInfo, scoPro, scoEco, scoPhi);
        studentArr.get(position).setStuMath(scoMath);
        studentArr.get(position).setStuEng(scoEng);
        studentArr.get(position).setStuPhy(scoPhy);
        studentArr.get(position).setStuInfo(scoInfo);
        studentArr.get(position).setStuPro(scoPro);
        studentArr.get(position).setStuEco(scoEco);
        studentArr.get(position).setStuPhi(scoPhi);

        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Stu Scr", result);
                if (result.trim().equals("STUDENT_UPDATE_SCORES_SUCCESSFUL")) {
                    Toast.makeText(AdminStudentUpdateLearningResultsActivity.this, "Successfully Updated Student " + studentArr.get(position).getStuName() + " Scores", Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Info", t.getMessage());
            }
        });
    }


    private void initView() {
        snMath = findViewById(R.id.sn_ad_stu_up_result_math);
        snEng = findViewById(R.id.sn_ad_stu_up_result_eng);
        snPhy = findViewById(R.id.sn_ad_stu_up_result_phy);
        snInfo = findViewById(R.id.sn_ad_stu_up_result_info);
        snPro = findViewById(R.id.sn_ad_stu_up_result_pro);
        snEco = findViewById(R.id.sn_ad_stu_up_result_eco);
        snPhi = findViewById(R.id.sn_ad_stu_up_result_phi);

        btnExit = findViewById(R.id.btn_ad_stu_up_result_exit);
        btnSave = findViewById(R.id.btn_ad_stu_up_result_save);
        ivExit = findViewById(R.id.iv_ad_stu_up_result_exit);

        snMath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoMath = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snEng.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoEng = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snPhy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoPhy = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoInfo = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoPro = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snEco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoEco = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        snPhi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scoPhi = scores.get(i);
                //Log.d("LOG", sco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Button Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOG", scoMath + scoEng + scoPhy + scoInfo + scoPro + scoEco + scoPhi);
                uploadScores();
            }
        });

        //Button Exit
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMenu();
            }
        });

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMenu();
            }
        });
    }
}