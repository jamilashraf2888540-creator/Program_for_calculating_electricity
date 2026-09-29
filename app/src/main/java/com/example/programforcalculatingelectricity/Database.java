package com.example.programforcalculatingelectricity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity.db";
    private static final int DATABASE_VERSION = 1;

    // ===== جدول العدادات =====
    public static final String TABLE_METERS = "meters";
    public static final String COL_METER_ID = "id";
    public static final String COL_METER_NAME = "name";

    // ===== جدول القراءات =====
    public static final String TABLE_READINGS = "readings";
    public static final String COL_READING_ID = "id";
    public static final String COL_METER_FK = "meter_id";           // ربط مع جدول meters
    public static final String COL_PREVIOUS_READING = "previous_reading";
    public static final String COL_CURRENT_READING = "current_reading";
    public static final String COL_CONSUMPTION = "consumption";
    public static final String COL_PRICE_PER_KWH = "price_per_kwh";
    public static final String COL_TOTAL_BILL = "total_bill";
    public static final String COL_DATE = "date";

    public Database(@Nullable Context context) {
        super(context, "database", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_METERS + " (" +
                COL_METER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_METER_NAME + " TEXT NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_READINGS + " (" +
                COL_READING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_METER_FK + " INTEGER NOT NULL, " +
                COL_PREVIOUS_READING + " REAL NOT NULL, " +
                COL_CURRENT_READING + " REAL NOT NULL, " +
                COL_CONSUMPTION + " REAL NOT NULL, " +
                COL_PRICE_PER_KWH + " REAL NOT NULL, " +
                COL_TOTAL_BILL + " REAL NOT NULL, " +
                COL_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COL_METER_FK + ") REFERENCES " + TABLE_METERS + "(" + COL_METER_ID + ")" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_METERS);
        onCreate(db);

    }
    public long insertMeter(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_METER_NAME, name);
        long id = db.insert(TABLE_METERS, null, values);
        db.close();
        return id;
    }
    public Cursor getAllMeters() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_METERS, null);
    }
    public long insertReading(long meterId, double previousReading, double currentReading, double consumption, double pricePerKwh, double totalBill, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_METER_FK, meterId);
        values.put(COL_PREVIOUS_READING, previousReading);
        values.put(COL_CURRENT_READING, currentReading);
        values.put(COL_CONSUMPTION, consumption);
        values.put(COL_PRICE_PER_KWH, pricePerKwh);
        values.put(COL_TOTAL_BILL, totalBill);
        values.put(COL_DATE, date);
        long id = db.insert(TABLE_READINGS, null, values);
        db.close();
        return id;
    }
    public Cursor getReadingsByMeter(long meterId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_READINGS +
                        " WHERE " + COL_METER_FK + " = ?" +
                        " ORDER BY " + COL_READING_ID + " DESC",
                new String[]{String.valueOf(meterId)}
        );
    }

    // جلب آخر قراءة لعداد معين — عشان نعرض القراءة السابقة تلقائي
    public Cursor getLastReading(long meterId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_READINGS +
                        " WHERE " + COL_METER_FK + " = ?" +
                        " ORDER BY " + COL_READING_ID + " DESC LIMIT 1",
                new String[]{String.valueOf(meterId)}
        );
    }
}
