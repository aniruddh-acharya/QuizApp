package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    private TextView questions;
    private TextView question;

    private AppCompatButton option1, option2, option3, option4;
    private AppCompatButton nextBtn;
    private Timer quizTimer;

    private int totalTimeInMin;
    private int seconds;
    private int range;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    private List<QuestionsList> questionLists = new ArrayList<>();

    private int currentQuestionPosition =0;
    private String selectedOptionByUser = "";
    Random random =new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final ImageView backBtn= findViewById(R.id.backBtn);
        final TextView timer = findViewById(R.id.timer);
        final TextView selectedTopicName = findViewById(R.id.topicName);

        questions=findViewById(R.id.questions);
        question=findViewById(R.id.question);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);

        nextBtn=findViewById(R.id.nextBtn);

        final String getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        selectedTopicName.setText(getSelectedTopicName);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizapp-48f22-default-rtdb.firebaseio.com");

        ProgressDialog progressDialog = new ProgressDialog(QuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalTimeInMin = snapshot.child("time").getValue(Integer.class);
                if(!getSelectedTopicName.equals("Arithmetic"))
                {
                    for (DataSnapshot dataSnapshot : snapshot.child(getSelectedTopicName).getChildren())
                    {
                        final String getQuestion = dataSnapshot.child("question").getValue(String.class);
                        final String getoption1 = dataSnapshot.child("option1").getValue(String.class);
                        final String getoption2 = dataSnapshot.child("option2").getValue(String.class);
                        final String getoption3 = dataSnapshot.child("option3").getValue(String.class);
                        final String getoption4 = dataSnapshot.child("option4").getValue(String.class);
                        final String getanswer = dataSnapshot.child("answer").getValue(String.class);

                        QuestionsList questionList = new QuestionsList(getQuestion, getoption1, getoption2, getoption3, getoption4, getanswer,"");
                        questionLists.add(questionList);
                        }
                }

                else
                {
                    range=snapshot.child("range").getValue(Integer.class);

                      for(int j =0;j<5;j++)
                      {

                          int value1 = random.nextInt(range);
                          int value2 = random.nextInt(range);

                          int indexOfCorrectAnswer = random.nextInt(4);
                          answers.clear();

                          for(int i = 0; i<4; i++){

                              if(indexOfCorrectAnswer == i){
                                  answers.add(value1+value2);
                              }else {
                                  int wrongAnswer = random.nextInt(range*2);
                                  while(wrongAnswer==value1+value2){

                                      wrongAnswer = random.nextInt(range*2);
                                  }
                                  answers.add(wrongAnswer);
                              }
                          }

                        final String getQuestion = Integer.toString(value1)+" + "+Integer.toString(value2);
                        final String getoption1 = Integer.toString(answers.get(0));
                        final String getoption2 = Integer.toString(answers.get(1));
                        final String getoption3 = Integer.toString(answers.get(2));
                        final String getoption4 = Integer.toString(answers.get(3));
                        final String getanswer = Integer.toString(answers.get(indexOfCorrectAnswer));

                        QuestionsList questionList = new QuestionsList(getQuestion, getoption1, getoption2, getoption3, getoption4, getanswer,"");
                        questionLists.add(questionList);

                      }
                }

                progressDialog.hide();

                questions.setText((currentQuestionPosition+1)+"/"+questionLists.size());
                question.setText(questionLists.get(0).getQuestion());
                option1.setText(questionLists.get(0).getOption1());
                option2.setText(questionLists.get(0).getOption2());
                option3.setText(questionLists.get(0).getOption3());
                option4.setText(questionLists.get(0).getOption4());

                startTimer(timer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        option1.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty())
            {
                selectedOptionByUser = option1.getText().toString();
                option1.setBackgroundResource(R.drawable.round_back_red);
                option1.setTextColor(Color.WHITE);

                revealAnswer();

                questionLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });
        option2.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty())
            {
                selectedOptionByUser = option2.getText().toString();
                option2.setBackgroundResource(R.drawable.round_back_red);
                option2.setTextColor(Color.WHITE);

                revealAnswer();

                questionLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });
        option3.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty())
            {
                selectedOptionByUser = option3.getText().toString();
                option3.setBackgroundResource(R.drawable.round_back_red);
                option3.setTextColor(Color.WHITE);

                revealAnswer();

                questionLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });
        option4.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty())
            {
                selectedOptionByUser = option4.getText().toString();
                option4.setBackgroundResource(R.drawable.round_back_red);
                option4.setTextColor(Color.WHITE);

                revealAnswer();

                questionLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });
        nextBtn.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty())
            {
                Toast.makeText(QuizActivity.this, "Please Select an option", Toast.LENGTH_SHORT).show();
            }
            else
            {
                changeNextQuestion();
            }
        });

        backBtn.setOnClickListener(view -> {

            quizTimer.purge();
            quizTimer.cancel();

            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        });
    }
    private void changeNextQuestion()
    {
        currentQuestionPosition++;

        if((currentQuestionPosition+1)== questionLists.size())
        {
            nextBtn.setText("Submit Quiz");
        }
        if(currentQuestionPosition < questionLists.size())
        {
            selectedOptionByUser = "";

            option1.setBackgroundResource(R.drawable.round_back_white_strok2);
            option1.setTextColor(Color.parseColor("#1F6BB8"));
            option2.setBackgroundResource(R.drawable.round_back_white_strok2);
            option2.setTextColor(Color.parseColor("#1F6BB8"));
            option3.setBackgroundResource(R.drawable.round_back_white_strok2);
            option3.setTextColor(Color.parseColor("#1F6BB8"));
            option4.setBackgroundResource(R.drawable.round_back_white_strok2);
            option4.setTextColor(Color.parseColor("#1F6BB8"));


            questions.setText((currentQuestionPosition+1)+"/"+questionLists.size());
            question.setText(questionLists.get(currentQuestionPosition).getQuestion());
            option1.setText(questionLists.get(currentQuestionPosition).getOption1());
            option2.setText(questionLists.get(currentQuestionPosition).getOption2());
            option3.setText(questionLists.get(currentQuestionPosition).getOption3());
            option4.setText(questionLists.get(currentQuestionPosition).getOption4());
        }

        else
        {
           Intent intent = new Intent(QuizActivity.this,QuizResults.class);
           intent.putExtra("correct",getCorrectAnswers());
           intent.putExtra("incorrect",getIncorrectAnswers());
           startActivity(intent);

           finish();
        }

    }
    private void startTimer(TextView timerTextView)
    {
        quizTimer = new Timer();

        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(seconds==0)
                {
                    totalTimeInMin--;
                    seconds=59;
                }
                else if (seconds==0 && totalTimeInMin==0)
                {
                    quizTimer.purge();
                    quizTimer.cancel();
                    Toast.makeText(QuizActivity.this, "Over", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(QuizActivity.this,QuizResults.class);
                    intent.putExtra("Correct", getCorrectAnswers());
                    intent.putExtra("Incorrect", getIncorrectAnswers());
                    startActivity(intent);

                    finish();
                }
                else
                {
                    seconds--;
                }

                runOnUiThread(() -> {

                    String finalMinutes = String.valueOf(totalTimeInMin);
                    String finalSeconds = String.valueOf(seconds);

                    if (finalMinutes.length() == 1)
                    {
                        finalMinutes="0"+finalMinutes;
                    }

                    if (finalSeconds.length() == 1)
                    {
                        finalSeconds="0"+finalMinutes;
                    }

                    timerTextView.setText(finalMinutes+":"+finalSeconds);
                });
            }
        },1000,1000);
    }

    private int getCorrectAnswers()
    {
        int correctAnswers = 0;

        for(int i=0;i<questionLists.size();i++)
        {
           final String getUserSelectedAnswer = questionLists.get(i).getUserSelectedAnswer();
           final String getAnswer = questionLists.get(i).getAnswer();

           if(getUserSelectedAnswer.equals(getAnswer))
           {
               correctAnswers++;
           }
        }
    return correctAnswers;
    }

    private int getIncorrectAnswers()
    {
        int incorrectAnswers = 0;

        for(int i=0;i<questionLists.size();i++)
        {
            final String getUserSelectedAnswer = questionLists.get(i).getUserSelectedAnswer();
            final String getAnswer = questionLists.get(i).getAnswer();

            if(!getUserSelectedAnswer.equals(getAnswer))
            {
                incorrectAnswers++;
            }
        }
        return incorrectAnswers;
    }

    @Override
    public void onBackPressed() {

        quizTimer.purge();
        quizTimer.cancel();

        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }

    private void revealAnswer()
    {
        final String getAnswer = questionLists.get(currentQuestionPosition).getAnswer();

        if(option1.getText().toString().equals(getAnswer))
        {
            option1.setBackgroundResource(R.drawable.round_back_green10);
            option1.setTextColor(Color.WHITE);
        }
        else if(option2.getText().toString().equals(getAnswer))
        {
            option2.setBackgroundResource(R.drawable.round_back_green10);
            option2.setTextColor(Color.WHITE);
        }
        else if(option3.getText().toString().equals(getAnswer))
        {
            option3.setBackgroundResource(R.drawable.round_back_green10);
            option3.setTextColor(Color.WHITE);
        }
        else if(option4.getText().toString().equals(getAnswer))
        {
            option4.setBackgroundResource(R.drawable.round_back_green10);
            option4.setTextColor(Color.WHITE);
        }
    }
}