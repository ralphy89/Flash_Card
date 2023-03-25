package com.uniq_lab1.flash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView questionText = findViewById(R.id.flashcard_question);
        TextView answerText = findViewById(R.id.flashcard_answer);

        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "I clicked the question", Toast.LENGTH_LONG).show();
                questionText.setVisibility(View.INVISIBLE);
                answerText.setVisibility(View.VISIBLE);
            }
        });
    }
}