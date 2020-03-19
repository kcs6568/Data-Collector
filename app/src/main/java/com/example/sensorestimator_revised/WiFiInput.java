package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WiFiInput extends AppCompatActivity {
    Button btn_wifi_ok;
    EditText edit_wifi_room;
    EditText edit_wifi_case;
    EditText edit_wifi_count;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_input);

        initScreen();
    }

    private void initScreen() {
        btn_wifi_ok = findViewById(R.id.wifi_input_OK);
        edit_wifi_room = findViewById(R.id.edit_wifi_room_number);
        edit_wifi_case = findViewById(R.id.edit_case_number);
        edit_wifi_count = findViewById(R.id.edit_wifi_count);

        btn_wifi_ok.setOnClickListener(WiFiOkListener);
    }

    private Button.OnClickListener WiFiOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.wifi_input_OK){
                intent = new Intent(getApplicationContext(), WiFiScanner.class);
                intent.putExtra("Room_Number", edit_wifi_room.getText().toString());
                intent.putExtra("Case_Number", edit_wifi_case.getText().toString());
                intent.putExtra("Count_Number", edit_wifi_count.getText().toString());

                startActivity(intent);
            }
        }
    };
}
