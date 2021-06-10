package com.example.quiz.views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.model.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String currentUserId;

     String quizName="";
     String quizId="";

    //UI Elements
    private TextView quizTitle;
    private Button optionOneBtn;
    private Button optionTwoBtn;
    private Button optionThreeBtn;
    private Button nextBtn;
    private TextView questionFeedback;
    private TextView questionText;
    private TextView questionTime;
    private ProgressBar questionProgress;
    private TextView questionNumber;

    private List<QuestionModel> allQuestionsList = new ArrayList<>();
    private List<QuestionModel> questionsToAnswer = new ArrayList<>();
    private Long totalQuestionsToAnswer = 0L;
    private CountDownTimer countDownTimer;

    private boolean canAnswer = false;
    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int notAnswered = 0;

    DocumentReference Q;
    public QuizActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        firebaseAuth = FirebaseAuth.getInstance();

        //Get User ID
        if(firebaseAuth.getCurrentUser() != null){
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        } else {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        //UI Initialize
        quizTitle = findViewById(R.id.quiz_title);
        optionOneBtn = findViewById(R.id.quiz_option_one);
        optionTwoBtn = findViewById(R.id.quiz_option_two);
        optionThreeBtn = findViewById(R.id.quiz_option_three);
        nextBtn = findViewById(R.id.quiz_next_btn);
        questionFeedback = findViewById(R.id.quiz_question_feedback);
        questionText = findViewById(R.id.quiz_question);
        questionTime = findViewById(R.id.quiz_question_time);
        questionProgress = findViewById(R.id.quiz_question_progress);
        questionNumber = findViewById(R.id.quiz_question_number);

        quizId = getIntent().getStringExtra("QuizID");
        Log.d(TAG, "QuizActivity Quiz id  " +quizId);
        quizName = getIntent().getStringExtra("Name");
        totalQuestionsToAnswer = Long.parseLong(getIntent().getStringExtra("Questions"));

        //Query Firestore Data
     //   firebaseFirestore.collection("QuizList")
               // .document(quizId).collection("Questions")
        firebaseFirestore.collection("QuizList").
                document(quizId).collection("Questions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    allQuestionsList = task.getResult().toObjects(QuestionModel.class);
                    pickQuestions();
                    loadUI();
                } else {
                    quizTitle.setText("Error : " + task.getException().getMessage());
                }
            }
        });

