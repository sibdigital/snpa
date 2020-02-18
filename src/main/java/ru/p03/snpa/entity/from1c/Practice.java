package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.RegPractice;

public class Practice {

    @SerializedName("reg_practice")
    private RegPractice[] regPractices;

    public RegPractice[] getRegPractices() {
        return regPractices;
    }

    public void setRegPractices(RegPractice[] regPractices) {
        this.regPractices = regPractices;
    }
}
