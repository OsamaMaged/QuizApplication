package com.example.quiz.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    View view;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        binding.user.setOnClickListener(mylistner);
        binding.admin.setOnClickListener(mylistner);
        fAuth = FirebaseAuth.getInstance();

        userActivity user;
    }

    View.OnClickListener mylistner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()== R.id.user){
               // Intent intent = new Intent(getApplicationContext(), adminActivity.class);

                startActivity(new Intent(getApplicationContext(),userActivity.class));
            }
            if (v.getId()==R.id.admin){
                startActivity(new Intent(getApplicationContext(),adminActivity.class));
            }
        }
    };}
