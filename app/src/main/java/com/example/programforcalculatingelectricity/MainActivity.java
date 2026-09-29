package com.example.programforcalculatingelectricity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // افحص قاعدة البيانات — عنده عدادات؟
        Database db = new Database(this);
        Cursor cursor = db.getAllMeters();
        boolean hasMeters = cursor.getCount() > 0;
        cursor.close();
        db.close();

        if (hasMeters) {
            // عنده عدادات → روح لقائمة العدادات
            startActivity(new Intent(this, MeterListActivity.class));
        } else {
            // ما عنده عدادات → روح لشاشة إضافة أول عداد
            startActivity(new Intent(this, add_meter.class));
        }

        finish(); // أغلق MainActivity عشان ما يرجعلها بزر الرجوع
    }
}