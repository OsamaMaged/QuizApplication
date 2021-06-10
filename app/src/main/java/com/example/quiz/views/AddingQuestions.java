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
import android.widget.Toast;

import com.example.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

public class AddingQuestions extends AppCompatActivity implements View.OnClickListener {
    EditText Quest,T;
    EditText OpA,OpB,OpC,Ans;
    Button addQuestion;
     FirebaseAuth firebaseAuth;
     String Quiz_ID="";
     long QuestionCounter=1L;
     long QuestionNumber=5L;
     String Question;
     String QNumber;
     long Timer;
     String OptionA,OptionB,OptionC,Answer;
     FirebaseFirestore db;
     Map<String,Object> question =new HashMap<   >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_questions);
        Quest= findViewById(R.id.question);
        T= findViewById(R.id.timer);
        OpA= findViewById(R.id.optionA);
        OpB= findViewById(R.id.optionB);
        OpC= findViewById(R.id.optionC);
        Ans= findViewById(R.id.answer);
        addQuestion= findViewById(R.id.addQB);
        addQuestion.setText("Add");
        addQuestion.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();
        Quiz_ID = getIntent().getStringExtra("Quiz");
        QNumber = getIntent().getStringExtra("Q");
        Log.d(TAG,"Number "+QNumber);
       QuestionNumber =Long.parseLong(QNumber);

    }
public void CLEAR()
{
    OpA.getText().clear();
    OpB.getText().clear();
    OpC.getText().clear();
    T.getText().clear();
    Quest.getText().clear();
    Ans.getText().clear();
}
    public void GET()
{
    OptionA = OpA.getText().toString().trim();
    OptionB = OpB.getText().toString().trim();
    OptionC = OpC.getText().toString().trim();
    if(!TextUtils.isEmpty((T.getText().toString().trim())))
    Timer = Long.parseLong(T.getText().toString().trim());
    else{ T.setError("Timer is Required.");return; }
    Question = Quest.getText().toString().trim();
    Answer = Ans.getText().toString().trim();
    if(CHECK())
    { CLEAR();

    }
    else {

        setQuestion();
        CLEAR();
    }
    }

    public boolean CHECK()
    {
        if (TextUtils.isEmpty(Question)) {
            Quest.setError("Question is Required.");
            return true;
        }
        if (TextUtils.isEmpty(valueOf(Timer))) {
            T.setError("Timer is Required.");
            return true;
        }

        if (TextUtils.isEmpty(OptionA)) {
            OpA.setError("OptionA is Required.");
            return true;
        }
        if (TextUtils.isEmpty(OptionB)) {
            OpB.setError("OptionB is Required.");
            return true;
        }
        if (TextUtils.isEmpty(OptionC)) {
            OpC.setError("OptionC is Required.");
            return true;
        }

        if (TextUtils.isEmpty(Answer)) {
            Ans.setError("Answer is Required.");
            return true;
        }
        return false;
    }
    public void setQuestion() {

        question.put("question",Question);
        question.put("option_a",OptionA);
        question.put("option_b",OptionB);
        question.put("option_c",OptionC);
        question.put("answer",Answer);
        question.put("timer",Timer);

        db.collection("QuizList").document(Quiz_ID).collection("Questions").document()
                .set(question).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Question uploaded ", Toast.LENGTH_SHORT).show();
                question.clear();
                QuestionCounter++;
                if(QuestionCounter==(QuestionNumber+1))
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.addQB:
                if(QuestionCounter<=QuestionNumber)
                {   if(QuestionCounter==(QuestionNumber-1))
                {
                    GET();
                    addQuestion.setText("Submit");
                }else {
                    addQuestion.setText("Add");
                    GET();
                }
                }



                break;
        }
    }
}