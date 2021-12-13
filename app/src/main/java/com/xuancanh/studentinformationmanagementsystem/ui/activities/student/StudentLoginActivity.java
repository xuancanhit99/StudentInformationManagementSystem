package com.xuancanh.studentinformationmanagementsystem.ui.activities.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.xuancanh.studentinformationmanagementsystem.R;
import com.xuancanh.studentinformationmanagementsystem.presentation.model.Student;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanh.studentinformationmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanh.studentinformationmanagementsystem.ui.activities.admin.AdminLoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText edtStuLoginEmail, edtStuLoginPassword;
    private Button btnStuLogin;
    private TextView tvStuLoginForgotPassword, tvStuLoginToLoginAdmin, tvStuLoginToRegister;
    private ImageView ivStuLoginClose;
    private CheckBox cbStudentLoginRememberMe;
    private SharedPreferences.Editor loginPrefsEditor;

    ArrayList<Student> studentArr;
    String studentName = "", studentEmail = "", studentIdGoogleIsPassword = "";
    Uri personPhoto = null;


    final int RC_SIGN_IN = 0;
    SignInButton btnStuGoogleSignIn;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //Connect layout
        initUI();


        //Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnStuGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });


        //Login When Enter - Done
        edtStuLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    studentLogin();
                    rememberMe();
                }
                return false;
            }
        });

        //Remember Me
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean rememberMeCheck = loginPreferences.getBoolean("STUDENT_REMEMBER_ME", false);
        if (rememberMeCheck) {
            edtStuLoginEmail.setText(loginPreferences.getString("STUDENT_EMAIL", ""));
            edtStuLoginPassword.setText(loginPreferences.getString("STUDENT_PASSWORD", ""));
            cbStudentLoginRememberMe.setChecked(true);
        }

        // Close
        ivStuLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To Login Admin
        tvStuLoginToLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //To Register
        tvStuLoginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Button Login
        btnStuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid(edtStuLoginEmail)) {
                    studentLogin();
                    rememberMe();
                } else {
                    edtStuLoginEmail.setError("Email address not valid");
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

    private void studentLogin() {
        studentEmail = edtStuLoginEmail.getText().toString();
        studentIdGoogleIsPassword = edtStuLoginPassword.getText().toString();
        if (studentEmail.length() > 0 && studentIdGoogleIsPassword.length() > 0) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<List<Student>> callback = dataClient.LoginStudentData(studentEmail, studentIdGoogleIsPassword);
            callback.enqueue(new Callback<List<Student>>() {
                @Override
                public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                    studentArr = (ArrayList<Student>) response.body();
                    if (studentArr.size() > 0) {
                        //Send Data and finish
                        Intent intent = new Intent(StudentLoginActivity.this, StudentMenuActivity.class);
                        intent.putExtra("STUDENT_DATA_FROM_LOGIN_TO_MENU", studentArr);
                        startActivity(intent);
                        finish();
                        Toast.makeText(StudentLoginActivity.this, "Welcome " + studentArr.get(0).getStuName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Student>> call, Throwable t) {
                    Toast.makeText(StudentLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    //Log.d("Error", t.getMessage());
                }
            });
        }
    }

    private void studentLoginWithGoogleAcc() {
        if (studentEmail.length() > 0) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<List<Student>> callback = dataClient.LoginStudentGoogleAccData(studentEmail);
            callback.enqueue(new Callback<List<Student>>() {
                @Override
                public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                    studentArr = (ArrayList<Student>) response.body();
                    if (studentArr.size() > 0) {
                        //Send Data and finish
                        Intent intent = new Intent(StudentLoginActivity.this, StudentMenuActivity.class);
                        intent.putExtra("STUDENT_DATA_FROM_LOGIN_TO_MENU", studentArr);
                        startActivity(intent);
                        finish();
                        Toast.makeText(StudentLoginActivity.this, "Welcome " + studentArr.get(0).getStuName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Student>> call, Throwable t) {
                    Toast.makeText(StudentLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    //Log.d("Error", t.getMessage());
                }
            });
        }
    }


    private void uploadInfo() {
        DataClient insertData = APIUtils.getData();
        Call<String> callback;
        if (!String.valueOf(personPhoto).equals("")) {
            callback = insertData.InsertStudentData(studentName, studentEmail, studentIdGoogleIsPassword, String.valueOf(personPhoto));
        } else {
            callback = insertData.InsertStudentData(studentName, studentEmail, studentIdGoogleIsPassword, "NO_IMAGE_STUDENT_REGISTER");
        }
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Retrofit response", result);
                if (result.trim().equals("STUDENT_INSERT_SUCCESSFUL")) {
                    Toast.makeText(StudentLoginActivity.this, "Registered in with Google account successfully.", Toast.LENGTH_LONG).show();
                } else if(result.trim().equals("STUDENT_EMAIL_ALREADY_EXISTS")) {
                    Toast.makeText(StudentLoginActivity.this, "Signed with Google account successfully.", Toast.LENGTH_LONG).show();
                }
                studentLoginWithGoogleAcc();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Stu Info", t.getMessage());
            }
        });

    }



    //Create new Student from Google acc
    private void createAccStudentFromGoogleAcc(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            studentName = acct.getDisplayName();
            studentEmail = acct.getEmail();
            studentIdGoogleIsPassword = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }
        uploadInfo();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            createAccStudentFromGoogleAcc();
            Toast.makeText(StudentLoginActivity.this, idToken, Toast.LENGTH_SHORT).show();
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Err", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void rememberMe() {
        if (cbStudentLoginRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("STUDENT_REMEMBER_ME", true);
            loginPrefsEditor.putString("STUDENT_EMAIL", studentEmail);
            loginPrefsEditor.putString("STUDENT_PASSWORD", studentIdGoogleIsPassword);
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.apply();
    }

    private void initUI() {
        edtStuLoginEmail = findViewById(R.id.edt_stu_login_email);
        edtStuLoginPassword = findViewById(R.id.edt_stu_login_password);
        btnStuLogin = findViewById(R.id.btn_stu_login);
        btnStuGoogleSignIn = findViewById(R.id.sign_in_button);
        tvStuLoginForgotPassword = findViewById(R.id.tv_stu_login_forgot_password);
        tvStuLoginToLoginAdmin = findViewById(R.id.tv_stu_login_to_login_admin);
        tvStuLoginToRegister = findViewById(R.id.tv_stu_login_to_register);
        ivStuLoginClose = findViewById(R.id.iv_stu_login_close);
        cbStudentLoginRememberMe = findViewById(R.id.cb_stu_login_remember_me);
    }
}