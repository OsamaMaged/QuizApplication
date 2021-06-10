package com.example.quiz.model;
import com.google.firebase.firestore.DocumentId;

public class QuizListModel {

    @DocumentId
    private String quiz_id;
    private String name, number, visibility;
    private long questions;

    public QuizListModel() {}

    public QuizListModel(String quiz_id, String name,  String number, String visibility, long questions) {
        this.quiz_id = quiz_id;
        this.name = name;
        this.number =number;
        this.visibility = visibility;
        this.questions = questions;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String level) {
        this.number = level;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }
}
