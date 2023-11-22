package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivityAddNewAdminBinding;
import com.example.foodorderapp.model.User;
import com.example.foodorderapp.utils.StringUtil;

public class AddNewAdminActivity extends BaseActivity{
    private ActivityAddNewAdminBinding mActivityAddNewAdminBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddNewAdminBinding = mActivityAddNewAdminBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddNewAdminBinding.getRoot());

        mActivityAddNewAdminBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivityAddNewAdminBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        String strEmail = mActivityAddNewAdminBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivityAddNewAdminBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
            } else {
                signUpAdmin(strEmail, strPassword);
            }
        }
    }
    private void signUpAdmin(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            userObject.setAdmin(true);
                            Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_sign_up_success), Toast.LENGTH_SHORT).show();
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(AddNewAdminActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
