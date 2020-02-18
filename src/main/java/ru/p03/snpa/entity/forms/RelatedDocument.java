package ru.p03.snpa.entity.forms;

import ru.p03.snpa.entity.RegPractice;

public class RelatedDocument {

    private String type;
    private RegPractice regPractice;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RegPractice getRegPractice() {
        return regPractice;
    }

    public void setRegPractice(RegPractice regPractice) {
        this.regPractice = regPractice;
    }
}
