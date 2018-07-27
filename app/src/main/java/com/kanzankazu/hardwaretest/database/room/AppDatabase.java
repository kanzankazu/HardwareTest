package com.kanzankazu.hardwaretest.database.room;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kanzankazu.hardwaretest.database.room.table.Hardware;

import java.util.ArrayList;

public class AppDatabase extends SQLiteOpenHelper {
    // Databases information
    private static final String TAG = "SQLiteHelper";
    private static final String DATABASE_NAME = "hardware.db";
    private static final int DATABASE_VERSION = 1;

    public static String TABEL_HARDWARE = "tabip";
    public static String KEY_HARDWARE_ID = "hardwareId";
    public static String KEY_HARDWARE_NAME = "hardwareName";
    public static String KEY_HARDWARE_STATUS = "hardwareStatus";
    public static String KEY_HARDWARE_DESC = "hardwareDesc";

    private static final String query_add_table = "CREATE TABLE IF NOT EXISTS " + TABEL_HARDWARE + "("
            + KEY_HARDWARE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_HARDWARE_NAME + " BLOB,"
            + KEY_HARDWARE_STATUS + " TEXT,"
            + KEY_HARDWARE_DESC + " TEXT)";
    private static final String query_delete_table = "DROP TABLE IF EXISTS " + TABEL_HARDWARE;

    private int id;

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_add_table);
        /*db.execSQL(query_add_table_oui);
        db.execSQL(query_add_table_subnet);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(query_delete_table);
        /*db.execSQL(query_delete_table_oui);
        db.execSQL(query_delete_table_subnet);*/
    }

    /*CRUD Hardware*/

    public void insertHardware(Hardware hardware) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_HARDWARE_ID, getLastNumaberIdDBIp(TABEL_HARDWARE) + 1);
        contentValues.put(KEY_HARDWARE_NAME, hardware.getNameHardware());
        contentValues.put(KEY_HARDWARE_STATUS, hardware.getStatusHardware());
        contentValues.put(KEY_HARDWARE_DESC, hardware.getDescHardware());
        db.insert(TABEL_HARDWARE, null, contentValues);
        db.close();
    }

    public int updateHardware(Hardware hardware) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HARDWARE_NAME, hardware.getNameHardware());
        contentValues.put(KEY_HARDWARE_STATUS, hardware.getStatusHardware());
        contentValues.put(KEY_HARDWARE_DESC, hardware.getDescHardware());
        int q = db.update(TABEL_HARDWARE, contentValues, KEY_HARDWARE_ID + " = ? ", new String[]{String.valueOf(hardware.getIdHardware())});
        db.close();
        return q;
    }

    public void deleteHardwareById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("deleteHardwareById from " + TABEL_HARDWARE + " where " + KEY_HARDWARE_NAME + " = '" + ipString + "'");
        db.delete(TABEL_HARDWARE, KEY_HARDWARE_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Hardware> findAllIp() {
        ArrayList<Hardware> ipModelList = new ArrayList<Hardware>();
        String query = "SELECT * FROM " + TABEL_HARDWARE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Hardware ipModel = new Hardware();
                ipModel.setIdHardware(cursor.getInt(0));
                ipModel.setNameHardware(cursor.getString(1));
                ipModel.setStatusHardware(cursor.getString(2));
                ipModel.setDescHardware(cursor.getString(3));
                ipModelList.add(ipModel);
            } while (cursor.moveToNext());
        }

        return ipModelList;
    }

    public ArrayList<String> findAll_ListIp() {
        ArrayList<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABEL_HARDWARE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public void deleteAllDataIp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABEL_HARDWARE);
        db.close();
    }

    public void deleteTebaleHardware() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query_delete_table);
        db.close();
    }

    public int getLastNumaberIdDBIp(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + table, null);
        if (cursor.moveToLast()) {
            //name = cursor.getString(column_index);//to get other values
            id = cursor.getInt(0);//to get id, 0 is the column index
        }
        return id;
    }

    public int getCountNumberDataDBIp(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, table);
    }

    public Cursor findAll_CursorIp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABEL_HARDWARE, null);
        return cursor;
    }


}