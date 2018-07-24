package com.kanzankazu.hardwaretest.database.room.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "thardware_data")
public class Hardware implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int idHardware;

    @ColumnInfo(name = "name_hardware")
    public String nameHardware;

    @ColumnInfo(name = "status_hardware")
    public String statusHardware;

    @ColumnInfo(name = "desc_status")
    public String descHardware;

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
