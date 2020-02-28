package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsQuestion;
import ru.p03.snpa.entity.RegPractice;

public class Question {

    @SerializedName("cls_question")
    private ClsQuestion[] clsQuestions;

    public ClsQuestion[] getClsQuestion() {
        return clsQuestions;
    }

    public void setClsQuestion(ClsQuestion[] clsQuestions) {
        this.clsQuestions = clsQuestions;
    }
}
