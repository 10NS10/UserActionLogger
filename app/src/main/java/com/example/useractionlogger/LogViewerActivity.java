package com.example.useractionlogger;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogViewerActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_viewer);

        listView = findViewById(R.id.log_list_view);
        Button refreshButton = findViewById(R.id.refresh_button);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logList);
        listView.setAdapter(adapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLogs();
            }
        });

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
}