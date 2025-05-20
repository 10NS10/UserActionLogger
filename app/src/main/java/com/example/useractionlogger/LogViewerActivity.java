package com.example.useractionlogger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogViewerActivity extends AppCompatActivity {
    private ListView listView;
    private LogAdapter adapter;
    private List<String> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_viewer);

        listView = findViewById(R.id.log_list_view);
        Button updateButton = findViewById(R.id.update_button);
        Button settingsButton = findViewById(R.id.settings_button);
        Button sortButton = findViewById(R.id.sort_button);

        adapter = new LogAdapter(logList);
        listView.setAdapter(adapter);

        updateButton.setOnClickListener(v -> loadLogs());
        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        sortButton.setOnClickListener(v -> startActivity(new Intent(this, SortActivity.class)));

        loadLogs();
    }

    private void loadLogs() {
        logList.clear();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logList.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            logList.add("Ошибка при чтении логов: " + e.getMessage());
        }
        adapter.notifyDataSetChanged();
    }

    private class LogAdapter extends ArrayAdapter<String> {
        public LogAdapter(List<String> logs) {
            super(LogViewerActivity.this, 0, logs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.log_item, parent, false);
            }
            TextView logText = convertView.findViewById(R.id.log_text);
            logText.setText(getItem(position));
            return convertView;
        }
    }
}