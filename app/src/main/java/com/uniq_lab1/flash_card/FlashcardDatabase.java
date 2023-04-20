package com.uniq_lab1.flash_card;

import android.content.Context;
import android.content.res.Resources;

import androidx.room.Room;

import java.util.List;
public class FlashcardDatabase {

    private final AppDatabase db;
    public FlashcardDatabase(Context context) {
        db = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class, "flashcard-database"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public void initFirstCard() {

        if (db.flashcardDao().getAll().isEmpty()) {
            insertCard(
                    new Flashcard(
                            "Who is the 44th president of the United States ?",
                            "Jovenel Moise",
                            "Barack Obama",
                            "Donald Trump"
                    )
            );
        }
    }

    public List<Flashcard> getAllCards() {
        return db.flashcardDao().getAll();
    }

    public void insertCard(Flashcard flashcard) {
        db.flashcardDao().insertAll(flashcard);
    }

    public void deleteCard(String flashcardQuestion) {
        List<Flashcard> allCards = db.flashcardDao().getAll();
        for (Flashcard card : allCards) {
            if (card.getQuestion().equals(flashcardQuestion)) {
                db.flashcardDao().delete(card);
            }
        }
    }

    public void updateCard(Flashcard flashcard) {
        db.flashcardDao().update(flashcard);
    }

    public void deleteAll() {
        for (Flashcard flashcard : db.flashcardDao().getAll()) {
            db.flashcardDao().delete(flashcard);
        }
    }
}