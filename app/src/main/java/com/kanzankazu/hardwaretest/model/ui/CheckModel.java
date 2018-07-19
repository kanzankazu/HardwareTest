package com.kanzankazu.hardwaretest.model.ui;

public class CheckModel {
    public static final int UNCHECKING = 0;
    public static final int CHECKING = 1;
    public static final int CHECK_DONE = 2;
    public static final int CHECK_ERROR = 3;

    int id;
    String modul;
    String value;
    int status;

    public CheckModel() {

    }

    public CheckModel(int id, String modul) {
        this.id = id;
        this.modul = modul;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModul() {
        return modul;
    }

    public void setModul(String modul) {
        this.modul = modul;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
