package com.example.christophergu.pg.data;

public class EmergencyContact {
    private String phone;
    private String fName;
    private String relationship;
    private String ecPhone;

    public EmergencyContact(String phone, String fName, String relationship, String ecPhone) {
        this.phone = phone;
        this.fName = fName;
        this.relationship = relationship;
        this.ecPhone = ecPhone;
    }

    public EmergencyContact(String fName, String relationship, String ecPhone) {
        this.fName = fName;
        this.relationship = relationship;
        this.ecPhone = ecPhone;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEcPhone() {
        return ecPhone;
    }

    public void setEcPhone(String ecPhone) {
        this.ecPhone = ecPhone;
    }
}

