package com.example.quiz.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz.R;
import com.example.quiz.model.QuestionModel;
import com.example.quiz.model.QuizListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DeletingQuiz extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    String QuizN;
    Button rem;
    FirebaseFirestore db;
   private List<QuestionModel> allQuestionsList = new ArrayList<>();
    CollectionReference itemsRef ;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleting_quiz);
        name=findViewById(R.id.QuizName);
        rem=findViewById(R.id.Delete);
        QuizN = name.getText().toString();
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("QuizList");
        rem.setOnClickListener(this);;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Delete:
                QuizN = name.getText().toString();
                if(TextUtils.isEmpty(QuizN))
                {
                    name.setError("Quiz Name is Required");
                    return;
                }
                else
                    {
                        query = itemsRef.whereEqualTo("name",QuizN);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        {
                                            Toast.makeText(getApplicationContext(),"Quiz Deleted",Toast.LENGTH_SHORT);
                                        itemsRef.document(document.getId()).delete();
                                        startActivity(new Intent(getApplicationContext(),AdminOptionsActivity.class));
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                }
                break;

    }
    }
}