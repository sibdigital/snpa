package ru.p03.snpa.entity.forms;

import ru.p03.snpa.entity.RegPractice;

public class ResultForm {

    private boolean success;
    private String time;
    private String data;
    private Iterable<RegPractice> regPractice2Iterable;

    private Long searchId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Iterable<RegPractice> getRegPractice2Iterable() {
        return regPractice2Iterable;
    }

    public void setRegPractice2Iterable(Iterable<RegPractice> regPractice2Iterable) {
        this.regPractice2Iterable = regPractice2Iterable;
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    @Override
    public String toString() {
        return "ResultForm{" +
                "success=" + success +
                ", time=" + time +
                ", regPractice2Iterable=" + regPractice2Iterable +
                '}';
    }
}
