package com.kanzankazu.hardwaretest.database;

public class Check {
    public static final int UNCHECKING = 0;
    public static final int CHECKING = 1;
    public static final int CHECK_DONE = 2;
    public static final int CHECK_ERROR = 3;

    int id;
    String name;
    String value;
    String desc;

    public Check() { }

    public Check(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getCheck(){
        return getCheck();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String modul) {
        this.name = modul;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
