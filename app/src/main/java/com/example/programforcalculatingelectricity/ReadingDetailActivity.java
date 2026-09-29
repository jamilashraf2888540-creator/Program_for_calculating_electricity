package com.example.programforcalculatingelectricity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReadingDetailActivity extends AppCompatActivity {

    TextView tvMeterName, tvDate, tvPreviousReading,
             tvCurrentReading, tvConsumption, tvPricePerKwh, tvTotalBill;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_detail);

        // Views
        tvMeterName      = findViewById(R.id.tvMeterName);
        tvDate           = findViewById(R.id.tvDate);
        tvPreviousReading = findViewById(R.id.tvPreviousReading);
        tvCurrentReading  = findViewById(R.id.tvCurrentReading);
        tvConsumption    = findViewById(R.id.tvConsumption);
        tvPricePerKwh    = findViewById(R.id.tvPricePerKwh);
        tvTotalBill      = findViewById(R.id.tvTotalBill);
        btnBack          = findViewById(R.id.btnBack);

        // استقبل البيانات من MeterHistoryActivity
        String meterName     = getIntent().getStringExtra("meter_name");
        String date          = getIntent().getStringExtra("date");
        double previousReading = getIntent().getDoubleExtra("previous_reading", 0.0);
        double currentReading  = getIntent().getDoubleExtra("current_reading", 0.0);
        double consumption   = getIntent().getDoubleExtra("consumption", 0.0);
        double pricePerKwh   = getIntent().getDoubleExtra("price_per_kwh", 0.0);
        double totalBill     = getIntent().getDoubleExtra("total_bill", 0.0);

        // عرض البيانات
        tvMeterName.setText(meterName);
        tvDate.setText(date);
        tvPreviousReading.setText(String.valueOf(previousReading));
        tvCurrentReading.setText(String.valueOf(currentReading));
        tvConsumption.setText(consumption + " kWh");
        tvPricePerKwh.setText(pricePerKwh + " ₪");
        tvTotalBill.setText(totalBill + " ₪");

        // زر رجوع
        btnBack.setOnClickListener(v -> finish());
    }
}
