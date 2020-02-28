package ru.p03.snpa.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "cls_practice_practice", schema = "main")
public class ClsPracticePractice {
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long id;
    @SerializedName("practice1_code")
    @Column(name = "PRACTICE1_CODE")
    private String practice1Code;
    @SerializedName("practice2_code")
    @Column(name = "PRACTICE2_CODE")
    private String practice2Code;
    @SerializedName("type")
    @Column(name = "TYPE")
    private String type;
    @SerializedName("doc_type1")
    @Column(name = "DOC_TYPE1")
    private String docType1;
    @SerializedName("doc_type2")
    @Column(name = "DOC_TYPE2")
    private String docType2;
    @SerializedName("condition")
    @Column(name = "CONDITION")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPractice1Code() {
        return practice1Code;
    }

    public void setPractice1Code(String practice1Code) {
        this.practice1Code = practice1Code;
    }

    public String getPractice2Code() {
        return practice2Code;
    }

    public void setPractice2Code(String practice2Code) {
        this.practice2Code = practice2Code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ClsPracticePractice{" +
                "id=" + id +
                ", practice1Code='" + practice1Code + '\'' +
                ", practice2Code='" + practice2Code + '\'' +
                ", type='" + type + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
