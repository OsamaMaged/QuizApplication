package com.example.quiz.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.R;
import com.example.quiz.model.QuizListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.util.Log.d;
import static java.lang.String.valueOf;

public class AddingQuiz extends AppCompatActivity implements View.OnClickListener {
    EditText Name;
    EditText QuestionsNumber;
    CheckBox pub;
    Button add;
    String QuizName,QuizVisibilty;
    long QuestionsN;
    String quiz_id;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_quiz);
        Name= findViewById(R.id.name);
        QuizVisibilty ="not public";
        QuestionsNumber =findViewById(R.id.questions);
        pub =(CheckBox) findViewById(R.id.checkBox);
        add= findViewById(R.id.button);
        pub.setOnClickListener(this);
        add.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.checkBox:
                if (pub.isChecked())
                    QuizVisibilty ="public";
                else QuizVisibilty ="not public";
                break;
            case R.id.button:
                QuizName = Name.getText().toString().trim();
                QuestionsN = Long.parseLong(QuestionsNumber.getText().toString().trim());
                if (TextUtils.isEmpty(QuizName)) {
                    Name.setError("Enter the Quiz Name.");
                    return;
                }else if(TextUtils.isEmpty(valueOf(QuestionsN)))
                {
                    QuestionsNumber.setError("Enter The Questions Number.");
                    return;
                }
                else
                {   Date dNow = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
                     quiz_id = (QuizName+ft.format(dNow)).trim();
                  //  QuizListModel quiz =new QuizListModel(quiz_id, QuizName, null, QuizVisibilty,  QuestionsN);
                    setQuiz(quiz_id,QuizName,QuestionsN,QuizVisibilty);
                }


            break;
        }
    }
    public void setQuiz(String quiz_id,String QuizName,long QuestionsN,String QuizVisibilty) {

        Map<String,Object> quiz =new HashMap<>();
        Map<String,Object> results =new HashMap<>();
        quiz.put("name",QuizName);
        quiz.put("questions",(long)QuestionsN);
        quiz.put("visibility",QuizVisibilty);
        results.put("correct",0);
        results.put("unanswered",0);
        results.put("wrong",0);
        db.collection("QuizList").document(quiz_id).set(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Quiz uploaded ", Toast.LENGTH_SHORT).show();
                db.collection("QuizList").document(quiz_id).collection("Results").document()
                        .set(results).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                              Log.d(TAG, "Results Added");

                                                                }
                                                            });
                         Intent intent = new Intent(AddingQuiz.this,AddingQuestions.class);
                         intent.putExtra("Q",valueOf(QuestionsN));
                         intent.putExtra("Quiz",quiz_id);
                         startActivity(intent);
                        //startActivity(new Intent(getApplicationContext(), AddingQuestions.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload ", Toast.LENGTH_SHORT).show();

            }
        });

    }
}