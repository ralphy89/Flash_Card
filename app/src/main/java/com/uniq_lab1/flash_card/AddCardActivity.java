package com.uniq_lab1.flash_card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ImageView cancel_img = findViewById(R.id.cancel_button);
        ImageView save_img = findViewById(R.id.save_button);

        EditText question = findViewById(R.id.edit_text);
        EditText answer1 = findViewById(R.id.edit_text1);
        EditText answer2 = findViewById(R.id.edit_text2);
        EditText answer3 = findViewById(R.id.edit_text3);


        if(getIntent().getStringExtra("Question") != null) {
            String question_text = getIntent().getStringExtra("Question");
            String ans1 = getIntent().getStringExtra("Answer1");
            String ans2 = getIntent().getStringExtra("Answer2");
            String ans3 = getIntent().getStringExtra("Answer3");
            if (question_text.length() > 0) {
                question.setText(question_text);
            }
            if (ans1.length() > 0) {
                answer1.setText(ans2);
            }
            if (ans2.length() > 0) {
                answer2.setText(ans1);
            }
            if (ans3.length() > 0) {
                answer3.setText(ans3);
            }
        }

        cancel_img.setOnClickListener(view -> {

            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        });
        save_img.setOnClickListener(view -> {
            String quest, ans1, ans2, ans3;
            quest = question.getText().toString();
            ans1 = answer1.getText().toString();
            ans2 = answer2.getText().toString();
            ans3 = answer3.getText().toString();

            Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
            if(quest.length() == 0)
            {
                Toast.makeText(AddCardActivity.this, "Must enter the question to continue", Toast.LENGTH_SHORT).show();
            } else if (ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0) {
                Toast.makeText(AddCardActivity.this, "Must enter question and the 3 answers to continue", Toast.LENGTH_SHORT).show();
            } else
            {
                intent.putExtra("Question", quest);
                intent.putExtra("answer1", ans1);
                intent.putExtra("answer2", ans2);
                intent.putExtra("answer3", ans3);
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }
}