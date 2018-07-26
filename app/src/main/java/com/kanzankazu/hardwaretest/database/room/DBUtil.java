package com.kanzankazu.hardwaretest.database.room;

import android.arch.persistence.room.Room;

import com.kanzankazu.hardwaretest.App;

public class DBUtil {
    public static AppDatabase dbBarang = Room
            .databaseBuilder(App.getContext(), AppDatabase.class, "barangdb")
            .allowMainThreadQueries()
            .build();

}
