package com.uniq_lab1.flash_card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public FlashcardDatabase flashCard_database = null; // Declaration of the database
    public ArrayList<Flashcard> flashCard_array = new ArrayList<>(); // list of flash_cards
    protected int current_display_index = 0;
    protected int edit = 0; //  If this = 1, the DB update the value , else continue
    protected Flashcard card_to_edit;
    protected UniqueNumberGenerator random, defaultt, random_index;
    protected int state = 1, state2 = 0, state3 = 0, state4 = 0; // variable to controle the state of random int generator
    protected CountDownTimer countDownTimer = null;
    protected int[] location = new int[2];
    protected String correct_answer = "", user_answer = "", answer_2, answer_3, default_correct;
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashCard_database = new FlashcardDatabase(this);
        flashCard_database.initFirstCard(); // Add the default card
        flashCard_array.addAll(flashCard_database.getAllCards()); // Add all cards to list

        random = new UniqueNumberGenerator(1, flashCard_array.size()-1);
        defaultt = new UniqueNumberGenerator(0, flashCard_array.size() - 1);
        random_index = new UniqueNumberGenerator(0, 2);

        ViewGroup container = findViewById(R.id.main_activity_layout);

        Animation leftOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
        Animation rightInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
        Animation leftInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);

        TextView questionText = findViewById(R.id.flashcard_question);
        TextView answerText1 = findViewById(R.id.flashcard_answer1);
        TextView answerText2 = findViewById(R.id.flashcard_answer2);
        TextView answerText3 = findViewById(R.id.flashcard_answer3);
        TextView timerTextView = findViewById(R.id.timer);


        ImageView add_button = findViewById(R.id.add_button);
        ImageView edit_button = findViewById(R.id.edit_button);
        ImageView next_button_right = findViewById(R.id.next_button_right);
        ImageView next_button_left = findViewById(R.id.next_button_left);
        ImageView delete_button = findViewById(R.id.delete_button);

        questionText.setText(flashCard_array.get(0).getQuestion());
        answerText1.setText(flashCard_array.get(0).getAnswer());
        answerText2.setText(flashCard_array.get(0).getWrongAnswer1());
        answerText3.setText(flashCard_array.get(0).getWrongAnswer2());
        default_correct = answerText1.getText().toString();

        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(16000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Define onTick logic here
                    timerTextView.setText("" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    next_button_right.performClick();
                }
            };
        }

        countDownTimer.start();
        if(flashCard_array.size() == 1)
        {
            next_button_right.setVisibility(View.INVISIBLE);
        }
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
                        //default_correct = answer2;
                        card_to_edit = flashCard_database.getAllCards().get(current_display_index);

                        card_to_edit.question = question;
                        card_to_edit.answer = answer2;
                        card_to_edit.wrongAnswer1 = answer1;
                        card_to_edit.wrongAnswer2 = answer3;
                        flashCard_database.updateCard(card_to_edit);
                        flashCard_array.add(current_display_index, card_to_edit);
                        flashCard_array.remove(current_display_index + 1);
                        countDownTimer.start();
                    }
                    else
                    {
                        countDownTimer.start();
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
                        countDownTimer.start();

                        if (
                                (question != null && answer1 != null) && (answer2 != null && answer3 != null)
                        ) {
                            next_button_right.setVisibility(View.INVISIBLE);
                            // Save newly created/updated flashcard to database
                            Flashcard flashcard = new Flashcard(question, answer2, answer1, answer3);
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
                    else
                    {
                        countDownTimer.start();
                    }
                }
        );

        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // This method is called when the animation first starts.
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // This method is called when the animation is finished playing.
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // We don't need to worry about this method.
            }
        });
        answerText1.setOnClickListener(view -> {

            answerText1.getLocationInWindow(location);
            correct_answer = flashCard_array.get(current_display_index).answer;
            user_answer = answerText1.getText().toString();
            answer_2 = answerText2.getText().toString();
            answer_3 = answerText3.getText().toString();
            if (correct_answer.equals(user_answer) || default_correct.equals(user_answer)) {
                Toast.makeText(MainActivity.this, "Bravo !! Correct Answer",
                        Toast.LENGTH_SHORT).show();
                for(int i = 400; i < 1400; i+=400) {
                    CommonConfetti.explosion(container, location[0] + i, location[1] - 300, new int[]{Color.GREEN})
                            .oneShot();
                }
                answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                countDownTimer.cancel();

            }
            else {
                Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer",
                        Toast.LENGTH_SHORT).show();
                if(correct_answer.equals(answer_2))
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                }
                else
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                }
            }
        });

        answerText2.setOnClickListener(view -> {

            answerText2.getLocationInWindow(location);
            correct_answer = flashCard_array.get(current_display_index).answer;
            user_answer = answerText2.getText().toString();
            answer_2 = answerText1.getText().toString();
            answer_3 = answerText3.getText().toString();
            if (correct_answer.equals(user_answer) || default_correct.equals(user_answer)) {
                Toast.makeText(MainActivity.this, "Bravo !! Correct Answer",
                        Toast.LENGTH_SHORT).show();
                for(int i = 400; i < 1400; i+=400) {
                    CommonConfetti.explosion(container, location[0] + i, location[1] - 300, new int[]{Color.GREEN})
                            .oneShot();
                }
                answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            }
            else {
                Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer :",
                        Toast.LENGTH_SHORT).show();
                if(correct_answer.equals(answer_2))
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                }
                else
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                }
            }
        });

        answerText3.setOnClickListener(view -> {
            answerText3.getLocationInWindow(location);
            correct_answer = flashCard_array.get(current_display_index).answer;
            user_answer = answerText3.getText().toString();
            answer_2 = answerText2.getText().toString();
            answer_3 = answerText1.getText().toString();
            if (correct_answer.equals(user_answer) || default_correct.equals(user_answer)) {
                Toast.makeText(MainActivity.this, "Bravo !! Correct Answer",
                        Toast.LENGTH_SHORT).show();
                for(int i = 400; i < 1400; i+=400) {
                    CommonConfetti.explosion(container, location[0] + i, location[1] - 300, new int[]{Color.GREEN})
                            .oneShot();
                }
                answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
            }
            else {
                Toast.makeText(MainActivity.this, "OUPS !! Wrong Answer",
                        Toast.LENGTH_SHORT).show();
                if(correct_answer.equals(answer_3))
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                }
                else
                {
                    answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                    answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.green_100));
                    answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
                }
            }
        });

        add_button.setOnClickListener(view -> {

            Toast.makeText(MainActivity.this, "Adding new Card ",
                    Toast.LENGTH_SHORT).show();
            countDownTimer.cancel();
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            Intent intent1 = new Intent(MainActivity.this, AddCardActivity.class);

            activityResultLauncher.launch(intent1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });

        edit_button.setOnClickListener(view -> {
            countDownTimer.cancel();
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
            countDownTimer.cancel();
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
                questionText.setAnimation(rightInAnim);
                questionText.setText(flashCard_array.get(current_display_index).getQuestion());

                answerText1.setAnimation(rightInAnim);
                answerText1.setText(flashCard_array.get(current_display_index).getAnswer());

                answerText2.setAnimation(rightInAnim);
                answerText2.setText(flashCard_array.get(current_display_index).getWrongAnswer1());

                answerText3.setAnimation(rightInAnim);
                answerText3.setText(flashCard_array.get(current_display_index).getWrongAnswer2());
                countDownTimer.start();
            }
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));

        });

        next_button_right.setOnClickListener(view -> {

            state += 1;
            if (flashCard_array.size() == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute

                return;
            }
            // Display Random Card
            if (state <= flashCard_array.size()) {
                countDownTimer.start();
                current_display_index = random.getNextNumber();
                ArrayList<Integer> answers_index = new ArrayList<>();
                ArrayList<String> list_answers = new ArrayList<>();
                // set the question and answers TextViews with data from the database

                Flashcard flash_card = flashCard_array.get(current_display_index);
                String question = flash_card.getQuestion();
                String answer1 = flash_card.getAnswer();
                String answer2 = flash_card.getWrongAnswer1();
                String answer3 = flash_card.getWrongAnswer2();
                list_answers.add(answer1);
                list_answers.add(answer2);
                list_answers.add(answer3);
                questionText.startAnimation(rightInAnim);

                questionText.setText(question);
                answerText1.setAnimation(rightInAnim);
                answerText1.setText(list_answers.get(random_index.getNextNumber()));

                answerText2.setAnimation(rightInAnim);
                answerText2.setText(list_answers.get(random_index.getNextNumber()));

                answerText3.setAnimation(rightInAnim);
                answerText3.setText(list_answers.get(random_index.getNextNumber()));
                random_index = new UniqueNumberGenerator(0, 2);
                System.out.println("\n\n**************\n  Number " + current_display_index + "\n***************\n\n");
                answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
                answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));

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
            questionText.setAnimation(leftInAnim);
            questionText.setText(question);

            answerText1.setAnimation(leftInAnim);
            answerText1.setText(answer1);

            answerText2.setAnimation(leftInAnim);
            answerText2.setText(answer2);

            answerText3.setAnimation(leftInAnim);
            answerText3.setText(answer3);
            next_button_left.setVisibility(View.INVISIBLE);
            next_button_right.setVisibility(View.VISIBLE);
            answerText1.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText2.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));
            answerText3.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_100));

        });
    }
}
