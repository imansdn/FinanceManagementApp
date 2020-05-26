package com.imandroid.financemanagement.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenditure")
public class ExpenditureEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "cost")
    private float cost;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    private String date;

    public ExpenditureEntity(Integer id, float cost, String category, String description, String date) {
        this.id = id;
        this.cost = cost;
        this.category = category;
        this.description = description;
        this.date = date;
    }


    public Integer getId() {
        return id;
    }


    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
