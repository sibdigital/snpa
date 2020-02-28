package ru.p03.snpa.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reg_practice_view_statistic", schema = "main")
public class RegPracticeViewStatistic {

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
    @Column(name = "PREVIOUS")
    private String previous;
    @Column(name = "ID_SEARCH_STATISTIC")
    private Long idSearchStatistic;

    @Override
    public String toString() {
        return "RegPracticeViewStatistic{" +
                "id=" + id +
                "dateView=" + dateView +
                "ip=" + ip +
                "regPracticeCode=" + regPracticeCode +
                "previous=" + previous +
                "idSearchStatistic=" + idSearchStatistic +
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

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public Long getIdSearchStatistic() {
        return idSearchStatistic;
    }

    public void setIdSearchStatistic(Long idSearchStatistic) {
        this.idSearchStatistic = idSearchStatistic;
    }
}
