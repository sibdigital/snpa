package ru.p03.snpa.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "reg_practice_attribute", schema = "main")
public class RegPracticeAttribute {

    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long id;
    @SerializedName("code_attribute")
    @Column(name = "CODE_ATTRIBUTE")
    private String codeAttribute;
    @SerializedName("code_practice")
    @Column(name = "CODE_PRACTICE")
    private String codePractice;
    @SerializedName("attribute_type")
    @Column(name = "ATTRIBUTE_TYPE")
    private int attributeType;
    @SerializedName("doc_type")
    @Column(name = "DOC_TYPE")
    private String docType;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAttribute() {
        return codeAttribute;
    }

    public void setCodeAttribute(String codeAttribute) {
        this.codeAttribute = codeAttribute;
    }

    public String getCodePractice() {
        return codePractice;
    }

    public void setCodePractice(String codePractice) {
        this.codePractice = codePractice;
    }

    public int getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(int attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return "\nRegPracticeAttribute{" +
                "id=" + id +
                ", codeAttribute='" + codeAttribute + '\'' +
                ", codePractice='" + codePractice + '\'' +
                ", attributeType='" + attributeType + '\'' +
                '}';
    }
}
