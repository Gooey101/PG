package com.example.christophergu.pg.data;

public class NewGame {
    private String sport;
    private String description;
    private String gameDate;
    private String startTime;
    private String endTime;
    private int minAge;
    private int maxAge;
    private int minSkillLevel;
    private int capacity;
    private String phone;
    private int cid;

    public NewGame(String phone, String description, String gameDate, String startTime,
                   String endTime, int minAge, int maxAge, int minSkillLevel, int capacity,
                   String sport, int cid){

        this.phone = phone;
        this.description = description;
        this.gameDate = gameDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minSkillLevel = minSkillLevel;
        this.capacity = capacity;
        this.sport = sport;
        this.cid = cid;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinSkillLevel() {
        return minSkillLevel;
    }

    public void setMinSkillLevel(int minSkillLevel) {
        this.minSkillLevel = minSkillLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
