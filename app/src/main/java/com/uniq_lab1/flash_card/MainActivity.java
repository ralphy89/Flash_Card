package com.uniq_lab1.flash_card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

public class MainActivity extends AppCompatActivity {

    public FlashcardDatabase flashCard_database = null; // Declaration of the database
    public ArrayList<Flashcard> flashCard_array = new ArrayList<>(); // list of flash_cards
    protected int current_display_index = 0;
    protected int edit = 0; //  If this = 1, the DB update the value , else continue
    protected Flashcard card_to_edit;
    protected UniqueNumberGenerator random, defaultt;
    protected int state = 1, state2 = 0, state3 = 0; // variable to controle the state of random int generator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        flashCard_database = new FlashcardDatabase(this);
        flashCard_database.initFirstCard(); // Add the default card
        flashCard_array.addAll(flashCard_database.getAllCards()); // Add all cards to list

        random = new UniqueNumberGenerator(1, flashCard_array.size()-1);
        defaultt = new UniqueNumberGenerator(0, flashCard_array.size() - 1);

        int def = defaultt.getNextNumber();
        current_display_index = def;

        TextView questionText = findViewById(R.id.flashcard_question);
        TextView answerText1 = findViewById(R.id.flashcard_answer1);
        TextView answerText2 = findViewById(R.id.flashcard_answer2);
        TextView answerText3 = findViewById(R.id.flashcard_answer3);

        ImageView add_button = findViewById(R.id.add_button);
        ImageView edit_button = findViewById(R.id.edit_button);
        ImageView next_button_right = findViewById(R.id.next_button_right);
        ImageView next_button_left = findViewById(R.id.next_button_left);
        ImageView delete_button = findViewById(R.id.delete_button);

        questionText.setText(flashCard_array.get(0).getQuestion());
        answerText1.setText(flashCard_array.get(0).getAnswer());
        answerText2.setText(flashCard_array.get(0).getWrongAnswer1());
        answerText3.setText(flashCard_array.get(0).getWrongAnswer2());

        if(flashCard_array.size() == 1)
        {
            next_button_right.setVisibility(View.INVISIBLE);
        }
        //TextView [] list_answer = {answerText1, answerText2, answerText3};
        // Edit Launcher
        ActivityResultLauncher<Intent> editResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Snackbar snackbar = Snackbar.make(questionText, "Question edited Successfully!! !",
                                Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(this,
                                R.color.green_100));
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                        state2 = 0;
                        state = 1;
                        defaultt = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
                        random = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
                        // grab the data passed from AddCardActivity
                        // set the TextViews to show the EDITED question and answer
                        Intent intent = result.getData();
                        assert intent != null;
                        String question = intent.getStringExtra("Question");
                        String answer1 = intent.getStringExtra("answer1");
                        String answer2 = intent.getStringExtra("answer2");
                        String answer3 = intent.getStringExtra("answer3");
                        // Display newly created flashcard
                        questionText.setText(question);
                        answerText1.setText(answer2);
                        answerText2.setText(answer1);
                        answerText3.setText(answer3);

                        card_to_edit = flashCard_database.getAllCards().get(current_display_index);

                        card_to_edit.question = question;
                        card_to_edit.answer = answer1;
                        card_to_edit.wrongAnswer1 = answer2;
                        card_to_edit.wrongAnswer2 = answer3;

