package com.example.quiz.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quiz.R;

public class AdminOptionsActivity extends AppCompatActivity implements View.OnClickListener {
    Button adding,removing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        adding = findViewById(R.id.add);
        removing =findViewById(R.id.remove);
        adding.setOnClickListener(this);
        removing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                startActivity(new Intent(getApplicationContext(),AddingQuiz.class));
                break;

            case R.id.remove:
                startActivity(new Intent(getApplicationContext(),DeletingQuiz.class));
                break;

        }
    }
}