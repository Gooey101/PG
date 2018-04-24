package com.example.christophergu.pg.data;

public class QuitGame {

    private int join;
    private int gid;
    private String phone;


    public QuitGame(int join, int gid, String phone) {
        this.join = join;
        this.gid = gid;
        this.phone = phone;
    }

    public int getJoin() {
        return join;
    }

    public void setJoin(int join) {
        this.join = join;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
