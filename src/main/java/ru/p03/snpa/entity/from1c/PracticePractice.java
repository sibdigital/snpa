package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsPracticePractice;

public class PracticePractice {

    @SerializedName("cls_practice_practice")
    ClsPracticePractice[] clsPracticePractices;

    public ClsPracticePractice[] getClsPracticePractices() {
        return clsPracticePractices;
    }

    public void setClsPracticePractices(ClsPracticePractice[] clsPracticePractices) {
        this.clsPracticePractices = clsPracticePractices;
    }
}
