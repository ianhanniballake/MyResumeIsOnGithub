package com.myresumeisongithub.model;

import java.util.Date;

public class JobHistory {
    private String title;
    private String description;
    private String company;
    private String companyLogoUrl;
    private Date startDate;
    private Date endDate;

    public JobHistory() {
    }

    public JobHistory(String title, String description, String company, String companyLogoUrl,
                      Date startDate, Date endDate) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.companyLogoUrl = companyLogoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
