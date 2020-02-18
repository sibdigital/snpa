package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.RegPracticeAttribute;

public class PracticeAttribute {

    @SerializedName("reg_practice_attribute")
    RegPracticeAttribute[] regPracticeAttributes;

    public RegPracticeAttribute[] getRegPracticeAttributes() {
        return regPracticeAttributes;
    }

    public void setRegPracticeAttributes(RegPracticeAttribute[] regPracticeAttributes) {
        this.regPracticeAttributes = regPracticeAttributes;
    }
}
