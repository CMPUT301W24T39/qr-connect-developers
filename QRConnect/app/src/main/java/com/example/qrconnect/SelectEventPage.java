package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.events;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SelectEventPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_existing_event_qr_code_page);

        Spinner spinner = findViewById(R.id.spinner_for_event);
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, events);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Event selectedEvent = (Event) parentView.getItemAtPosition(position);
                events.get(position).setQRCodeImage(selectedEvent.getQRCodeImage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
}
