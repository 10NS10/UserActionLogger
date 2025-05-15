package com.example.useractionlogger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LogActivity extends AppCompatActivity {
    private ListView logListView;
    private DatabaseHelper dbHelper;
    private ArrayList<String> logList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logListView = findViewById(R.id.log_list);
        dbHelper = new DatabaseHelper(this);
        logList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logList);
        logListView.setAdapter(adapter);

        loadLogs();
    }

    private void loadLogs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM logs", null);
        if (cursor.moveToFirst()) {
            do {
                String action = cursor.getString(cursor.getColumnIndex("action"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                logList.add(action + " в " + timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        dbHelper.addLog("Пользователь просмотрел логи", time);
        loadLogs();
    }
}