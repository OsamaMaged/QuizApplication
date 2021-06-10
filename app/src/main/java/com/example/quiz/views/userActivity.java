package com.example.quiz.views;


import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.quiz.viewmodel.authViewModel;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class userActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button log;
    EditText Email,Password;
    public authViewModel authviewModel;


    /* ------------------Email : user@eng.asu.edu.eg------------------------------
       -----------------Paswword: user123
     */
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        log = findViewById(R.id.loginBtn);
        fAuth = FirebaseAuth.getInstance();
      authviewModel =  ViewModelProviders.of(this).get(authViewModel.class);
        log.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

             String    email = Email.getText().toString().trim();
             String    password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    Password.setError("Password Must be >= 6 Characters");
                    return;
                }

            progressBar.setVisibility(View.VISIBLE);
            authviewModel.login( email, password,"user");
            Log.d(TAG, "Calling ViewModel");
            progressBar.setVisibility(View.GONE);


            }
        });
    }
}


