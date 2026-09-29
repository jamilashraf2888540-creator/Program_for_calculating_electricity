package com.example.programforcalculatingelectricity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MeterListActivity extends AppCompatActivity {

    RecyclerView recyclerMeters;
    TextView tvMeterCount;
    ImageButton btnAddMeter;
    Database db;
    MeterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_list);
        db = new Database(this);
        initViews();

        // زر إضافة عداد جديد
        btnAddMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeterListActivity.this, add_meter.class));
            }
        });

        recyclerMeters.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // كل مرة ترجع للشاشة نحدث القائمة
        loadMeters();
    }

    void loadMeters() {
        Cursor cursor = db.getAllMeters();
        int count = cursor.getCount();
        tvMeterCount.setText(count + " عدادات");

        if (count > 0) {
            findViewById(R.id.recyclerMeters).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutEmptyState).setVisibility(View.GONE);
        } else {
            findViewById(R.id.recyclerMeters).setVisibility(View.GONE);
            findViewById(R.id.layoutEmptyState).setVisibility(View.VISIBLE);

            // ← ضيف هاد
            findViewById(R.id.btnAddFirstMeter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MeterListActivity.this, add_meter.class));
                }
            });
        }

        adapter = new MeterAdapter(this, cursor, new MeterAdapter.OnMeterClickListener() {
            @Override
            public void onMeterClick(long meterId, String meterName) {
                Intent intent = new Intent(MeterListActivity.this, MeterHistoryActivity.class); // ← غير هاد
                intent.putExtra("meter_id",   meterId);
                intent.putExtra("meter_name", meterName);
                startActivity(intent);
            }
        });
        recyclerMeters.setAdapter(adapter);
    }

    void initViews() {
        recyclerMeters = findViewById(R.id.recyclerMeters);
        tvMeterCount   = findViewById(R.id.tvMeterCount);
        btnAddMeter    = findViewById(R.id.btnAddMeter);
    }
}
