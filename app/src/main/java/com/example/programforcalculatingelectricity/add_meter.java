package com.example.programforcalculatingelectricity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_meter extends AppCompatActivity {

    EditText etMeterName, etPreviousReading, etCurrentReading, etPricePerKwh;
    Button btnSaveMeter;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meter);
        db = new Database(this);
        initViews();

        btnSaveMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meterName     = etMeterName.getText().toString().trim();
                String previousStr   = etPreviousReading.getText().toString().trim();
                String currentStr    = etCurrentReading.getText().toString().trim();
                String priceStr      = etPricePerKwh.getText().toString().trim();

                if (meterName.isEmpty() || previousStr.isEmpty() || currentStr.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(add_meter.this, "يوجد حقل فارغ", Toast.LENGTH_SHORT).show();
                    return;
                }

                double prev        = Double.parseDouble(previousStr);
                double curr        = Double.parseDouble(currentStr);
                double price       = Double.parseDouble(priceStr);
                double consumption = curr - prev;
                double totalBill   = consumption * price;

                // 1. احفظ العداد وخد الـ id
                long meterId = db.insertMeter(meterName);

                // 2. احفظ القراءة الأولى مربوطة بالعداد
                String today = java.text.DateFormat.getDateInstance().format(new java.util.Date());
                db.insertReading(meterId, prev, curr, consumption, price, totalBill, today);

                // 3. روح لشاشة النتيجة
                Intent intent = new Intent(add_meter.this, reading_result.class);
                intent.putExtra("meter_id",        meterId);
                intent.putExtra("meter_name",      meterName);
                intent.putExtra("previous_reading", prev);
                intent.putExtra("current_reading",  curr);
                intent.putExtra("consumption",      consumption);
                intent.putExtra("price_per_kwh",    price);
                intent.putExtra("total_bill",       totalBill);
                startActivity(intent);
            }
        });
    }

    void initViews() {
        etMeterName      = findViewById(R.id.etMeterName);
        etPreviousReading = findViewById(R.id.etPreviousReading);
        etCurrentReading  = findViewById(R.id.etCurrentReading);
        etPricePerKwh     = findViewById(R.id.etPricePerKwh);
        btnSaveMeter      = findViewById(R.id.btnSaveMeter);
    }
}