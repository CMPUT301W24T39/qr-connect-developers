package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class SelectEventPage extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_existing_event_qr_code);

        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        EventAdapterForQRCodeGenerating adapter = new EventAdapterForQRCodeGenerating(this, events);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) parent.getItemAtPosition(position);
                Event newEvent = events.get(events.size() - 1);
                newEvent.setQRCodeImage(selectedEvent.getQRCodeImage());
            }
        });

    }


}
