package com.example.christophergu.pg.data;

public class Team {
    private int tid;
    private String tName;
    private String description;
    private int numMembers;


    public Team(int tid, String tName, String description, int numMembers) {
        this.tid = tid;
        this.tName = tName;
        this.description = description;
        this.numMembers = numMembers;
    }

    public Team(int tid, String tName, String description) {
        this.tid = tid;
        this.tName = tName;
        this.description = description;
    }

    public Team(int tid, String tName) {
        this.tid = tid;
        this.tName = tName;
    }


    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }
}
