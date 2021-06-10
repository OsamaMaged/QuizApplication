package com.example.quiz.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.quiz.R;

public class ResultActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    private String quizId;

    private TextView resultCorrect;
    private TextView resultWrong;
    private TextView resultMissed;

    private TextView resultPercent;
    private ProgressBar resultProgress;

    private Button resultHomeBtn;
    public ResultActivity() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        firebaseAuth = FirebaseAuth.getInstance();

        //Get User ID
        if(firebaseAuth.getCurrentUser() != null){
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        } else {
            //Go Back to Home Page
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        quizId = getIntent().getStringExtra("QuizID");

        //Initialize UI Elements
        resultCorrect = findViewById(R.id.results_correct_text);
        resultWrong = findViewById(R.id.results_wrong_text);
        resultMissed = findViewById(R.id.results_missed_text);

        resultHomeBtn = findViewById(R.id.results_home_btn);
        resultHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ListActivity.class));
            }
        });

        resultPercent = findViewById(R.id.results_percent);
        resultProgress = findViewById(R.id.results_progress);

        //Get Results
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();

                    Long correct = result.getLong("correct");
                    Long wrong = result.getLong("wrong");
                    Long missed = result.getLong("unanswered");

                    resultCorrect.setText(correct.toString());
                    resultWrong.setText(wrong.toString());
                    resultMissed.setText(missed.toString());

                    //Calculate Progress
                    Long total = correct + wrong + missed;
                    Long percent = (correct*100)/total;

                    resultPercent.setText(percent + "%");
                    resultProgress.setProgress(percent.intValue());
                }
            }
        });
    }
}
