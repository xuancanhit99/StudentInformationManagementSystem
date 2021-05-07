package com.xuancanh.studentinformationmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.model.Admin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AdminUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;

    EditText edtAdminUpdateEmail, edtAdminUpdateName, edtAdminUpdateCurrentPassword, edtAdminUpdateNewPassword, edtAdminUpdateRetypeNewPassword;
    Button btnAdminUpdateTakePhoto, btnAdminUpdateChoosePhoto, btnAdminUpdateSave, btnAdminUpdateDelete, btnAdminUpdateExit;
    ImageView ivAdminUpdateAvatar;

    ArrayList<Admin> adminArr;
    String adminEmail, adminName, currentPassword, newPassword, retypeNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        initUI();

        initView();

        //Button Save
        btnAdminUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminName = edtAdminUpdateName.getText().toString();
                adminEmail = edtAdminUpdateEmail.getText().toString();
                currentPassword = edtAdminUpdateCurrentPassword.getText().toString();
                newPassword = edtAdminUpdateNewPassword.getText().toString();
                retypeNewPassword = edtAdminUpdateRetypeNewPassword.getText().toString();
                if(adminName.length() > 0 && adminEmail.length() > 0){
                    if(currentPassword.length() > 0 && newPassword.length() > 0 && retypeNewPassword.length() > 0 ) {
                        if(currentPassword.equals(adminArr.get(0).getAdPassword()) && newPassword.equals(retypeNewPassword)) {
                            Toast.makeText(AdminUpdateActivity.this, "Co the doi mk", Toast.LENGTH_SHORT).show();
                        }
                        else if(currentPassword.equals(adminArr.get(0).getAdPassword()) && !newPassword.equals(retypeNewPassword)) {
                            Toast.makeText(AdminUpdateActivity.this, "Nhap mat khau moi khong giong nhau", Toast.LENGTH_SHORT).show();
                        }
                        else if(!currentPassword.equals(adminArr.get(0).getAdPassword()) && newPassword.equals(retypeNewPassword)) {
                            Toast.makeText(AdminUpdateActivity.this, "Mat khau cu sai", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AdminUpdateActivity.this, "Vui long nhap mat khau hoac de trong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //
                    Toast.makeText(AdminUpdateActivity.this, "Chi luu ten, email va avatar", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AdminUpdateActivity.this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button Exit
        btnAdminUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUpdateActivity.this, HomeMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Button Choose Photo
        btnAdminUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdminUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }

    private void initView() {
        Intent intent = getIntent();
        adminArr = intent.getParcelableArrayListExtra("ADMIN_DATA_TO_UPDATE");
        edtAdminUpdateName.setText(adminArr.get(0).getAdName());
        edtAdminUpdateEmail.setText(adminArr.get(0).getAdEmail());
        Picasso.get()
                .load(adminArr.get(0).getAdAvatar())
                .placeholder(R.drawable.admin)
                .error(R.drawable.admin)
                .into(ivAdminUpdateAvatar);
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
                    ivAdminUpdateAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdminUpdateAvatar.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable)ivAdminUpdateAvatar.getDrawable()).getBitmap();
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
        edtAdminUpdateEmail = findViewById(R.id.edt_admin_update_email);
        edtAdminUpdateName = findViewById(R.id.edt_admin_update_name);
        edtAdminUpdateCurrentPassword = findViewById(R.id.edt_admin_update_current_password);
        edtAdminUpdateNewPassword = findViewById(R.id.edt_admin_update_new_password);
        edtAdminUpdateRetypeNewPassword = findViewById(R.id.edt_admin_update_retype_new_password);
        btnAdminUpdateChoosePhoto = findViewById(R.id.btn_admin_update_choose_photo);
        btnAdminUpdateTakePhoto = findViewById(R.id.btn_admin_update_take_photo);
        btnAdminUpdateSave = findViewById(R.id.btn_admin_update_save);
        btnAdminUpdateDelete = findViewById(R.id.btn_admin_update_delete);
        btnAdminUpdateExit = findViewById(R.id.btn_admin_update_exit);
        ivAdminUpdateAvatar = findViewById(R.id.iv_admin_update_avt);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminUpdateActivity.this, HomeMenuActivity.class);
        startActivity(intent);
        finish();
    }
}