package com.kanzankazu.hardwaretest.database.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kanzankazu.hardwaretest.database.room.table.Hardware;

@Dao
public interface HardwareDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHardware(Hardware barang);

    @Update
    int updateHardware(Hardware barang);

    @Delete
    int deleteHardware(Hardware barang);

    @Query("SELECT * FROM thardware_data")
    Hardware[] selectAllHardwares();

    @Query("SELECT * FROM thardware_data WHERE idHardware = :id LIMIT 1")
    Hardware selectHardwareDetail(int id);
}
