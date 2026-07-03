package com.petfoodmonitoring.app.model;

import java.sql.Date;

public class FeedingHistory {

    private int id;
    private Date feedingDate;
    private String feedingTime;
    private String status;
    private String remarks;
    private int scheduleId;

    public FeedingHistory() {
    }

    public FeedingHistory(Date feedingDate, String feedingTime, String status,
                          String remarks, int scheduleId) {

        this.feedingDate = feedingDate;
        this.feedingTime = feedingTime;
        this.status = status;
        this.remarks = remarks;
        this.scheduleId = scheduleId;
    }

    public FeedingHistory(int id, Date feedingDate, String feedingTime, String status,
                          String remarks, int scheduleId) {

        this.id = id;
        this.feedingDate = feedingDate;
        this.feedingTime = feedingTime;
        this.status = status;
        this.remarks = remarks;
        this.scheduleId = scheduleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFeedingDate() {
        return feedingDate;
    }

    public void setFeedingDate(Date feedingDate) {
        this.feedingDate = feedingDate;
    }

    public String getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(String feedingTime) {
        this.feedingTime = feedingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
