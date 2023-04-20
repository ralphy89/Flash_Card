
package com.uniq_lab1.flash_card;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Flashcard {
    @ColumnInfo(name = "question")
    protected String question;

    @ColumnInfo(name = "answer")
    protected String answer;

    @ColumnInfo(name = "wrong_answer_1")
    protected String wrongAnswer1;

    @ColumnInfo(name = "wrong_answer_2")
    protected String wrongAnswer2;

    @PrimaryKey(autoGenerate = true)
    private int uuid;

    public Flashcard(String question, String answer, String wrongAnswer1, String wrongAnswer2) {
        this.question = question;
        this.answer = wrongAnswer1;
        this.wrongAnswer1 = answer;
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }


    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
