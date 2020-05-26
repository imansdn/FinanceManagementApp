package com.imandroid.financemanagement.data.db;

public class ExpenditureSummation {

    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subscriptionExp;
    private float othersExp;
    private float reminder;

    public ExpenditureSummation(float foodExp, float transportExp, float entertainmentExp, float subscriptionExp, float othersExp) {
        this.foodExp = foodExp;
        this.transportExp = transportExp;
        this.entertainmentExp = entertainmentExp;
        this.subscriptionExp = subscriptionExp;
        this.othersExp = othersExp;
    }

    public ExpenditureSummation(float foodExp, float transportExp, float entertainmentExp, float subscriptionExp, float othersExp, float reminder) {
        this.foodExp = foodExp;
        this.transportExp = transportExp;
        this.entertainmentExp = entertainmentExp;
        this.subscriptionExp = subscriptionExp;
        this.othersExp = othersExp;
        this.reminder = reminder;
    }

    public float getReminder() {
        return reminder;
    }

    public void setReminder(float reminder) {
        this.reminder = reminder;
    }

    public float getFoodExp() {
        return foodExp;
    }

    public void setFoodExp(float foodExp) {
        this.foodExp = foodExp;
    }

    public float getTransportExp() {
        return transportExp;
    }

    public void setTransportExp(float transportExp) {
        this.transportExp = transportExp;
    }

    public float getEntertainmentExp() {
        return entertainmentExp;
    }

    public void setEntertainmentExp(float entertainmentExp) {
        this.entertainmentExp = entertainmentExp;
    }

    public float getSubscriptionExp() {
        return subscriptionExp;
    }

    public void setSubscriptionExp(float subscriptionExp) {
        this.subscriptionExp = subscriptionExp;
    }

    public float getOthersExp() {
        return othersExp;
    }

    public void setOthersExp(float othersExp) {
        this.othersExp = othersExp;
    }
}
