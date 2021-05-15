package com.xuancanh.studentinformationmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuancanh.studentinformationmanagementsystem.adapter.StudentAdapter;
import com.xuancanh.studentinformationmanagementsystem.model.Student;
import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentViewAllActivity extends AppCompatActivity {

    private ArrayList<Student> studentArr;
    RecyclerView rvItems;
    private StudentAdapter studentAdapter;

    ImageButton ibStuAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_all);

        //Circle Button Add
        ibStuAdd = findViewById(R.id.ib_stu_add);
        ibStuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminStudentViewAllActivity.this, AdminStudentAddActivity.class));
                //Co can dua lai du lieu
            }
        });

        addControls();
        readData();

    }

    private void readData() {
        studentArr.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Student>> callback = dataClient.AdminViewAllStudentData();
        callback.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                studentArr = (ArrayList<Student>)response.body();

                if(studentArr.size() > 0) {
                    studentAdapter = new StudentAdapter(getApplicationContext(), studentArr);
                    //studentAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(studentAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.d("Error load all stu", t.getMessage());
            }
        });
    }

    private void addControls() {
        studentArr = new ArrayList<Student>();
        rvItems = findViewById(R.id.rv_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(AdminStudentViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        studentAdapter = new StudentAdapter(getApplicationContext(), studentArr); // this
        rvItems.setAdapter(studentAdapter);
    }

}