package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentUpdateActivity extends AppCompatActivity {
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String studentName, studentEmail, studentPassword, studentNo, studentDOB, studentClass, studentAvatar, studentPhone;

    private EditText edtAdStuUpdateName, edtAdStuUpdateEmail, edtAdStuUpdatePassword, edtAdStuUpdateNo, edtAdStuUpdateDOB, edtAdStuUpdateClass, edtAdStuUpdatePhone;
    private Button btnAdStuUpdateSave, btnAdStuUpdateDelete, btnAdStuUpdateExit, btnAdStuUpdateTakePhoto, btnAdStuUpdateChoosePhoto;
    private ImageButton imBtnAdStuUpdateDelDOB;
    private ImageView ivAdStuUpdateAvt, ivAdStuUpdateExit;

    private RadioGroup rgAdStuUpdateGender, rgAdStuUpdateStatus;
    private RadioButton rbAdStuUpdateMale, rbAdStuUpdateFemale, rbAdStuUpdateActive, rbAdStuUpdateInactive;
    String updateGender = "1", status = "0";

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    ArrayList<Student> studentArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_update);
        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdStuViewProfile();

        //Set on View
        initView();

        //Button Delete Date of birth
        imBtnAdStuUpdateDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAdStuUpdateDOB.setText("");
            }
        });

        //Set click text view Date of birth
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        edtAdStuUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdminStudentUpdateActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //RadioGroup Gender
        rgAdStuUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_stu_update_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });

        //RadioGroup Status
        rgAdStuUpdateStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_stu_update_active) {
                    status = "1";
                } else {
                    status = "0";
                }
            }
        });

        //Button Choose Photo
        btnAdStuUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdStuUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdStuUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdStuUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdStuUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this student account");
                builder.setMessage("Are you sure want to delete account student " + studentArr.get(position).getStuName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccStudent();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Button Save
        btnAdStuUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdStuUpdateName)) {
                    edtAdStuUpdateName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtAdStuUpdatePassword)) {
                    edtAdStuUpdatePassword.setError("Please enter student's password");
                }
                if (isEmptyEditText(edtAdStuUpdateEmail)) {
                    edtAdStuUpdateEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtAdStuUpdateEmail)) {
                    studentName = edtAdStuUpdateName.getText().toString();
                    studentEmail = edtAdStuUpdateEmail.getText().toString();
                    studentPassword = edtAdStuUpdatePassword.getText().toString();
                    studentNo = edtAdStuUpdateNo.getText().toString();
                    studentDOB = edtAdStuUpdateDOB.getText().toString();
                    studentClass = edtAdStuUpdateClass.getText().toString();
                    studentPhone = edtAdStuUpdatePhone.getText().toString();
                    if (studentName.length() > 0 && studentEmail.length() > 0 && studentPassword.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                } else {
                    edtAdStuUpdateEmail.setError("Email address not valid");
                }
            }
        });
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if (email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isEmptyEditText(EditText editText) {
        String str = editText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    private void deleteAccStudent() {
        String currentAvatar;
        if (!studentArr.get(position).getStuAvatar().equals("")) {
            currentAvatar = studentArr.get(position).getStuAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_STUDENT_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteStudentData(studentArr.get(position).getStuId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("STUDENT_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminStudentUpdateActivity.this, "Deleted Student " + studentArr.get(position).getStuName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminStudentUpdateActivity.this, AdminStudentViewAllActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("Delete Err", res.trim());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Retrofit response", t.getMessage());
            }
        });
    }


    private void receiveDataFromAdStuViewProfile() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("STUDENT_DATA_FROM_AD_STU_VIEW_PROFILE_TO_UPDATE");
        if (bundle != null) {
            studentArr = bundle.getParcelableArrayList("STUDENT_DATA_ARRAY");
            position = bundle.getInt("STUDENT_DATA_POSITION");
        }
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callbackPhoto = dataClient.UploadStudentPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    studentAvatar = response.body();
                    Log.d("Updated Stuc Photo", studentAvatar);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Photo", t.getMessage());
            }
        });
    }

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (studentArr.get(position).getStuAvatar().equals("")) {
            //curAva = "", newAva=""
            currentAvatar = "NO_CURRENT_IMAGE_STUDENT_UPDATE";
            if (realPath.equals("")) {
                newAvatar = "";
            } else {
                newAvatar = APIUtils.BASE_URL + "images/" + studentAvatar;
            }
        } else {
            if (realPath.equals("")) {
                currentAvatar = "NO_CURRENT_IMAGE_STUDENT_UPDATE";
                newAvatar = studentArr.get(position).getStuAvatar();
            } else {
                currentAvatar = studentArr.get(position).getStuAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
                newAvatar = APIUtils.BASE_URL + "images/" + studentAvatar;
            }
        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.AdminUpdateStudentData(studentArr.get(position).getStuId(), studentNo, studentName, studentDOB, studentClass, studentPhone, studentEmail, newAvatar, updateGender, studentPassword, status, currentAvatar);
        studentArr.get(position).setStuNo(studentNo);
        studentArr.get(position).setStuName(studentName);
        studentArr.get(position).setStuDOB(studentDOB);
        studentArr.get(position).setStuClass(studentClass);
        studentArr.get(position).setStuPhone(studentPhone);
        studentArr.get(position).setStuEmail(studentEmail);
        studentArr.get(position).setStuAvatar(newAvatar);
        studentArr.get(position).setStuGender(updateGender);
        studentArr.get(position).setStuActive(status);
        studentArr.get(position).setStuPassword(studentPassword);
        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Stu Info", result);
                if (result.trim().equals("STUDENT_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(AdminStudentUpdateActivity.this, "Successfully Updated Student Information " + studentName, Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Info", t.getMessage());
            }
        });
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminStudentUpdateActivity.this, AdminStudentViewProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", studentArr);
        bundle.putInt("STUDENT_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
        startActivity(intent);
        finish();
    }

    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtAdStuUpdateDOB.setText(sdf.format(calendar.getTime()));
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                imageUri = data.getData();
                realPath = getRealPathFromURI(imageUri);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivAdStuUpdateAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdStuUpdateAvt.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }
    }

    // Get Real Path when upload photo(from uri - image/mame_image)
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivAdStuUpdateAvt.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image From Take Photo");
        values.put(MediaStore.Images.Media.BUCKET_ID, "image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "take photo and save to gallery");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        ivAdStuUpdateAvt = findViewById(R.id.iv_ad_stu_update_avt);
        ivAdStuUpdateExit = findViewById(R.id.iv_ad_stu_update_exit);

        edtAdStuUpdateName = findViewById(R.id.edt_ad_stu_update_name);
        edtAdStuUpdateNo = findViewById(R.id.edt_ad_stu_update_no);
        edtAdStuUpdateDOB = findViewById(R.id.edt_ad_stu_update_dob);
        edtAdStuUpdatePhone = findViewById(R.id.edt_ad_stu_update_phone);
        edtAdStuUpdateEmail = findViewById(R.id.edt_ad_stu_update_email);
        edtAdStuUpdateClass = findViewById(R.id.edt_ad_stu_update_class);
        edtAdStuUpdatePassword = findViewById(R.id.edt_ad_stu_update_password);

        rgAdStuUpdateGender = findViewById(R.id.rg_ad_stu_update_gender);
        rgAdStuUpdateStatus = findViewById(R.id.rg_ad_stu_update_status);

        rbAdStuUpdateFemale = findViewById(R.id.rb_ad_stu_update_female);
        rbAdStuUpdateMale = findViewById(R.id.rb_ad_stu_update_male);
        rbAdStuUpdateActive = findViewById(R.id.rb_ad_stu_update_active);
        rbAdStuUpdateInactive = findViewById(R.id.rb_ad_stu_update_inactive);

        btnAdStuUpdateSave = findViewById(R.id.btn_ad_stu_update_save);
        btnAdStuUpdateDelete = findViewById(R.id.btn_ad_stu_update_delete);
        btnAdStuUpdateExit = findViewById(R.id.btn_ad_stu_update_exit);
        btnAdStuUpdateTakePhoto = findViewById(R.id.btn_ad_stu_update_take_photo);
        btnAdStuUpdateChoosePhoto = findViewById(R.id.btn_ad_stu_update_choose_photo);
        imBtnAdStuUpdateDelDOB = findViewById(R.id.im_btn_ad_stu_update_del_dob);
    }

    private void initView() {
        edtAdStuUpdateName.setText(studentArr.get(position).getStuName());
        edtAdStuUpdateNo.setText(studentArr.get(position).getStuNo());
        edtAdStuUpdateDOB.setText(studentArr.get(position).getStuDOB());
        edtAdStuUpdatePhone.setText(studentArr.get(position).getStuPhone());
        edtAdStuUpdateEmail.setText(studentArr.get(position).getStuEmail());
        edtAdStuUpdateClass.setText(studentArr.get(position).getStuClass());
        edtAdStuUpdatePassword.setText(studentArr.get(position).getStuPassword());

        if (!studentArr.get(position).getStuGender().equals("-1")) {
            if (studentArr.get(position).getStuGender().equals("1")) {
                rbAdStuUpdateMale.setChecked(true);
                updateGender = "1";
            } else {
                rbAdStuUpdateFemale.setChecked(true);
                updateGender = "0";
            }
        }


        if (studentArr.get(position).getStuActive().equals("1")) {
            rbAdStuUpdateActive.setChecked(true);
            status = "1";
        } else {
            rbAdStuUpdateInactive.setChecked(true);
            status = "0";
        }


        if (!studentArr.get(position).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(position).getStuAvatar())
                    .placeholder(R.drawable.graduated)
                    .error(R.drawable.graduated)
                    .into(ivAdStuUpdateAvt);
        } else {
            if (!studentArr.get(position).getStuGender().equals("-1")) {
                if (studentArr.get(position).getStuGender().equals("1")) {
                    ivAdStuUpdateAvt.setImageResource(R.drawable.male);
                } else {
                    ivAdStuUpdateAvt.setImageResource(R.drawable.female);
                }
            } else {
                ivAdStuUpdateAvt.setImageResource(R.drawable.graduated);
            }
        }

    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}