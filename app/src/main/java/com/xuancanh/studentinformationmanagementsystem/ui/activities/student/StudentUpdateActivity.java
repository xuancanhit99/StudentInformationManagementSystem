package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

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

public class StudentUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    private ImageView ivStuUpdateAvatar, ivStuUpdateExit;
    private EditText edtStuUpdateNo, edtStuUpdateName, edtStuUpdateDOB, edtStuUpdateClass, edtStuUpdatePhone, edtStuUpdateEmail;
    private Button btnStuUpdateSave, btnStuUpdateExit, btnStuUpdateDelete, btnStuUpdateTakePhoto, btnStuUpdateChoosePhoto;
    private ImageButton imBtnStuUpdateDelDOB;
    private RadioGroup rgStuUpdateGender;
    private RadioButton rbStuUpdateMale, rbStuUpdateFemale;
    String updateGender = "1";
    String realPath = "";
    String studentNo, studentName, studentDOB, studentClass, studentPhone, studentEmail, studentAvatar;
    Uri imageUri;

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    ArrayList<Student> studentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);

        //Connect layout
        initUI();

        //Receive Data from student menu
        receiveDataFromMenu();

        //Set on View
        initView();


        //Button Choose Photo
        btnStuUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnStuUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Delete Acc Student
        btnStuUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this student account");
                builder.setMessage("Are you sure want to delete account student " + studentArr.get(0).getStuName() + "?");
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
        //Button Exit
        btnStuUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivStuUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button Delete Date of birth
        imBtnStuUpdateDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStuUpdateDOB.setText("");
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
        edtStuUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(StudentUpdateActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //RadioGroup Gender
        rgStuUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_stu_update_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });


        //Button Save
        btnStuUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtStuUpdateName)) {
                    edtStuUpdateName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtStuUpdateEmail)) {
                    edtStuUpdateEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtStuUpdateEmail)) {
                    studentNo = edtStuUpdateNo.getText().toString();
                    studentName = edtStuUpdateName.getText().toString();
                    studentDOB = edtStuUpdateDOB.getText().toString();
                    studentClass = edtStuUpdateClass.getText().toString();
                    studentPhone = edtStuUpdatePhone.getText().toString();
                    studentEmail = edtStuUpdateEmail.getText().toString();
                    if (studentName.length() > 0 && studentEmail.length() > 0) {
                        //Toast.makeText(StudentUpdateActivity.this, realPath, Toast.LENGTH_SHORT).show();
                        if (!realPath.equals("")) {
                            //Toast.makeText(StudentUpdateActivity.this, "empty", Toast.LENGTH_SHORT).show();
                            uploadInfoWithPhoto();

                            //Log.d("uploadInfoWithPhoto", "1");
                        } else {
                            //Log.d("uploadInfo", "1");
                            uploadInfo();
                        }
                    }
                } else {
                    edtStuUpdateEmail.setError("Email address not valid");
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
        if (!studentArr.get(0).getStuAvatar().equals("")) {
            currentAvatar = studentArr.get(0).getStuAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_STUDENT_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteStudentData(studentArr.get(0).getStuId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("STUDENT_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(StudentUpdateActivity.this, "Deleted Student " + studentArr.get(0).getStuName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentUpdateActivity.this, StudentLoginActivity.class);
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

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (studentArr.get(0).getStuAvatar().equals("")) {
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
                newAvatar = studentArr.get(0).getStuAvatar();
            } else {
                currentAvatar = studentArr.get(0).getStuAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
                newAvatar = APIUtils.BASE_URL + "images/" + studentAvatar;
            }
        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.UpdateStudentData(studentArr.get(0).getStuId(), studentNo, studentName, studentDOB, studentClass, studentPhone, studentEmail, newAvatar, updateGender, currentAvatar);
        studentArr.get(0).setStuNo(studentNo);
        studentArr.get(0).setStuName(studentName);
        studentArr.get(0).setStuDOB(studentDOB);
        studentArr.get(0).setStuClass(studentClass);
        studentArr.get(0).setStuPhone(studentPhone);
        studentArr.get(0).setStuEmail(studentEmail);
        studentArr.get(0).setStuAvatar(newAvatar);
        studentArr.get(0).setStuGender(updateGender);
        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Stu Info", result);
                if (result.trim().equals("STUDENT_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(StudentUpdateActivity.this, "Successfully Updated Student Information " + studentName, Toast.LENGTH_SHORT).show();
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
        Intent intent = getIntent();
        intent.putExtra("STUDENT_DATA_FROM_UPDATE_TO_MENU", studentArr);
        setResult(StudentMenuActivity.RESULT_OK, intent);
        finish();
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

    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtStuUpdateDOB.setText(sdf.format(calendar.getTime()));
    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        studentArr = intent.getParcelableArrayListExtra("STUDENT_DATA_FROM_MENU_TO_UPDATE");
    }

    private void initView() {
        edtStuUpdateName.setText(studentArr.get(0).getStuName());
        edtStuUpdateNo.setText(studentArr.get(0).getStuNo());
        edtStuUpdateDOB.setText(studentArr.get(0).getStuDOB());
        edtStuUpdatePhone.setText(studentArr.get(0).getStuPhone());
        edtStuUpdateEmail.setText(studentArr.get(0).getStuEmail());
        edtStuUpdateClass.setText(studentArr.get(0).getStuClass());

        if (!studentArr.get(0).getStuGender().equals("-1")) {
            if (studentArr.get(0).getStuGender().equals("1")) {
                rbStuUpdateMale.setChecked(true);
                updateGender = "1";
            } else {
                rbStuUpdateFemale.setChecked(true);
                updateGender = "0";
            }
        }

        if (!studentArr.get(0).getStuAvatar().equals("")) {
            Picasso.get()
                    .load(studentArr.get(0).getStuAvatar())
                    .placeholder(R.drawable.graduated)
                    .error(R.drawable.graduated)
                    .into(ivStuUpdateAvatar);
        } else {
            if (!studentArr.get(0).getStuGender().equals("-1")) {
                if (studentArr.get(0).getStuGender().equals("1")) {
                    ivStuUpdateAvatar.setImageResource(R.drawable.male);
                } else {
                    ivStuUpdateAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivStuUpdateAvatar.setImageResource(R.drawable.graduated);
            }
        }

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
                    ivStuUpdateAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStuUpdateAvatar.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivStuUpdateAvatar.getDrawable()).getBitmap();
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
        ivStuUpdateAvatar = findViewById(R.id.iv_stu_update_avt);
        ivStuUpdateExit = findViewById(R.id.iv_stu_update_exit);
        edtStuUpdateName = findViewById(R.id.edt_stu_update_name);
        edtStuUpdateNo = findViewById(R.id.edt_stu_update_no);
        edtStuUpdateDOB = findViewById(R.id.edt_stu_update_dob);
        edtStuUpdatePhone = findViewById(R.id.edt_stu_update_phone);
        edtStuUpdateEmail = findViewById(R.id.edt_stu_update_email);
        edtStuUpdateClass = findViewById(R.id.edt_stu_update_class);
        rgStuUpdateGender = findViewById(R.id.rg_stu_update_gender);
        rbStuUpdateFemale = findViewById(R.id.rb_stu_update_female);
        rbStuUpdateMale = findViewById(R.id.rb_stu_update_male);
        btnStuUpdateSave = findViewById(R.id.btn_stu_update_save);
        btnStuUpdateExit = findViewById(R.id.btn_stu_update_exit);
        btnStuUpdateDelete = findViewById(R.id.btn_stu_update_delete);
        btnStuUpdateTakePhoto = findViewById(R.id.btn_stu_update_take_photo);
        btnStuUpdateChoosePhoto = findViewById(R.id.btn_stu_update_choose_photo);
        imBtnStuUpdateDelDOB = findViewById(R.id.im_btn_stu_update_del_dob);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}