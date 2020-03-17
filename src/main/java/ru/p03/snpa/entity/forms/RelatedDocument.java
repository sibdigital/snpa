package ru.p03.snpa.entity.forms;

import ru.p03.snpa.entity.RegPractice;

public class RelatedDocument {

    private String type;
    private RegPractice regPractice;
    private String condition;

    public String getDocType1() {
        return docType1;
    }

    public void setDocType1(String docType1) {
        this.docType1 = docType1;
    }

    public String getDocType2() {
        return docType2;
    }

    public void setDocType2(String docType2) {
        this.docType2 = docType2;
    }

    private String docType1;
    private String docType2;

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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
