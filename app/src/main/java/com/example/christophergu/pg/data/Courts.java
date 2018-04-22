package com.example.christophergu.pg.data;

public class Courts {
    private int cid;
    private boolean mPublic;
    private boolean mOutside;
    private String openTime;
    private String closeTime;
    private String address;
    private int numLanes = 0;
    private int numHoops = 0;
    private int numNets = 0;
    private int grass = 2;

    public Courts(int cid, String address, boolean mPublic, boolean mOutside, String openTime, String closeTime, int numLanes, int numHoops, int numNets, int grass) {
        this.cid = cid;
        this.mPublic = mPublic;
        this.mOutside = mOutside;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.numHoops = numHoops;
        this.numLanes = numLanes;
        this.numNets = numNets;
        this.grass = grass;
    }



    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public boolean ismPublic() {
        return mPublic;
    }

    public void setmPublic(boolean mPublic) {
        this.mPublic = mPublic;
    }

    public boolean ismOutside() {
        return mOutside;
    }

    public void setmOutside(boolean mOutside) {
        this.mOutside = mOutside;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumLanes() {
        return numLanes;
    }

    public void setNumLanes(int numLanes) {
        this.numLanes = numLanes;
    }

    public int getNumHoops() {
        return numHoops;
    }

    public void setNumHoops(int numHoops) {
        this.numHoops = numHoops;
    }

    public int getNumNets() {
        return numNets;
    }

    public void setNumNets(int numNets) {
        this.numNets = numNets;
    }

    public int isGrass() {
        return grass;
    }

    public void setGrass(int grass) {
        this.grass = grass;
    }
}
