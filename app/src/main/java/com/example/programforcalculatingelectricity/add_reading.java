package com.example.programforcalculatingelectricity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_reading extends AppCompatActivity {

    TextView tvMeterNameSubtitle, tvPreviousReadingValue, tvCalculatedCost;
    EditText etCurrentReading, etPricePerKwh, etDate;
    Button btnSaveReading;
    Database db;

    String meterName;
    long meterId;
    double previousReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading);
        db = new Database(this);
        initViews();
        // بعد initViews() مباشرة
        String today = java.text.DateFormat.getDateInstance().format(new java.util.Date());
        etDate.setText(today);
        etDate.setFocusable(false);

        // استقبل اسم العداد والـ id من MeterListActivity
        meterName = getIntent().getStringExtra("meter_name");
        meterId   = getIntent().getLongExtra("meter_id", -1);

        // اجلب آخر قراءة من قاعدة البيانات تلقائياً
        Cursor cursor = db.getLastReading(meterId);
        if (cursor.moveToFirst()) {
            previousReading = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_CURRENT_READING));
        }
        cursor.close();

        tvMeterNameSubtitle.setText(meterName);
        tvPreviousReadingValue.setText(previousReading + " kWh");

        btnSaveReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentStr = etCurrentReading.getText().toString().trim();
                String priceStr   = etPricePerKwh.getText().toString().trim();

                if (currentStr.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(add_reading.this, "يوجد حقل فارغ", Toast.LENGTH_SHORT).show();
                    return;
                }

                double curr        = Double.parseDouble(currentStr);
                double price       = Double.parseDouble(priceStr);
                double consumption = curr - previousReading;
                double totalBill   = consumption * price;

                // احفظ القراءة في قاعدة البيانات
                String today = java.text.DateFormat.getDateInstance().format(new java.util.Date());
                db.insertReading(meterId, previousReading, curr, consumption, price, totalBill, today);

                // روح لشاشة النتيجة
                Intent intent = new Intent(add_reading.this, reading_result.class);
                intent.putExtra("meter_id",         meterId);
                intent.putExtra("meter_name",       meterName);
                intent.putExtra("previous_reading", previousReading);
                intent.putExtra("current_reading",  curr);
                intent.putExtra("consumption",      consumption);
                intent.putExtra("price_per_kwh",    price);
                intent.putExtra("total_bill",       totalBill);
                startActivity(intent);
            }
        });
    }

    void initViews() {
        tvMeterNameSubtitle   = findViewById(R.id.tvMeterNameSubtitle);
        tvPreviousReadingValue = findViewById(R.id.tvPreviousReadingValue);
        tvCalculatedCost       = findViewById(R.id.tvCalculatedCost);
        etCurrentReading       = findViewById(R.id.etCurrentReading);
        etPricePerKwh          = findViewById(R.id.etPricePerKwh);
        etDate                 = findViewById(R.id.etDate);
        btnSaveReading         = findViewById(R.id.btnSaveReading);
    }
}