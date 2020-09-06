package com.example.coffeegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeegroup.Services.ConstData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        ids();
    }

    private void ids()
    {
        etEmail = findViewById(R.id.etEmail);
        etPassword =findViewById(R.id.etPassword);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        Button buttonSignIn = findViewById(R.id.btnSignIn);

        tvForgotPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvForgotPassword:
                String temp=etEmail.getText().toString().trim();
                if(TextUtils.isEmpty(temp))
                    etEmail.setError("Required");
                else
                    forgotPassword(temp);
                break;

            case R.id.tvSignUp:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;

            case R.id.btnSignIn:
                String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                    etEmail.setError("Required");
                else if (TextUtils.isEmpty(password))
                    etPassword.setError("Required");
                else
                    signIn(email,password);
                break;
        }
    }

    private void forgotPassword(String email) {
        progressDialog.setMessage("Sending recovery mail...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Check Email for RESET link.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Error Resetting password"+task.getResult(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(String email, String password) {
        progressDialog.setMessage("Signing In...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    SharedPreferences.Editor editor = getSharedPreferences(ConstData.sharedPrefName,MODE_PRIVATE).edit();
                    editor.putBoolean(ConstData.loginStatus,true);
                    editor.putString(ConstData.uid,FirebaseAuth.getInstance().getUid());
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), CoffeeList.class));
                    finish();
                }
                else
                    Toast.makeText(LoginActivity.this, "Login Failed "+task.getResult(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}