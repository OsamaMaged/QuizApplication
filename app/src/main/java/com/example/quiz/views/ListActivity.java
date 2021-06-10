package com.example.quiz.views;

import androidx.appcompat.app.AppCompatActivity;
import  com.example.quiz.R;
import com.example.quiz.model.QuizListModel;
import com.example.quiz.viewmodel.QuizListViewModel;
import com.example.quiz.viewmodel.authViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

public class ListActivity extends AppCompatActivity implements QuizListAdapter.OnQuizListItemClicked {
    private RecyclerView listView;
    private QuizListViewModel quizListViewModel;
    private ProgressBar listProgress;
    private int position;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;
   // private detailsActivity dA =new detailsActivity();
    private QuizListAdapter adapter;
    public ListActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.d(TAG, "Activity List Layout");

        listView = findViewById(R.id.list_view);
        listView = findViewById(R.id.list_view);
        adapter = new QuizListAdapter(this);
        Log.d(TAG, "Adapter declaration");
        listProgress = findViewById(R.id.list_progress);
        fadeInAnim = AnimationUtils.loadAnimation(getApplication().getApplicationContext(), R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getApplication().getApplicationContext(), R.anim.fade_out);
        listView.setLayoutManager(new LinearLayoutManager(getApplication().getApplicationContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);
        Log.d(TAG, "Adapter set");

        quizListViewModel = ViewModelProviders.of(this).get(QuizListViewModel.class);
        Log.d(TAG, "Setting Provider");

        quizListViewModel.getQuizListModelData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                //Load RecyclerView
                listView.startAnimation(fadeInAnim);
                listProgress.startAnimation(fadeOutAnim);
                adapter.setQuizListModels(quizListModels);
                adapter.notifyDataSetChanged();
            }
        });
        Log.d(TAG, "Observer complete");

    }


    @Override
    public void onItemClicked(int position) {
        Log.d(TAG, "itemclicked open");
     //   dA.setPosition(position);
        //detailsActivity d = new detailsActivity(1);
        String pos= valueOf(position);
        Intent intent = new Intent( ListActivity.this,detailsActivity.class);
        intent.putExtra("position",pos);
        Log.d(TAG, "real position:"+position);
       // Log.d(TAG, "getpostion:"+dA.getPosition());
        startActivity(intent);
        //startActivity(new Intent(getApplicationContext(),detailsActivity.class));
        Log.d(TAG, "itemclicked close");
    }
}