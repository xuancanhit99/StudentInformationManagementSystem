package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminStudentAddActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String studentName, studentEmail, studentPassword, studentNo, studentDOB, studentClass, studentAvatar, studentPhone;

    private EditText edtAdStuAddName, edtAdStuAddEmail, edtAdStuAddPassword, edtAdStuAddNo, edtAdStuAddDOB, edtAdStuAddClass, edtAdStuAddPhone;
    private Button btnAdStuAddSave, btnAdStuAddExit, btnAdStuAddTakePhoto, btnAdStuAddChoosePhoto;
    private ImageButton imBtnAdStuAddDelDOB;
    private ImageView ivAdStuAddAvt, ivAdStuAddExit;

    private RadioGroup rgAdStuAddGender, rgAdStuAddStatus;
    private RadioButton rbAdStuAddMale, rbAdStuAddFemale, rbAdStuAddActive, rbAdStuAddInactive;
    String updateGender = "1", status = "0";

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_add);

        //Connect layout
        initUI();

        //Button Delete Date of birth
        imBtnAdStuAddDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAdStuAddDOB.setText("");
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
        edtAdStuAddDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdminStudentAddActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //RadioGroup Gender
        rgAdStuAddGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_stu_add_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });

        //RadioGroup Status
        rgAdStuAddStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_stu_add_active) {
                    status = "1";
                } else {
                    status = "0";
                }
            }
        });

        //Button Choose Photo
        btnAdStuAddChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdStuAddTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdStuAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ImageView Exit
        ivAdStuAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnAdStuAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdStuAddName)) {
                    edtAdStuAddName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtAdStuAddPassword)) {
                    edtAdStuAddPassword.setError("Please enter student's password");
                }
                if (isEmptyEditText(edtAdStuAddEmail)) {
                    edtAdStuAddEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtAdStuAddEmail)) {
                    studentName = edtAdStuAddName.getText().toString();
                    studentEmail = edtAdStuAddEmail.getText().toString();
                    studentPassword = edtAdStuAddPassword.getText().toString();
                    studentNo = edtAdStuAddNo.getText().toString();
                    studentDOB = edtAdStuAddDOB.getText().toString();
                    studentClass = edtAdStuAddClass.getText().toString();
                    studentPhone = edtAdStuAddPhone.getText().toString();
                    if (studentName.length() > 0 && studentPassword.length() > 0 && studentEmail.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                } else {
                    edtAdStuAddEmail.setError("Email address not valid");
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

    private void uploadInfo() {
        DataClient insertData = APIUtils.getData();
        Call<String> callback;
        if (!realPath.equals("")) {
            callback = insertData.AdminAddStudentData(studentNo, studentName, studentDOB, studentClass, updateGender, studentPhone, studentEmail, studentPassword, status, APIUtils.BASE_URL + "images/" + studentAvatar);
        } else {
            callback = insertData.AdminAddStudentData(studentNo, studentName, studentDOB, studentClass, updateGender, studentPhone, studentEmail, studentPassword, status, "NO_IMAGE_ADD_STUDENT");
        }
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Ad Stu Add Info", result);
                if (result.trim().equals("ADD_STUDENT_SUCCESSFUL")) {
                    Toast.makeText(AdminStudentAddActivity.this, "Student " + studentName + " Added Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Ad Stu Add Info", t.getMessage());
            }
        });
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadStudentPhoto(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    studentAvatar = response.body();
                    uploadInfo();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Photo", t.getMessage());
            }
        });
    }


    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtAdStuAddDOB.setText(sdf.format(calendar.getTime()));
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
                    ivAdStuAddAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdStuAddAvt.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdStuAddAvt.getDrawable()).getBitmap();
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
        ivAdStuAddAvt = findViewById(R.id.iv_ad_stu_add_avt);
        ivAdStuAddExit = findViewById(R.id.iv_ad_stu_add_exit);

        edtAdStuAddName = findViewById(R.id.edt_ad_stu_add_name);
        edtAdStuAddNo = findViewById(R.id.edt_ad_stu_add_no);
        edtAdStuAddDOB = findViewById(R.id.edt_ad_stu_add_dob);
        edtAdStuAddPhone = findViewById(R.id.edt_ad_stu_add_phone);
        edtAdStuAddEmail = findViewById(R.id.edt_ad_stu_add_email);
        edtAdStuAddClass = findViewById(R.id.edt_ad_stu_add_class);
        edtAdStuAddPassword = findViewById(R.id.edt_ad_stu_add_password);

        rgAdStuAddGender = findViewById(R.id.rg_ad_stu_add_gender);
        rgAdStuAddStatus = findViewById(R.id.rg_ad_stu_add_status);

        rbAdStuAddFemale = findViewById(R.id.rb_ad_stu_add_female);
        rbAdStuAddMale = findViewById(R.id.rb_ad_stu_add_male);
        rbAdStuAddActive = findViewById(R.id.rb_ad_stu_add_active);
        rbAdStuAddInactive = findViewById(R.id.rb_ad_stu_add_inactive);

        btnAdStuAddSave = findViewById(R.id.btn_ad_stu_add_save);
        btnAdStuAddExit = findViewById(R.id.btn_ad_stu_add_exit);
        btnAdStuAddTakePhoto = findViewById(R.id.btn_ad_stu_add_take_photo);
        btnAdStuAddChoosePhoto = findViewById(R.id.btn_ad_stu_add_choose_photo);
        imBtnAdStuAddDelDOB = findViewById(R.id.im_btn_ad_stu_add_del_dob);
    }
}