package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SensorEstimator extends AppCompatActivity {
    Button walk;
    Button run;
    Button sit_down;
    Button stand_up;
    Button put;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_estimator);

        walk = findViewById(R.id.Walk);
        run = findViewById(R.id.Run);
        sit_down = findViewById(R.id.SitDown);
        stand_up = findViewById(R.id.StandUp);
        put = findViewById(R.id.Put);

        walk.setOnClickListener(actClickListener);
        run.setOnClickListener(actClickListener);
        sit_down.setOnClickListener(actClickListener);
        stand_up.setOnClickListener(actClickListener);
        put.setOnClickListener(actClickListener);
    }

    private Button.OnClickListener actClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.Walk:
                    intent = new Intent(getApplicationContext(), Walk.class);
                    startActivity(intent);
                    break;

                case R.id.Run:
                    intent = new Intent(getApplicationContext(), Run.class);
                    startActivity(intent);
                    break;

                case R.id.SitDown:
                    intent = new Intent(getApplicationContext(), SitDown.class);
                    startActivity(intent);
                    break;

                case R.id.StandUp:
                    intent = new Intent(getApplicationContext(), StandUp.class);
                    startActivity(intent);
                    break;

                case R.id.Put:
                    intent = new Intent(getApplicationContext(), Put.class);
                    startActivity(intent);
                    break;
            }

        }
    };
}
