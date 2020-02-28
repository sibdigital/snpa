package ru.p03.snpa.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reg_practice_rating", schema = "main")
public class RegPracticeRating {

    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long id;
    @Column(name = "DATE_VIEW")
    private Timestamp dateView;
    @Column(name = "IP")
    private String ip;
    @Column(name = "REG_PRACTICE_CODE")
    private String regPracticeCode;
    @Column(name = "ID_PRACTICE_VIEW_STATISTIC")
    private Long idPracticeViewStatistic;
    @Column(name = "ID_SEARCH_STATISTIC")
    private Long idSearchStatistic;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "COMMENT")
    private String comment;

    @Override
    public String toString() {
        return "RegPracticeRating{" +
                "id=" + id +
                "dateView=" + dateView +
                "ip=" + ip +
                "regPracticeCode=" + regPracticeCode +
                "idPracticeViewStatistic=" + idPracticeViewStatistic +
                "idSearchStatistic=" + idSearchStatistic +
                "status=" + status +
                "comment=" + comment +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateView() {
        return dateView;
    }

    public void setDateView(Timestamp dateView) {
        this.dateView = dateView;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegPracticeCode() {
        return regPracticeCode;
    }

    public void setRegPracticeCode(String regPracticeCode) {
        this.regPracticeCode = regPracticeCode;
    }

    public Long getIdPracticeViewStatistic() {
        return idPracticeViewStatistic;
    }

    public void setIdPracticeViewStatistic(Long idPracticeViewStatistic) {
        this.idPracticeViewStatistic = idPracticeViewStatistic;
    }

    public Long getIdSearchStatistic() {
        return idSearchStatistic;
    }

    public void setIdSearchStatistic(Long idSearchStatistic) {
        this.idSearchStatistic = idSearchStatistic;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
