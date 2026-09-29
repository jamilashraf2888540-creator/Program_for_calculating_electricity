package com.example.programforcalculatingelectricity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeterAdapter extends RecyclerView.Adapter<MeterAdapter.MeterViewHolder> {

    Context context;
    Cursor cursor;
    OnMeterClickListener listener;

    // Interface عشان MeterListActivity يعرف أي عداد ضغط عليه المستخدم
    public interface OnMeterClickListener {
        void onMeterClick(long meterId, String meterName);
    }

    public MeterAdapter(Context context, Cursor cursor, OnMeterClickListener listener) {
        this.context  = context;
        this.cursor   = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MeterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meter, parent, false);
        return new MeterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterViewHolder holder, int position) {
        // روح للصف الصح في الـ Cursor
        cursor.moveToPosition(position);

        long   meterId   = cursor.getLong(cursor.getColumnIndexOrThrow(Database.COL_METER_ID));
        String meterName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COL_METER_NAME));

        holder.tvMeterName.setText(meterName);

        // اجلب آخر قراءة لهاد العداد
        Database db = new Database(context);
        Cursor lastReading = db.getLastReading(meterId);
        if (lastReading.moveToFirst()) {
            double lastCurrent = lastReading.getDouble(lastReading.getColumnIndexOrThrow(Database.COL_CURRENT_READING));
            double lastBill    = lastReading.getDouble(lastReading.getColumnIndexOrThrow(Database.COL_TOTAL_BILL));
            String lastDate    = lastReading.getString(lastReading.getColumnIndexOrThrow(Database.COL_DATE));
            holder.tvLastReading.setText(lastCurrent + " kWh");
            holder.tvLastCost.setText(lastBill + " ₪");
            holder.tvLastDate.setText(lastDate);
        } else {
            // لو ما عنده قراءات بعد
            holder.tvLastReading.setText("لا توجد قراءات");
            holder.tvLastCost.setText("");
            holder.tvLastDate.setText("");
        }
        lastReading.close();
        db.close();

        // لما يضغط على الكارد
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMeterClick(meterId, meterName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // ViewHolder — بيمسك الـ Views تبع كل كارد
    static class MeterViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeterName, tvLastReading, tvLastCost, tvLastDate;

        public MeterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMeterName   = itemView.findViewById(R.id.tvMeterName);
            tvLastReading = itemView.findViewById(R.id.tvLastReading);
            tvLastCost    = itemView.findViewById(R.id.tvLastCost);
            tvLastDate    = itemView.findViewById(R.id.tvLastDate);
        }
    }
}
