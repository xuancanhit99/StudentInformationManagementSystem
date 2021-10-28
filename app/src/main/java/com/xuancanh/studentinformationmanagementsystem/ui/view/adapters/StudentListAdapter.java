package com.xuancanh.studentinformationmanagementsystem.ui.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.ui.activities.admin.AdminStudentViewProfileActivity;
import com.xuancanh.studentinformationmanagementsystem.ui.interfaces.ItemClickListener;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;

import java.util.ArrayList;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {
    //Form for adapter
    Context context;
    ArrayList<Student> studentArr;

    public StudentListAdapter(Context context, ArrayList<Student> studentArr) {
        this.context = context;
        this.studentArr = studentArr;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
        return new StudentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentArr.get(position);

        String studentName = student.getStuName();
        String studentClass = student.getStuClass();


        if (!student.getStuAvatar().equals("")) {
            Picasso.get()
                    .load(student.getStuAvatar())
                    .placeholder(R.drawable.graduated)
                    .error(R.drawable.graduated)
                    .into(holder.ivStuAvt);
        } else {
            if (!student.getStuGender().equals("-1")) {
                if (student.getStuGender().equals("1")) {
                    holder.ivStuAvt.setImageResource(R.drawable.male);
                } else {
                    holder.ivStuAvt.setImageResource(R.drawable.female);
                }
            } else {
                holder.ivStuAvt.setImageResource(R.drawable.graduated);
            }
        }


        holder.tvStuName.setText(studentName);
        holder.tvStuClass.setText(studentClass);

        //Click for RecycleView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Student " + student.getStuName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AdminStudentViewProfileActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", studentArr);
                    bundle.putInt("STUDENT_DATA_POSITION", position);
                    intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                    //Tao Update Student moi

                    //replace STUDENT_DATA_FROM_MENU_TO_UPDATE instead of STUDENT_DATA_FROM_ADAPTER UpdateStudentActivity just receiver 1 key
//                    ArrayList<Student> studentTemp = new ArrayList<>();
//                    studentTemp.add(student);
//                    intent.putExtra("STUDENT_DATA_FROM_MENU_TO_UPDATE", studentTemp);
//                    view.getContext().startActivity(intent);
                    //((Activity)view.getContext()).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return studentArr == null ? 0 : studentArr.size();
    }


    //Data ViewHolder class
    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivStuAvt;
        TextView tvStuName, tvStuClass;

        ItemClickListener itemClickListener;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStuName = itemView.findViewById(R.id.tv_stu_name);
            tvStuClass = itemView.findViewById(R.id.tv_stu_class);
            ivStuAvt = itemView.findViewById(R.id.iv_stu_avt);

            //Turn On Click for RecycleView
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        //onClick for RecycleView
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        //onLongClick for RecycleView
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
            //return false; // if not use
        }
    }
}
