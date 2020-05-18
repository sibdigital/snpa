package ru.p03.snpa.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "reg_search_statistic", schema = "main")
public class RegSearchStatistic {

    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long id;
    @Column(name = "SEARCH_DATE_TIME")
    private Date searchDateTime;
    @Column(name = "SEARCH_TEXT")
    private String searchText;
    @Column(name = "SEARCH_TYPE")
    private String searchType;
    @Column(name = "SEARCH_DATE_OF_DOCUMENT_START")
    private Date searchDateOfDocumentStart;
    @Column(name = "SEARCH_DATE_OF_DOCUMENT_END")
    private Date searchDateOfDocumentEnd;
    @Column(name = "SEARCH_RELEVANCE")
    private String searchRelevance;
    @Column(name = "SEARCH_SORT_TYPE")
    private String searchSortType;
    @Column(name = "RESULTS_COUNT")
    private Integer resultsCount;
    @Column(name = "PROCEED_TIME")
    private Long proceedTime;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "COMMENT")
    private String comment;

    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @Column(name = "ACTION_TAGS")
    private String[] actionTags;
    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @Column(name = "LIFE_SITUATION_TAGS")
    private String[] lifeSituationTags;
    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @Column(name = "PAYMENT_TYPE_TAGS")
    private String[] paymentTypeTags;
    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @Column(name = "QUESTION_TAGS")
    private String[] questionTags;
    @Type(type = "ru.p03.snpa.entity.GenericArrayTags")
    @Column(name = "RESULTS")
    private String[] results;

    @Override
    public String toString() {
        return "SearchStatistic{" +
                "id=" + id +
                ", searchDateTime=" + searchDateTime +
                ", searchText='" + searchText + '\'' +
                ", searchType='" + searchType + '\'' +
                ", searchDateOfDocumentStart=" + searchDateOfDocumentStart +
                ", searchDateOfDocumentEnd=" + searchDateOfDocumentEnd +
                ", searchRelevance='" + searchRelevance + '\'' +
                ", searchSortType='" + searchSortType + '\'' +
                ", resultsCount='" + resultsCount + '\'' +
                ", proceedTime='" + proceedTime + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", actionTags=" + Arrays.toString(actionTags) +
                ", lifeSituationTags=" + Arrays.toString(lifeSituationTags) +
                ", paymentTypeTags=" + Arrays.toString(paymentTypeTags) +
                ", questionTags=" + Arrays.toString(questionTags) +
                ", results=" + Arrays.toString(results) +
                '}';
    }

    public Date getSearchDateTime() {
        return searchDateTime;
    }

    public void setSearchDateTime(Date searchDateTime) {
        this.searchDateTime = searchDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Date getSearchDateOfDocumentStart() {
        return searchDateOfDocumentStart;
    }

    public void setSearchDateOfDocumentStart(Date searchDateOfDocumentStart) {
        this.searchDateOfDocumentStart = searchDateOfDocumentStart;
    }

    public Date getSearchDateOfDocumentEnd() {
        return searchDateOfDocumentEnd;
    }

    public void setSearchDateOfDocumentEnd(Date searchDateOfDocumentEnd) {
        this.searchDateOfDocumentEnd = searchDateOfDocumentEnd;
    }

    public String getSearchRelevance() {
        return searchRelevance;
    }

    public void setSearchRelevance(String searchRelevance) {
        this.searchRelevance = searchRelevance;
    }

    public String getSearchSortType() {
        return searchSortType;
    }

    public void setSearchSortType(String searchSortType) {
        this.searchSortType = searchSortType;
    }

    public Integer getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
    }

    public Long getProceedTime() {
        return proceedTime;
    }

    public void setProceedTime(Long proceedTime) {
        this.proceedTime = proceedTime;
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

    public String[] getActionTags() {
        return actionTags;
    }

    public void setActionTags(String[] actionTags) {
        this.actionTags = actionTags;
    }

    public String[] getLifeSituationTags() {
        return lifeSituationTags;
    }

    public void setLifeSituationTags(String[] lifeSituationTags) {
        this.lifeSituationTags = lifeSituationTags;
    }

    public String[] getPaymentTypeTags() {
        return paymentTypeTags;
    }

    public void setPaymentTypeTags(String[] paymentTypeTags) {
        this.paymentTypeTags = paymentTypeTags;
    }

    public String[] getQuestionTags() {
        return questionTags;
    }

    public void setQuestionTags(String[] questionTags) {
        this.questionTags = questionTags;
    }

    public String[] getResults() {
        return results;
    }

    public void setResults(String[] results) {
        this.results = results;
    }
}
