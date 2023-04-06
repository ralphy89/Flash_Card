package com.uniq_lab1.flash_card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView questionText = findViewById(R.id.flashcard_question);
        TextView answerText1 = findViewById(R.id.flashcard_answer1);
        TextView answerText2 = findViewById(R.id.flashcard_answer2);
        TextView answerText3 = findViewById(R.id.flashcard_answer3);
        ImageView add_button = findViewById(R.id.add_button);
        ImageView edit_button = findViewById(R.id.edit_button);
        //TextView [] list_answer = {answerText1, answerText2, answerText3};
        // Activity Launcher
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Snackbar snackbar = Snackbar.make(questionText, "New question added !", Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.green_100));
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                        Intent intent = result.getData();
                        assert intent != null;
                        String question = intent.getStringExtra("Question");
                        String answer1 = intent.getStringExtra("answer1");
                        String answer2 = intent.getStringExtra("answer2");
                        String answer3 = intent.getStringExtra("answer3");
                        questionText.setText(question);
                        answerText1.setText(answer2);
                        answerText2.setText(answer1);
                        answerText3.setText(answer3);
                    }
                }
        );

        answerText1.setOnClickListener(view -> {


            Toast.makeText(MainActivity.this, "Bravo !! Correct Answer",
                        Toast.LENGTH_SHORT).show();
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
        });

        answerText2.setOnClickListener(view -> {

            Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer", Toast.LENGTH_SHORT).show();
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
        });

        answerText3.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer", Toast.LENGTH_SHORT).show();
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
        });


        add_button.setOnClickListener(view -> {
           Toast.makeText(MainActivity.this, "Adding new Card ", Toast.LENGTH_SHORT).show();
           answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
           answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
           answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
           Intent intent1 = new Intent(MainActivity.this, AddCardActivity.class);
           activityResultLauncher.launch(intent1);
        });

        edit_button.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Editing Card ", Toast.LENGTH_SHORT).show();
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));

            String question = questionText.getText().toString();
            String ans1 = answerText1.getText().toString();
            String ans2 = answerText2.getText().toString();
            String ans3 = answerText3.getText().toString();

            Intent intent2 = new Intent(MainActivity.this, AddCardActivity.class);

            intent2.putExtra("Question", question);
            intent2.putExtra("Answer1", ans1);
            intent2.putExtra("Answer2", ans2);
            intent2.putExtra("Answer3", ans3);

            activityResultLauncher.launch(intent2);
        });

    }
}
