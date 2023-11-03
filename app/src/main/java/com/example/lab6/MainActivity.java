package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] baitap = {"Bài 1", "Bài 2"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addEvents();
    }
    private void addEvents() {
        autoCompleteTextView = findViewById(R.id.autoComplete_tv);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_baitap, baitap);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int i, long l) {
                String baitap = adpterView.getItemAtPosition(i).toString();

                switch (baitap) {
                    case "Bài 1":
                        startActivity(new Intent(MainActivity.this, Bai1.class));
                        break;
                    case "Bài 2":
                        startActivity(new Intent(MainActivity.this, Bai2.class));
                        break;

                }
            }
        });
    }
}