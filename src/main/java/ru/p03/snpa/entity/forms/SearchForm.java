package ru.p03.snpa.entity.forms;

import java.util.Arrays;

public class SearchForm {

    private String searchText;
    private int searchPage;
    private String searchType;
    private String searchDateOfDocumentStart;
    private String searchDateOfDocumentEnd;
    private String searchRelevance;
    private String searchSortType;
    private String[] searchTagList;
    private String[] searchTagNameList;
    private String[] idPractices;

    private Long searchId;
    private Short status;
    private String comment;

    public String[] getIdPractices() {
        return idPractices;
    }

    public void setIdPractices(String[] idPractices) {
        this.idPractices = idPractices;
    }

    public String[] getSearchTagNameList() {
        return searchTagNameList;
    }

    public void setSearchTagNameList(String[] searchTagNameList) {
        this.searchTagNameList = searchTagNameList;
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

    public String getSearchDateOfDocumentStart() {
        return searchDateOfDocumentStart;
    }

    public void setSearchDateOfDocumentStart(String searchDateOfDocumentStart) {
        this.searchDateOfDocumentStart = searchDateOfDocumentStart;
    }

    public String getSearchDateOfDocumentEnd() {
        return searchDateOfDocumentEnd;
    }

    public void setSearchDateOfDocumentEnd(String searchDateOfDocumentEnd) {
        this.searchDateOfDocumentEnd = searchDateOfDocumentEnd;
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

    public int getSearchPage() {
        return searchPage;
    }

    public void setSearchPage(int searchPage) {
        this.searchPage = searchPage;
    }

    public String[] getSearchTagList() {
        return searchTagList;
    }

    public void setSearchTagList(String[] searchTagList) {
        this.searchTagList = searchTagList;
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
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

    @Override
    public String toString() {
        return "SearchForm{" +
                "searchText='" + searchText + '\'' +
                ", searchPage=" + searchPage +
                ", searchType='" + searchType + '\'' +
                ", searchDateOfDocumentStart='" + searchDateOfDocumentStart + '\'' +
                ", searchDateOfDocumentEnd='" + searchDateOfDocumentEnd + '\'' +
                ", searchRelevance='" + searchRelevance + '\'' +
                ", searchTagList=" + Arrays.toString(searchTagList) +
                '}';
    }
}
