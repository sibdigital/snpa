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
    @Column(name = "CONTENT")
    @SerializedName("content")
    private String content;
    @SerializedName("practice_code")
    @Column(name = "PRACTICE_CODE")
    private String practiceCode;
    @SerializedName("code")
    @Column(name = "CODE")
    private String code;
    @SerializedName("parent_code")
    @Column(name = "PARENT_CODE")
    private String parentCode;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public String toString() {
        return "ClsQuestion{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", practiceCode='" + practiceCode + '\'' +
                ", parentCode='" + parentCode + '\'' +
                '}';
    }
}
