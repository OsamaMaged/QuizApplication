package com.example.quiz.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.quiz.views.AddingQuiz;
import com.example.quiz.views.AdminOptionsActivity;
import com.example.quiz.views.ListActivity;
import com.example.quiz.views.MainActivity;
import com.example.quiz.views.adminActivity;
import com.example.quiz.views.userActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.content.ContentValues.TAG;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AppRepository {
    private Application application;
    private FirebaseAuth fAuth;
    private int success = -1;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query quizRef = firebaseFirestore.collection("QuizList").whereEqualTo("visibility", "public");
    DocumentReference users ;
    DocumentReference admins ;
    Query UserQuery;
    Query AdminQuery;
    public AppRepository(Application application) {
        this.application = application;
        this.fAuth=FirebaseAuth.getInstance();
        this.userMutableLiveData =new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        if (fAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(fAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }
    public AppRepository( Application application, OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;

    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public int isSuccess() {
        return success;
    }

    public void login(  String email, String password, String UI) {
        users = firebaseFirestore.collection("users").document(email);
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(fAuth.getCurrentUser());
                            if(UI.equals("admin"))
                            {
                                admins = firebaseFirestore.collection("admins").document(email);
                                admins.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.getString("isAdmin")!=null) {
                                            Toast.makeText(application.getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                            application.startActivity(new Intent(application.getApplicationContext(), AdminOptionsActivity.class));
                                        }else
                                            Toast.makeText(application.getApplicationContext(), "Not an Admin account", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(application.getApplicationContext(), "This Admin account is not added to system", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                            else if (UI.equals("user"))
                            {
                                users = firebaseFirestore.collection("users").document(email);
                                users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.getString("isUser")!=null) {
                                            Toast.makeText(application.getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                            application.startActivity(new Intent(application.getApplicationContext(), ListActivity.class));
                                        }else
                                            Toast.makeText(application.getApplicationContext(), "Not an User account", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(application.getApplicationContext(), "This User account is not added to system", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            else
                                {
                           Toast.makeText(application.getApplicationContext(), "Not admin or user!!", Toast.LENGTH_SHORT).show();
                                }
                        } else {

                            Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           // application.startActivity(new Intent(application.getApplicationContext(),MainActivity.class));
                        }
                    }
                });
    }
    public void logOut() {
        fAuth.signOut();
        loggedOutLiveData.postValue(true);
    }
    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(List<QuizListModel> quizListModelsList);
        void onError(Exception e);
    }
    public void getQuizData() {
        quizRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(QuizListModel.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }
}
