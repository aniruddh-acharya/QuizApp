package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String selectedTopicName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout java =findViewById(R.id.javalayout);
        final LinearLayout arithmetic =findViewById(R.id.arithmeticlayout);
        final LinearLayout html =findViewById(R.id.htmllayout);
        final LinearLayout android =findViewById(R.id.androidlayout);

        final Button startBtn =findViewById(R.id.startQuizBtn);

        java.setOnClickListener(view -> {
            selectedTopicName = "java";
            java.setBackgroundResource(R.drawable.round_back_white_stroke);

            arithmetic.setBackgroundResource(R.drawable.round_back_white);
            html.setBackgroundResource(R.drawable.round_back_white);
            android.setBackgroundResource(R.drawable.round_back_white);

        });

        arithmetic.setOnClickListener(view -> {
            selectedTopicName = "Arithmetic";
            arithmetic.setBackgroundResource(R.drawable.round_back_white_stroke);

            java.setBackgroundResource(R.drawable.round_back_white);
            html.setBackgroundResource(R.drawable.round_back_white);
            android.setBackgroundResource(R.drawable.round_back_white);

        });

        html.setOnClickListener(view -> {
            selectedTopicName = "HTML";
            html.setBackgroundResource(R.drawable.round_back_white_stroke);

            arithmetic.setBackgroundResource(R.drawable.round_back_white);
            java.setBackgroundResource(R.drawable.round_back_white);
            android.setBackgroundResource(R.drawable.round_back_white);

        });

        android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "android";
                android.setBackgroundResource(R.drawable.round_back_white_stroke);

                arithmetic.setBackgroundResource(R.drawable.round_back_white);
                html.setBackgroundResource(R.drawable.round_back_white);
                java.setBackgroundResource(R.drawable.round_back_white);

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTopicName.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please Select Topic",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent= new Intent(MainActivity.this,QuizActivity.class);
                    intent.putExtra("selectedTopic",selectedTopicName);
                    startActivity(intent);
                }
            }
        });

    }
}