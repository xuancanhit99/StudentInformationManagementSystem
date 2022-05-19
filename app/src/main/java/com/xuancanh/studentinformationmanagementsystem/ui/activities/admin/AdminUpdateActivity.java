package com.xuancanh.studentinformationmanagementsystem.ui.activities.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Admin;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUpdateActivity extends AppCompatActivity {
    public static final int ADMIN_CHANGE_PASSWORD_ACTIVITY = 2;
    public static final int RESULT_CHANGE_PASSWORD_OK = 3;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;

    EditText edtAdminUpdateEmail, edtAdminUpdateName;
    Button btnAdminUpdateTakePhoto, btnAdminUpdateChoosePhoto, btnAdminUpdateSave, btnAdminUpdateDelete, btnAdminUpdateExit, btnAdminChangePassword;
    ImageView ivAdminUpdateAvatar, ivAdminUpdateExit;

    ArrayList<Admin> adminArr;
    String adminEmail, adminName, adminAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        //Connect layout
        initUI();
        //Set on Views
        initView();

        //Button Delete
        btnAdminUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this admin account");
                builder.setMessage("Are you sure want to delete account admin " + adminArr.get(0).getAdName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccAdmin();
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

        //Button Change Password
        btnAdminChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUpdateActivity.this, AdminChangePasswordActivity.class);
                intent.putExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", adminArr);
                startActivityForResult(intent, ADMIN_CHANGE_PASSWORD_ACTIVITY);
            }
        });

        //Button Save
        btnAdminUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdminUpdateName)) {
                    edtAdminUpdateName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtAdminUpdateEmail)) {
                    edtAdminUpdateEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtAdminUpdateEmail)) {
                    adminName = edtAdminUpdateName.getText().toString();
                    adminEmail = edtAdminUpdateEmail.getText().toString();
                    if (adminName.length() > 0 && adminEmail.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                }
                else {
                    edtAdminUpdateEmail.setError("Email address not valid");
                }

            }
        });

        //Button Exit
        btnAdminUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdminUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
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

    private void deleteAccAdmin() {
        String currentAvatar;
        if (!adminArr.get(0).getAdAvatar().equals("")) {
            currentAvatar = adminArr.get(0).getAdAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_ADMIN_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteAdminData(adminArr.get(0).getAdId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("ADMIN_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminUpdateActivity.this, "Deleted Admin " + adminArr.get(0).getAdName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminUpdateActivity.this, AdminLoginActivity.class);
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

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callbackPhoto = dataClient.UploadAdminPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    adminAvatar = response.body();
                    Log.d("Updated Ad Photo", adminAvatar);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Ad Photo", t.getMessage());
            }
        });

    }

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (adminArr.get(0).getAdAvatar().equals("")) {
            currentAvatar = "NO_DELETE_CURRENT_IMAGE";
            if (realPath.equals("")) {
                newAvatar = "";
            } else {
                newAvatar = APIUtils.BASE_URL + "admin/images/" + adminAvatar;
            }
        } else {

            if (realPath.equals("")) {
                currentAvatar = "NO_DELETE_CURRENT_IMAGE";
                newAvatar = adminArr.get(0).getAdAvatar();
            } else {
                currentAvatar = adminArr.get(0).getAdAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
                newAvatar = APIUtils.BASE_URL + "admin/images/" + adminAvatar;
            }
        }

//        if (realPath.equals("")) {
//            newAvatar = "";
//        } else {
//            newAvatar = APIUtils.BASE_URL + "admin/images/" + adminAvatar;
//        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.UpdateAdminData(adminArr.get(0).getAdId(), adminEmail, adminName, newAvatar, currentAvatar);
        adminArr.get(0).setAdEmail(adminEmail);
        adminArr.get(0).setAdName(adminName);
        adminArr.get(0).setAdAvatar(newAvatar);
        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Ad Info", result);
                if (result.trim().equals("ADMIN_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(AdminUpdateActivity.this, "Successfully Updated Admin Information", Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Ad Info", t.getMessage());
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        adminArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE");
        edtAdminUpdateName.setText(adminArr.get(0).getAdName());
        edtAdminUpdateEmail.setText(adminArr.get(0).getAdEmail());
        if (!adminArr.get(0).getAdAvatar().equals("")) {
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdminUpdateAvatar);
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

        //Change password
        else if (resultCode == RESULT_CHANGE_PASSWORD_OK) {
            if (requestCode == ADMIN_CHANGE_PASSWORD_ACTIVITY) {
                adminArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_CHANGE_PASSWORD_TO_UPDATE");
            }
        }
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivAdminUpdateAvatar.getDrawable()).getBitmap();
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
        edtAdminUpdateEmail = findViewById(R.id.edt_admin_update_email);
        edtAdminUpdateName = findViewById(R.id.edt_admin_update_name);
        btnAdminUpdateChoosePhoto = findViewById(R.id.btn_admin_update_choose_photo);
        btnAdminUpdateTakePhoto = findViewById(R.id.btn_admin_update_take_photo);
        btnAdminUpdateSave = findViewById(R.id.btn_admin_update_save);
        btnAdminUpdateDelete = findViewById(R.id.btn_admin_update_delete);
        btnAdminUpdateExit = findViewById(R.id.btn_admin_update_exit);
        btnAdminChangePassword = findViewById(R.id.btn_admin_update_change_password);
        ivAdminUpdateAvatar = findViewById(R.id.iv_admin_update_avt);
        ivAdminUpdateExit = findViewById(R.id.iv_admin_update_exit);
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

    @Override
    public void onBackPressed() {
        backToMenu();
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU", adminArr);
        setResult(AdminMenuActivity.RESULT_OK, intent);
        finish();
    }

}