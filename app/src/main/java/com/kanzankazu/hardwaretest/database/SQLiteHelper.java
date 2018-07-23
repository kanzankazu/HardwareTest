package com.kanzankazu.hardwaretest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    // Databases information

    private static final String DATABASE_NAME = "hardwaretest.db";
    private static final int DATABASE_VERSION = 1;
    public static String TABEL_CHECK = "tbCheckHardware";
    public static String KEY_ID = "id";
    public static String KEY_NAME = "name";
    public static String KEY_VALUE = "value";
    public static String KEY_DESC = "desc";

    private static final String query_add_table = "CREATE TABLE IF NOT EXISTS "
            + TABEL_CHECK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT,"
            + KEY_VALUE + " TEXT,"
            + KEY_DESC + "TEXT)";

    private static final String query_delete_table = "DROP TABLE IF EXISTS " + TABEL_CHECK;

    private static final String TAG = "SQLiteHelper";
    private int id;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_add_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(query_delete_table);
    }

    public void save(Check check) {
        SQLiteDatabase db = SQLiteHelper.this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, check.getName());
        contentValues.put(KEY_VALUE, check.getValue());
        contentValues.put(KEY_DESC, check.getDesc());
        db.insert(TABEL_CHECK, null, contentValues);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABEL_CHECK);
        db.close();
    }

    public void setName(Check check) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, check.getName());
        contentValues.put(KEY_VALUE, check.getValue());
        db.update(TABEL_CHECK, contentValues, "checkid = ? ",
                new String[]{Integer.toString(Integer.parseInt(String.valueOf(check.getId())))});
        db.close();
    }
}