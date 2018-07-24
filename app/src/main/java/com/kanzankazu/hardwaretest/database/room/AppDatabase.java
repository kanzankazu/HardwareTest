package com.kanzankazu.hardwaretest.database.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kanzankazu.hardwaretest.database.room.dao.HardwareDAO;
import com.kanzankazu.hardwaretest.database.room.table.Hardware;

@Database(entities = {Hardware.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract HardwareDAO hardwareDAO();

}