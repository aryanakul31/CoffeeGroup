package com.example.coffeegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.coffeegroup.Services.ConstData;
import com.example.coffeegroup.Services.UserFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etName, etEmail, etPassword, etConfirmPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);

        ids();
    }

    private void ids() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button buttonSignUp = findViewById(R.id.btnSignUp);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        buttonSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSignUp:
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(name))
                    etName.setError("Required");
                else if (TextUtils.isEmpty(email))
                    etEmail.setError("Required");
                else if (TextUtils.isEmpty(password))
                    etPassword.setError("Required");
                else if (TextUtils.isEmpty(confirmPassword))
                    etConfirmPassword.setError("Required");
                else if (!password.equals(confirmPassword))
                    Toast.makeText(this, "Passwords do not match..", Toast.LENGTH_SHORT).show();
                else
                    signUp(name, email, password);
                break;
            case R.id.tvSignIn:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void signUp(final String name, final String email, final String password)
    {
        progressDialog.setMessage("Signing Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseUser currentUser=firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    final String uid = currentUser.getUid();
                    UserFormat user = new UserFormat(name,0,0,0);
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(ConstData.firebaseDBRef);
                    dbReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(ConstData.sharedPrefName,MODE_PRIVATE).edit();
                                editor.putBoolean(ConstData.loginStatus,true);
                                editor.putString(ConstData.uid,uid);
                                editor.apply();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getApplicationContext(), CoffeeList.class));
                                        finish();
                                    }
                                },2000);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}