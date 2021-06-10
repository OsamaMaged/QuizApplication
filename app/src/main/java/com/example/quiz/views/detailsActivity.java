package com.example.quiz.views;



import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quiz.model.QuizListModel;
import com.example.quiz.viewmodel.QuizListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import com.example.quiz.R;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

public class detailsActivity extends AppCompatActivity implements View.OnClickListener {
    private QuizListViewModel quizListViewModel;
    int position=0;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
Application app;
    private TextView detailsTitle;
    private TextView detailsQuestions;
    private TextView detailsScore;
    private Button detailsStartBtn;
    private String quizId;
    private long totalQuestions = 0;
    private String quizName;
    public detailsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
      //  position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        Log.d(TAG, "Quiz  " +position);
        detailsTitle = findViewById(R.id.details_title);
        detailsQuestions =findViewById(R.id.details_questions_text);
        detailsScore =findViewById(R.id.details_score_text);

        detailsStartBtn =findViewById(R.id.details_start_btn);
        detailsStartBtn.setOnClickListener(this);
                //Load Previous Results
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        quizListViewModel =  ViewModelProviders.of(this).get(QuizListViewModel.class);
        quizListViewModel.getQuizListModelData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {



                detailsTitle.setText(quizListModels.get(position).getName());
                detailsQuestions.setText(quizListModels.get(position).getQuestions() + "");

                quizId = quizListModels.get(position).getQuiz_id();
                Log.d(TAG, "Details Quiz id  " +quizId);
                quizName = quizListModels.get(position).getName();
                totalQuestions  = quizListModels.get(position).getQuestions();

                //Load Results Data
                loadResultsData();

            }
        });

    }
    private void loadResultsData() {
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()){
                        //Get Result
                        Long correct = document.getLong("correct");
                        Long wrong = document.getLong("wrong");
                        Long missed = document.getLong("unanswered");

                        //Calculate Progress
                        Long total = correct + wrong + missed;
                        Long percent = (correct*100)/total;

                        detailsScore.setText(percent + "%");
                    } else {
                        //Document Doesn't Exist, and result should stay N/A
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_start_btn:
                Intent intent = new Intent(detailsActivity.this,QuizActivity.class);
                intent.putExtra("QuizID",quizId);
                intent.putExtra("Name",quizName);
                intent.putExtra("Questions",valueOf(totalQuestions));
                startActivity(intent);
                break;
        }
    }
}