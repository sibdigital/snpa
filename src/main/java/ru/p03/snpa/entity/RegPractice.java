package ru.p03.snpa.entity;

import com.google.gson.annotations.SerializedName;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reg_practice", schema = "main")
@NamedQueries({
        // -----------------searchType == ALL ------------------
        @NamedQuery(name = "RegPractice.findAllValid", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfter", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllExpired", query = "SELECT r FROM RegPractice r " +
                "WHERE r.dateEnd<:now " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfter", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllInvalid", query = "SELECT r FROM RegPractice r " +
                "WHERE r.dateStart>:now " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfter", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentBefore", query = "SELECT r FROM RegPractice r " +
                "WHERE ((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        // -----------------searchType == ALL ------------------




        // -----------------searchType == P ------------------
        @NamedQuery(name = "RegPractice.findAllValidAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC"),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllExpiredAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllInvalidAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeP", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='p')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        // -----------------searchType == P ------------------




        // -----------------searchType == R ------------------
        @NamedQuery(name = "RegPractice.findAllValidAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllExpiredAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllInvalidAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeR", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='n')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        // -----------------searchType == R ------------------






        // -----------------searchType == Z ------------------
        @NamedQuery(name = "RegPractice.findAllValidAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC"),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateStart<:now AND r.dateEnd>:now)" +
                "OR(r.dateStart IS NULL AND r.dateEnd>:now)" +
                "OR(r.dateStart<:now AND r.dateEnd IS NULL)" +
                "OR(r.dateStart IS NULL AND r.dateEnd IS NULL)) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllExpiredAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateEnd<:now) " +
                "ORDER BY r.dateOfDocument DESC "),

        @NamedQuery(name = "RegPractice.findAllInvalidAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument>:dateStart)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        @NamedQuery(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeZ", query = "SELECT r FROM RegPractice r " +
                "WHERE (r.docType='z')" +
                "AND((r.dateOfDocument<:dateEnd)OR(r.dateOfDocument IS NULL))" +
                "AND(r.dateStart>:now) " +
                "ORDER BY r.dateOfDocument DESC "),
        // -----------------searchType == Z ------------------


})
public class RegPractice {
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long id;
    @Column(name = "ID_PARENT")
    private Long idParent;
    @SerializedName("name")
    @Column(name = "NAME")
    private String name;
    @SerializedName("content")
    @Column(name = "CONTENT")
    private String content;
    @SerializedName("code")
    @Column(name = "CODE")
    private String code;
    @SerializedName("number")
    @Column(name = "NUMBER")
    private String number;
    @SerializedName("date_of_document")
    @Column(name = "DATE_OF_DOCUMENT")
    private Date dateOfDocument;
    @SerializedName("code_parent")
    @Column(name = "CODE_PARENT")
    private String codeParent;
    @SerializedName("doc_type")
    @Column(name = "DOC_TYPE")
    private String docType;
    @SerializedName("parent_doc_type")
    @Column(name = "PARENT_DOC_TYPE")
    private String parentDocType;
    @SerializedName("pos_num")
    @Column(name = "POS_NUM")
    private Integer posNum;
    @SerializedName("date_start")
    @Column(name = "DATE_START")
    private Date dateStart;
    @SerializedName("date_end")
    @Column(name = "DATE_END")
    private Date dateEnd;
    @SerializedName("parent_name")
    @Column(name = "PARENT_NAME")
    private String parentName;
    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @SerializedName("file")
    @Column(name = "FILES")
    private String[] files;

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "RegPractice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", number='" + number + '\'' +
                ", dateOfDocument=" + dateOfDocument +
                ", codeParent='" + codeParent + '\'' +
                ", docType='" + docType + '\'' +
                ", parentDocType='" + parentDocType + '\'' +
                ", posNum=" + posNum +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getParentDocType() {
        return parentDocType;
    }

    public void setParentDocType(String parentDocType) {
        this.parentDocType = parentDocType;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Integer getPosNum() {
        return posNum;
    }

    public void setPosNum(Integer posNum) {
        this.posNum = posNum;
    }

    public Date getDateOfDocument() {
        return dateOfDocument;
    }

    public void setDateOfDocument(Date dateOfDocument) {
        this.dateOfDocument = dateOfDocument;
    }

    public String getCodeParent() {
        return codeParent;
    }

    public void setCodeParent(String codeParent) {
        this.codeParent = codeParent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
