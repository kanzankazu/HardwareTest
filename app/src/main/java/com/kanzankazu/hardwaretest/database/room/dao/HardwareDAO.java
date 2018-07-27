package com.kanzankazu.hardwaretest.database.room.dao;

import android.arch.persistence.room.Dao;

@Dao
public interface HardwareDAO {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertHardware(Hardware barang);

    @Update
    int updateHardware(Hardware barang);

    @Delete
    int deleteHardware(Hardware barang);

    @Query("SELECT * FROM thardware_data")
    Hardware[] selectAllHardware();

    @Query("SELECT * FROM thardware_data WHERE idHardware = :id LIMIT 1")
    Hardware selectHardwareById(int id);*/
}