//Set Button Click Listeners
        optionOneBtn.setOnClickListener(this);
        optionTwoBtn.setOnClickListener(this);
        optionThreeBtn.setOnClickListener(this);

        nextBtn.setOnClickListener(this);



    }
    private void loadUI() {
        //Quiz Data Loaded, Load the UI
        quizTitle.setText(quizName);
        questionText.setText("Load First Question");

        //Enable Options
        enableOptions();

        //Load First Question
        loadQuestion(1);
    }


    private void loadQuestion(int questNum) {
        //Set Question Number
        questionNumber.setText(questNum + "");

        //Load Question Text
        questionText.setText(questionsToAnswer.get(questNum-1).getQuestion());

        //Load Options
        optionOneBtn.setText(questionsToAnswer.get(questNum-1).getOption_a());
        optionTwoBtn.setText(questionsToAnswer.get(questNum-1).getOption_b());
        optionThreeBtn.setText(questionsToAnswer.get(questNum-1).getOption_c());

        //Question Loaded, Set Can Answer
        canAnswer = true;
        currentQuestion = questNum;

        //Start Question Timer
        startTimer(questNum);
    }

    private void startTimer(int questionNumber) {

        //Set Timer Text
        final Long timeToAnswer = questionsToAnswer.get(questionNumber-1).getTimer();
        questionTime.setText(timeToAnswer.toString());

        //Show Timer ProgressBar
        questionProgress.setVisibility(View.VISIBLE);

        //Start CountDown
        countDownTimer = new CountDownTimer(timeToAnswer*1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Update Time
                questionTime.setText(millisUntilFinished/1000 + "");

                //Progress in percent
                Long percent = millisUntilFinished/(timeToAnswer*10);
                questionProgress.setProgress(percent.intValue());
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                //Time Up, Cannot Answer Question Anymore
                canAnswer = false;

                questionFeedback.setText("Time Up! No answer was submitted.");
                questionFeedback.setTextColor(getResources().getColor(R.color.blue, null));
                notAnswered++;
                showNextBtn();
            }
        };

        countDownTimer.start();
    }

    private void enableOptions() {
        //Show All Option Buttons
        optionOneBtn.setVisibility(View.VISIBLE);
        optionTwoBtn.setVisibility(View.VISIBLE);
        optionThreeBtn.setVisibility(View.VISIBLE);

        //Enable Option Buttons
        optionOneBtn.setEnabled(true);
        optionTwoBtn.setEnabled(true);
        optionThreeBtn.setEnabled(true);

        //Hide Feedback and next Button
        questionFeedback.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);
    }

    private void pickQuestions() {
        for(int i=0; i < totalQuestionsToAnswer; i++) {
            int randomNumber = getRandomInt(0, allQuestionsList.size());
            questionsToAnswer.add(allQuestionsList.get(randomNumber));
            allQuestionsList.remove(randomNumber);
            Log.d("QUESTIONS LOG", "Question " + i + " : " + questionsToAnswer.get(i).getQuestion());
        }
    }

    private int getRandomInt(int min, int max) {
        return ((int) (Math.random()*(max-min))) + min;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_option_one:
                verifyAnswer(optionOneBtn);
                break;
            case R.id.quiz_option_two:
                verifyAnswer(optionTwoBtn);
                break;
            case R.id.quiz_option_three:
                verifyAnswer(optionThreeBtn);
                break;
            case R.id.quiz_next_btn:
                if(currentQuestion == totalQuestionsToAnswer){
                    //Load Results
                    submitResults();
                } else {
                    currentQuestion++;
                    loadQuestion(currentQuestion);
                    resetOptions();
                }
                break;
        }
    }

    private void submitResults() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswers);
        resultMap.put("wrong", wrongAnswers);
        resultMap.put("unanswered", notAnswered);
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(currentUserId).set(resultMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Go To Results Page
                    Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                    intent.putExtra("QuizID",quizId);
                    startActivity(intent);
                } else {
                    //Show Error
                    quizTitle.setText(task.getException().getMessage());
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetOptions() {

        optionOneBtn.setTextColor(getResources().getColor(R.color.blue, null));
        optionTwoBtn.setTextColor(getResources().getColor(R.color.blue, null));
        optionThreeBtn.setTextColor(getResources().getColor(R.color.blue, null));

        questionFeedback.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verifyAnswer(Button selectedAnswerBtn) {
        //Check Answer
        if(canAnswer){
            //Set Answer Btn Text Color to Black
            selectedAnswerBtn.setTextColor(getResources().getColor(R.color.colorDark, null));

            if(questionsToAnswer.get(currentQuestion-1).getAnswer().equals(selectedAnswerBtn.getText())){
                //Correct Answer
                correctAnswers++;
                //Set Feedback Text
                questionFeedback.setText("Correct Answer");
                questionFeedback.setTextColor(getResources().getColor(R.color.blue, null));
            } else {
                //Wrong Answer
                wrongAnswers++;
                //Set Feedback Text
                questionFeedback.setText("Wrong Answer \n Correct Answer : " + questionsToAnswer.get(currentQuestion-1).getAnswer());
                questionFeedback.setTextColor(getResources().getColor(R.color.colorAccent, null));
            }
            //Set Can answer to false
            canAnswer = false;

            //Stop The Timer
            countDownTimer.cancel();

            //Show Next Button
            showNextBtn();
        }
    }

    private void showNextBtn() {
        if(currentQuestion == totalQuestionsToAnswer){
            nextBtn.setText("Submit Results");
        }
        questionFeedback.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setEnabled(true);
    }
}
