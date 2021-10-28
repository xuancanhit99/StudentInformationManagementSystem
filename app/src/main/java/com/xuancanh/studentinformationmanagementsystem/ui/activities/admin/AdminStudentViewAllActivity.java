package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xuancanh.studentinformationmanagementsystem.ui.tools.DividerItemDecorator;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.ui.view.adapters.StudentListAdapter;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentViewAllActivity extends AppCompatActivity {

    private ArrayList<Student> studentArr, studentArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlAdStuViewAll;
    private StudentListAdapter studentListAdapter;

    ImageButton ibStuAdd;
    EditText edtStuViewAllSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_all);

        //Search
        edtStuViewAllSearch = findViewById(R.id.edt_stu_view_all_search);
        edtStuViewAllSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textSearch = charSequence.toString();
                FilterData(textSearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //SwipeRefreshLayout
        srlAdStuViewAll = findViewById(R.id.srl_ad_stu_view_all);
        srlAdStuViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                studentListAdapter.notifyDataSetChanged();
                srlAdStuViewAll.setRefreshing(false);
            }
        });

        //Circle Button Add
        ibStuAdd = findViewById(R.id.ib_stu_add);
        ibStuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminStudentViewAllActivity.this, AdminStudentAddActivity.class));
            }
        });

        addControls();
        readData();

    }

    //Filter data
    @SuppressLint("NotifyDataSetChanged")
    public void FilterData(String textSearch) {
        textSearch = textSearch.toLowerCase(Locale.getDefault());
        Log.d("filter", textSearch);
        studentArr.clear();
        if(textSearch.length() == 0) {
            studentArr.addAll(studentArrSearch);
            Log.d("load data", "all");
        }
        else {
            Log.d("load data", "filtered");
            for (int i=0; i<studentArrSearch.size(); i++) {
                if(studentArrSearch.get(i).getStuName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        studentArrSearch.get(i).getStuClass().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    studentArr.add(studentArrSearch.get(i));
                }
            }
        }
        studentListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        studentArr.clear();
        studentArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Student>> callback = dataClient.AdminViewAllStudentData();
        callback.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                studentArr = (ArrayList<Student>) response.body();

                if (studentArr.size() > 0) {
                    studentArrSearch.addAll(studentArr);
                    studentListAdapter = new StudentListAdapter(getApplicationContext(), studentArr);
                    //studentListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(studentListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.d("Error load all stu", t.getMessage());
            }
        });
    }

    private void addControls() {
        studentArr = new ArrayList<>();
        studentArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_ad_stu_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(AdminStudentViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        studentListAdapter = new StudentListAdapter(getApplicationContext(), studentArr); // this
        rvItems.setAdapter(studentListAdapter);
    }

}