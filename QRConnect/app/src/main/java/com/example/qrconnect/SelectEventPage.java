package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.eventDataList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SelectEventPage extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;

    private ImageButton backButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_existing_event_qr_code);

        backButton2 = findViewById(R.id.arrow_back_2);

        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        EventAdapter adapter = new EventAdapter(this, eventDataList);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) parent.getItemAtPosition(position);
                Event newEvent = eventDataList.get(eventDataList.size() - 1);
                newEvent.setQRCodeImage(selectedEvent.getQRCodeImage());
            }
        });


        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }





}
