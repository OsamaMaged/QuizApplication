package com.example.quiz.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.example.quiz.model.AppRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.quiz.viewmodel.authViewModel;

import java.util.Observer;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

public class adminActivity extends AppCompatActivity {
    //FirebaseAuth fAuth;
    Button login;
    EditText Email,Password;
    private  authViewModel authviewModel;
    /* ------------------Email : admin@eng.asu.edu.eg------------------------------
         -----------------Paswword: admin123
   */
    ProgressBar progressBar;
    String email;
    String password;
    AppRepository.OnFirestoreTaskComplete onFirestoreTaskComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        login = findViewById(R.id.loginBtn);
       // fAuth = FirebaseAuth.getInstance();
        authviewModel =  ViewModelProviders.of(this).get(authViewModel.class);
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                 email = Email.getText().toString().trim();
                 password = Password.getText().toString().trim();

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

                // authenticate the user
                authviewModel.login(email, password,"admin");
                Log.d(TAG, "Calling ViewModel");
                progressBar.setVisibility(View.GONE);
                   }


        });
    }
}


