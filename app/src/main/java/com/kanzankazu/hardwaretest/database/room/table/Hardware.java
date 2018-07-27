package com.kanzankazu.hardwaretest.database.room.table;

import java.io.Serializable;

public class Hardware implements Serializable {

    public int idHardware;

    public String nameHardware;

    public String statusHardware;

    public String descHardware;

    public Hardware(String nameHardware, String statusHardware, String descHardware) {
        this.nameHardware = nameHardware;
        this.statusHardware = statusHardware;
        this.descHardware = descHardware;
    }

    public Hardware() {

    }

    public int getIdHardware() {
        return idHardware;
    }

    public void setIdHardware(int idHardware) {
        this.idHardware = idHardware;
    }

    public String getNameHardware() {
        return nameHardware;
    }

    public void setNameHardware(String nameHardware) {
        this.nameHardware = nameHardware;
    }

    public String getStatusHardware() {
        return statusHardware;
    }

    public void setStatusHardware(String statusHardware) {
        this.statusHardware = statusHardware;
    }

    public String getDescHardware() {
        return descHardware;
    }

    public void setDescHardware(String descHardware) {
        this.descHardware = descHardware;
    }
}
