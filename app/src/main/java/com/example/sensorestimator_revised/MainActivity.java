package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button Sensor_Estimator;
    private Button WiFi_scanner;
    private Button BLE_scanner;

    final static int BTN_SE = 1;
    final static int BTN_WiFi = 2;
    final static int BTN_BLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateView();
        defaultButtonColor();
    }

    private Button.OnClickListener ModeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (returnButtonType(v)) {
                case BTN_SE:
                    intent = new Intent(getApplicationContext(), SensorEstimator.class);
                    startActivity(intent);
                    break;

                case BTN_WiFi:
                    intent = new Intent(getApplicationContext(), WiFiInput.class);
                    startActivity(intent);
                    break;

                case BTN_BLE:

                    break;
            }
        }
    };

    private void initiateView() {
        Sensor_Estimator = findViewById(R.id.SensorEstimator);
        WiFi_scanner = findViewById(R.id.WiFiScanner);
        BLE_scanner = findViewById(R.id.BLEScanner);

        Sensor_Estimator.setOnClickListener(ModeClickListener);
        WiFi_scanner.setOnClickListener(ModeClickListener);
        BLE_scanner.setOnClickListener(ModeClickListener);
    }

    private void defaultButtonColor() {
        Sensor_Estimator.setBackgroundColor(Color.GRAY);
        Sensor_Estimator.setTextColor(Color.WHITE);

        WiFi_scanner.setBackgroundColor(Color.GRAY);
        WiFi_scanner.setTextColor(Color.WHITE);

        BLE_scanner.setBackgroundColor(Color.GRAY);
        BLE_scanner.setTextColor(Color.WHITE);
    }

    private int returnButtonType(View v) {
        if (v == Sensor_Estimator) return BTN_SE;
        else if (v == WiFi_scanner) return BTN_WiFi;
        else if (v == BLE_scanner) return BTN_BLE;
        else return 0;
    }
}
