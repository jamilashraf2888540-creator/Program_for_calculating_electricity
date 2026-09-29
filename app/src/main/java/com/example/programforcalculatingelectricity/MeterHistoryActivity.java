package com.example.programforcalculatingelectricity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class MeterHistoryActivity extends AppCompatActivity {

    TextView tvMeterName, tvReadingCount;
    RecyclerView recyclerReadings;
    MaterialButton btnAddReading;
    ImageButton btnBack;
    Database db;

    long meterId;
    String meterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_history);
        db = new Database(this);

        meterId   = getIntent().getLongExtra("meter_id", -1);
        meterName = getIntent().getStringExtra("meter_name");

        tvMeterName      = findViewById(R.id.tvMeterName);
        tvReadingCount   = findViewById(R.id.tvReadingCount);
        recyclerReadings = findViewById(R.id.recyclerReadings);
        btnAddReading    = findViewById(R.id.btnAddReading);
        btnBack          = findViewById(R.id.btnBack);

        tvMeterName.setText(meterName);
        recyclerReadings.setLayoutManager(new LinearLayoutManager(this));

        // زر رجوع
        btnBack.setOnClickListener(v -> finish());

        // زر إضافة قراءة جديدة
        btnAddReading.setOnClickListener(v -> {
            Intent intent = new Intent(MeterHistoryActivity.this, add_reading.class);
            intent.putExtra("meter_id",   meterId);
            intent.putExtra("meter_name", meterName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReadings();
    }

    void loadReadings() {
        Cursor cursor = db.getReadingsByMeter(meterId);
        int count = cursor.getCount();
        tvReadingCount.setText(count + " قراءات");

        recyclerReadings.setAdapter(new ReadingAdapter(this, cursor, meterName));
    }

    // ===== Adapter =====
    static class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ViewHolder> {

        Context context;
        Cursor cursor;
        String meterName;

        ReadingAdapter(Context context, Cursor cursor, String meterName) {
            this.context   = context;
            this.cursor    = cursor;
            this.meterName = meterName;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_reading, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void  onBindViewHolder(ViewHolder holder, int position) {
            cursor.moveToPosition(position);

            String date          = cursor.getString(cursor.getColumnIndexOrThrow(Database.COL_DATE));
            double previousReading = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_PREVIOUS_READING));
            double currentReading  = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_CURRENT_READING));
            double consumption   = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_CONSUMPTION));
            double pricePerKwh   = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_PRICE_PER_KWH));
            double totalBill     = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_TOTAL_BILL));

            holder.tvDate.setText(date);
            holder.tvConsumption.setText(consumption + " kWh");
            holder.tvBill.setText(totalBill + " ₪");

            // لما يضغط على القراءة — يفتح تفاصيلها
            holder.itemView.setOnClickListener(v -> {
                cursor.moveToPosition(holder.getAdapterPosition());

                Intent intent = new Intent(context, ReadingDetailActivity.class);
                intent.putExtra("meter_name", meterName);
                intent.putExtra("date", cursor.getString(cursor.getColumnIndexOrThrow(Database.COL_DATE)));
                intent.putExtra("previous_reading", cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_PREVIOUS_READING)));
                intent.putExtra("current_reading", cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_CURRENT_READING)));
                intent.putExtra("consumption", cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_CONSUMPTION)));
                intent.putExtra("price_per_kwh", cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_PRICE_PER_KWH)));
                intent.putExtra("total_bill", cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COL_TOTAL_BILL)));

                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return cursor.getCount(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate, tvConsumption, tvBill;
            ViewHolder(View v) {
                super(v);
                tvDate        = v.findViewById(R.id.tvDate);
                tvConsumption = v.findViewById(R.id.tvConsumption);
                tvBill        = v.findViewById(R.id.tvBill);
            }
        }
    }
}