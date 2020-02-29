package ru.p03.snpa.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "cls_question", schema = "main")
public class ClsQuestion {
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @SerializedName("content")
    @Column(name = "CONTENT")
    private String content;

    @SerializedName("practice_code")
    @Column(name = "PRACTICE_CODE")
    private String practiceCode;

    @SerializedName("parent_code")
    @Column(name = "PARENT_CODE")
    private String parentCode;

    @SerializedName("code")
    @Column(name = "CODE")
    private String code;

    @Override
    public String toString() {
        return "ClsQuestion{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", practiceCode='" + practiceCode + '\'' +
                ", parentCode='" + parentCode + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPracticeCode() {
        return practiceCode;
    }

    public void setPracticeCode(String practiceCode) {
        this.practiceCode = practiceCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
