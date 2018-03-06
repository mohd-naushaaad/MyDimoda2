package com.mydimoda.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Parth on 3/6/2018.
 */

public class ReviewTripData {
    private List<ModelLookListing> totalLookList = new ArrayList<>();
    private String tripTitle;
    private Date startDate;
    private String rowId;

    public ReviewTripData(List<ModelLookListing> totalLookList, String tripTitle, Date startDate, String rowId) {
        this.totalLookList = totalLookList;
        this.tripTitle = tripTitle;
        this.startDate = startDate;
        this.rowId = rowId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public List<ModelLookListing> getTotalLookList() {
        return totalLookList;
    }

    public void setTotalLookList(List<ModelLookListing> totalLookList) {
        this.totalLookList = totalLookList;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