                        flashCard_database.updateCard(card_to_edit);
                        flashCard_array.add(current_display_index, card_to_edit);
                        flashCard_array.remove(current_display_index + 1);
                    }
                }
        );
        // Activity Launcher

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Snackbar snackbar = Snackbar.make(questionText, "New question added !",
                                Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(this,
                                R.color.green_100));
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                        state2 = 0;
                        state = 1;
                        defaultt = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
                        random = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
                        Intent intent = result.getData();
                        assert intent != null;
                        String question = intent.getStringExtra("Question");
                        String answer1 = intent.getStringExtra("answer1");
                        String answer2 = intent.getStringExtra("answer2");
                        String answer3 = intent.getStringExtra("answer3");

                        // Display newly created flashcard
                        questionText.setText(question);
                        answerText1.setText(answer2);
                        answerText2.setText(answer1);
                        answerText3.setText(answer3);


                        if (
                                (question != null && answer1 != null) && (answer2 != null && answer3 != null)
                        ) {
                            next_button_right.setVisibility(View.INVISIBLE);
                            // Save newly created/updated flashcard to database
                            Flashcard flashcard = new Flashcard(question, answer1, answer2, answer3);
                            flashCard_database.insertCard(flashcard);
                            // Update set of flashcards to include new card
                            flashCard_array.add(flashcard);
                            // When a card is added, the current index get the last value of the array
                            current_display_index = flashCard_array.size() - 1;
                            next_button_left.setVisibility(View.VISIBLE);
                            Log.i("New Card", "Last Index" + current_display_index + "\nSize " + flashCard_array.size());
                        } else {
                            Log.i("TAG", "Missing question or answer to input into database");
                        }
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

            Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer",
                    Toast.LENGTH_SHORT).show();
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
        });

        answerText3.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer",
                    Toast.LENGTH_SHORT).show();
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
        });

        add_button.setOnClickListener(view -> {

            Toast.makeText(MainActivity.this, "Adding new Card ",
                    Toast.LENGTH_SHORT).show();
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            Intent intent1 = new Intent(MainActivity.this, AddCardActivity.class);

            activityResultLauncher.launch(intent1);
        });

        edit_button.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Editing Card ",
                    Toast.LENGTH_SHORT).show();
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
            editResultLauncher.launch(intent2);
        });

        delete_button.setOnClickListener(view -> {
            Toast.makeText(this, "Deleting card ...", Toast.LENGTH_SHORT).show();
            Random random = new Random();
            int random_card;

            if (flashCard_array.size() == 1 || flashCard_array.isEmpty())
            {

                if(state3 == 0) {
                    current_display_index = 0;
                    flashCard_database.deleteCard(flashCard_array.get(current_display_index)
                            .getQuestion());
                    flashCard_array.remove(current_display_index);
                    state3 += 1;
                    //flashCard_array.addAll(flashCard_database.getAllCards());
                }
                current_display_index = 0;
                Intent intent = new Intent(this, EmptyStateActivity.class);
                startActivity(intent);
            }
            else if(!flashCard_array.isEmpty())
            {
                flashCard_database.deleteCard(flashCard_array.get(current_display_index)
                        .getQuestion());
                flashCard_array.remove(current_display_index);
                //flashCard_array.addAll(flashCard_database.getAllCards());
                random_card = random.nextInt(flashCard_array.size());
                current_display_index = random_card;

                System.out.println("\n\n\n************\n                              " +
                        "INDEX : " + current_display_index + "\n                      " +
                        "             SIZE  : " + flashCard_array.size() + "\n***********\n\n\n");
                questionText.setText(flashCard_array.get(current_display_index).getQuestion());
                answerText1.setText(flashCard_array.get(current_display_index).getAnswer());
                answerText2.setText(flashCard_array.get(current_display_index).getWrongAnswer1());
                answerText3.setText(flashCard_array.get(current_display_index).getWrongAnswer2());
            }

        });

        next_button_right.setOnClickListener(view -> {
            state += 1;
            if (flashCard_array.size() == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                Log.i("TAG", "flashCard size is equal 0");

                return;
            }
            // Display Random Card
            if (state <= flashCard_array.size()) {

                current_display_index = random.getNextNumber();

                // set the question and answers TextViews with data from the database

                Flashcard flash_card = flashCard_array.get(current_display_index);
                String question = flash_card.getQuestion();
                String answer1 = flash_card.getAnswer();
                String answer2 = flash_card.getWrongAnswer1();
                String answer3 = flash_card.getWrongAnswer2();

                questionText.setText(question);
                answerText1.setText(answer1);
                answerText2.setText(answer2);
                answerText3.setText(answer3);
                System.out.println("\n\n**************\n  Number " + current_display_index + "\n***************\n\n");
            }

           else {
                next_button_right.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(questionText,
                        "You've reached the end of the cards, going back to start.",
                        Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this,
                        R.color.gray_100));
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
                current_display_index = flashCard_array.size() - 1;
            }

        });

        next_button_left.setOnClickListener(view -> {
            state = 1;
            state2 += 1;
            current_display_index = 0;
            random = new UniqueNumberGenerator(1, flashCard_array.size()-1);
//            if (state2 <= flashCard_array.size() - 1) {
//                current_display_index = 0;
//            }
//            else
//            {
//                defaultt = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
//            }
            Flashcard flash_card = flashCard_array.get(0);
            String question = flash_card.getQuestion();
            String answer1 = flash_card.getAnswer();
            String answer2 = flash_card.getWrongAnswer1();
            String answer3 = flash_card.getWrongAnswer2();

            questionText.setText(question);
            answerText1.setText(answer1);
            answerText2.setText(answer2);
            answerText3.setText(answer3);
            next_button_left.setVisibility(View.INVISIBLE);
            next_button_right.setVisibility(View.VISIBLE);

        });
    }
}
