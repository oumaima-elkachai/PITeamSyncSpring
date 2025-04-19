package com.example.payroll.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.util.Date;
import java.util.List;


@Document(collection = "projets")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Projet {

    @Id
    private String id; // MongoDB uses String by default for IDs

    private String name;
    private Date startDate;
    private Date endDate;

    private List<String> budgetIds;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setBudgetIds(List<String> budgetIds) {
        this.budgetIds = budgetIds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<String> getBudgetIds() {
        return budgetIds;
    }
}
