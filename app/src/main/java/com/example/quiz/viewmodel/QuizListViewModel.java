package com.example.quiz.viewmodel;
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quiz.model.AppRepository;
import com.example.quiz.model.QuizListModel;

import java.util.List;

public class QuizListViewModel extends ViewModel implements AppRepository.OnFirestoreTaskComplete {
    Application app;
    private MutableLiveData<List<QuizListModel>> quizListModelData = new MutableLiveData<>();

    public LiveData<List<QuizListModel>> getQuizListModelData() {
        return quizListModelData;
    }

    private AppRepository firebaseRepository = new AppRepository(app,this);

    public QuizListViewModel() {
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(List<QuizListModel> quizListModelsList) {
        quizListModelData.setValue(quizListModelsList);
    }

    @Override
    public void onError(Exception e) {

    }
}