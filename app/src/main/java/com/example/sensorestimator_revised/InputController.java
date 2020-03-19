package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputController extends AppCompatActivity {
    EditText input_case;
    EditText input_exp_cnt;
    EditText input_exp_delay;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_controller);

        input_case = findViewById(R.id.input_case);
        input_exp_cnt = findViewById(R.id.input_exp_cnt);
        input_exp_delay = findViewById(R.id.input_exp_delay);

        ok = findViewById(R.id.input_OK);
        ok.setOnClickListener(actOK);
    }

    private Button.OnClickListener actOK = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
        }
    };
}

