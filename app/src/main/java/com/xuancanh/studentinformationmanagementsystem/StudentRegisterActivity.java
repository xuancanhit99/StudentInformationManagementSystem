package com.xuancanh.studentinformationmanagementsystem;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuancanh.studentinformationmanagementsystem.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRegisterActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String StudentName, StudentEmail, StudentPassword;

    private EditText edtStuRegisterName, edtStuRegisterEmail, edtStuRegisterPassword;
    private Button btnStuRegister, btnStuRegisterTakePhoto, btnStuRegisterChoosePhoto;
    private TextView tvStuRegisterToLogin;
    private ImageView ivStuRegisterClose, ivStuRegisterAvt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        //Anh xa
        initUI();

        // Close
        ivStuRegisterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Choose Photo
        btnStuRegisterChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnStuRegisterTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // To Login
        tvStuRegisterToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRegisterActivity.this, StudentLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Button Register
        btnStuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentName = edtStuRegisterName.getText().toString();
                StudentEmail = edtStuRegisterEmail.getText().toString();
                StudentPassword = edtStuRegisterPassword.getText().toString();

                if(StudentName.length() > 0 && StudentEmail.length() > 0 && StudentPassword.length() > 0) {
                    //Cre file from real path photo
                    File file = new File(realPath);
                    //Get file path to String
                    String file_path = file.getAbsolutePath();
                    //Log.d("Error", file_path);

                    //Split name photo(Avoid the same name)
                    String[] arrayNamePhoto = file_path.split("\\.");
                    file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
                    //Log.d("Error", file_path);

                    //Confirm the file's data type - multipart/form-data
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //key - name(in file php: $file_path = $file_path.basename($_FILES['upload_file']['name']);)
                    MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
                    //Call retrofit: cre connect and return data
                    DataClient dataClient = APIUtils.getData();
                    // Take data from call
                    retrofit2.Call<String> callback = dataClient.UploadPhoto(body);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response != null) {
                                String message = response.body();
                                //Log.d("Error", message);
                                if(message.length() > 0) {
                                    DataClient insertData = APIUtils.getData();
                                    Call<String> callback = insertData.InsertStudentData(StudentName, StudentEmail, StudentPassword, APIUtils.BASE_URL + "image/" + message);
                                    //Upload
                                    callback.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String result = response.body();
                                            Log.d("Retrofit response", result);
                                            if(result.trim().equals("Success")) {
                                                Toast.makeText(StudentRegisterActivity.this, "Insert Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(StudentRegisterActivity.this, StudentLoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("Error Retrofit response", t.getMessage());
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(StudentRegisterActivity.this, "Enter your email, name and password in the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable)ivStuRegisterAvt.getDrawable()).getBitmap();
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
            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    private void initUI() {
        edtStuRegisterName = (EditText)findViewById(R.id.edt_stu_register_name);
        edtStuRegisterEmail = (EditText)findViewById(R.id.edt_stu_register_email);
        edtStuRegisterPassword = (EditText)findViewById(R.id.edt_stu_register_password);
        btnStuRegister =(Button)findViewById(R.id.btn_stu_register);
        btnStuRegisterTakePhoto =(Button)findViewById(R.id.btn_stu_register_take_photo);
        btnStuRegisterChoosePhoto =(Button)findViewById(R.id.btn_stu_register_choose_photo);
        tvStuRegisterToLogin =(TextView) findViewById(R.id.tv_stu_register_to_login);
        ivStuRegisterAvt = (ImageView)findViewById(R.id.iv_stu_register_avt);
        ivStuRegisterClose = (ImageView)findViewById(R.id.iv_stu_register_close);
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
                    ivStuRegisterAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStuRegisterAvt.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }
    }

    // Get Real Path when upload photo(from uri - image/mame_image)
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}