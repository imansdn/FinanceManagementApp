package com.imandroid.financemanagement.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface ExpenditureDao {

    @Query("SELECT * FROM expenditure")
    List<ExpenditureEntity> getAllExp();

    @Query("SELECT * FROM expenditure WHERE id = :id LIMIT 1")
    ExpenditureEntity getExp(int id);

    @Query("SELECT COUNT(*) FROM expenditure")
    int getExpCount();

    @Query("SELECT COUNT(*) FROM expenditure WHERE category = :category AND date(date) = date('now', 'localtime')")
    int getCategoryExpCountForDay(String category);

    @Query("SELECT COUNT(*) FROM expenditure WHERE category = :category AND date(date) >= DATE('now', 'weekday 0', '-7 days')")
    int getCategoryExpCountForWeek(String category);

    @Query("SELECT COUNT(*) FROM expenditure WHERE category = :category AND strftime( '%m', date)  = strftime('%m', 'now')")
    int getCategoryExpCountForMonth(String category);

    @Query("SELECT SUM(cost) FROM expenditure WHERE category = :category AND date(date) = date('now', 'localtime')")
    float getCategoryExpSumForDay(String category);

    @Query("SELECT SUM(cost) FROM expenditure WHERE category = :category AND date(date) >= DATE('now', 'weekday 0', '-7 days')")
    float getCategoryExpSumForWeek(String category);

    @Query("SELECT SUM(cost) FROM expenditure WHERE category = :category AND strftime( '%m', date)  = strftime('%m', 'now')")
    float getCategoryExpSumForMonth(String category);

    @Query("SELECT * FROM expenditure WHERE category = :category ORDER BY date(date) DESC")
    List<ExpenditureEntity> getCategoryExp(String category);

    @Update
    void updateExp(ExpenditureEntity exp);

    @Insert
    void addExp(ExpenditureEntity exp);

    @Delete
    void deleteExp(ExpenditureEntity exp);

    @Query("DELETE FROM expenditure")
    void deleteAllExp();
}
