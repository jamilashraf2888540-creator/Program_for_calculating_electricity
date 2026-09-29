package com.example.programforcalculatingelectricity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class reading_result extends AppCompatActivity {

    TextView tvPreviousReading, tvCurrentReading, tvConsumption,
            tvPricePerKwh, tvTotalBill, tvMeterNameSubtitle;
    Button btnBackToMeters, btnAddAnotherReading;
    ImageButton btnBack;

    long meterId;
    String meterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_result);
        initViews();

        // استقبل البيانات
        Intent intent    = getIntent();
        meterId          = intent.getLongExtra("meter_id", -1);
        meterName        = intent.getStringExtra("meter_name");
        double prev      = intent.getDoubleExtra("previous_reading", 0.0);
        double curr      = intent.getDoubleExtra("current_reading", 0.0);
        double consump   = intent.getDoubleExtra("consumption", 0.0);
        double price     = intent.getDoubleExtra("price_per_kwh", 0.0);
        double totalBill = intent.getDoubleExtra("total_bill", 0.0);

        // اعرض البيانات
        tvMeterNameSubtitle.setText(meterName);
        tvPreviousReading.setText(prev + " kWh");
        tvCurrentReading.setText(curr + " kWh");
        tvConsumption.setText(consump + " kWh");
        tvPricePerKwh.setText(price + " ₪");
        tvTotalBill.setText(totalBill + " ₪");

        // زر الرجوع للعدادات
        btnBackToMeters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(reading_result.this, MeterListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // امسح كل الشاشات فوقها
                startActivity(i);
            }
        });

        // زر إضافة قراءة ثانية لنفس العداد
        btnAddAnotherReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(reading_result.this, add_reading.class);
                i.putExtra("meter_id",   meterId);
                i.putExtra("meter_name", meterName);
                startActivity(i);
                finish(); // أغلق شاشة النتيجة الحالية
            }
        });

        // زر السهم للرجوع
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initViews() {
        tvMeterNameSubtitle = findViewById(R.id.tvMeterNameSubtitle);
        tvPreviousReading   = findViewById(R.id.tvPreviousReading);
        tvCurrentReading    = findViewById(R.id.tvCurrentReading);
        tvConsumption       = findViewById(R.id.tvConsumption);
        tvPricePerKwh       = findViewById(R.id.tvPricePerKwh);
        tvTotalBill         = findViewById(R.id.tvTotalBill);
        btnBackToMeters     = findViewById(R.id.btnBackToMeters);
        btnAddAnotherReading = findViewById(R.id.btnAddAnotherReading);
        btnBack             = findViewById(R.id.btnBack);
    }
}