package com.example.sensorestimator_revised;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class Walk extends AppCompatActivity {
    Button pocket, hand, use;
    Button ok;

    EditText input_case;
    EditText input_exp_cnt;
    EditText input_exp_delay;

    private final static String Main_Action = "Walk";
    private final static String Pocket = "Pocket";
    private final static String Hand = "Hand";
    private final static String Use = "Use";

    private final static int LABEL_POCKET = 11;
    private final static int LABEL_HAND = 12;
    private final static int LABEL_USE = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        this.InitializeView();
    }

    public void InitializeView() {
        pocket = findViewById(R.id.Pocket);
        hand = findViewById(R.id.Hand);
        use = findViewById(R.id.Use);

        pocket.setOnClickListener(detailClickListener);
        hand.setOnClickListener(detailClickListener);
        use.setOnClickListener(detailClickListener);
    }

    private Button.OnClickListener detailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Pocket:
                    control_OK(LABEL_POCKET);
                    break;

                case R.id.Hand:
                    control_OK(LABEL_HAND);
                    break;

                case R.id.Use:
                    control_OK(LABEL_USE);
                    break;
            }
        }
    };

    private void control_OK(final int btnNumber) {
        ok = addInputController().findViewById(R.id.input_OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OK_Processor(btnNumber);
            }
        });
    }

    public View addInputController() {
        FrameLayout frame_ic = findViewById(R.id.IC_Space);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_input_controller, frame_ic, false);
        frame_ic.addView(v);
        return v;
    }

    private void OK_Processor(int btnNumber) {
        input_case = findViewById(R.id.input_case);
        input_exp_cnt = findViewById(R.id.input_exp_cnt);
        input_exp_delay = findViewById(R.id.input_exp_delay);
        Intent intent = new Intent(getApplicationContext(), ExperimentController.class);
        intent.putExtra("main_action", Main_Action);

        switch (btnNumber) {
            case LABEL_POCKET:
                intent.putExtra("detail_action", Pocket);
                intent.putExtra("label", Integer.toString(LABEL_POCKET));
                intent.putExtra("input_case", input_case.getText().toString());
                intent.putExtra("exp_count", input_exp_cnt.getText().toString());
                intent.putExtra("exp_delay", input_exp_delay.getText().toString());
                break;

            case LABEL_HAND:
                intent.putExtra("detail_action", Hand);
                intent.putExtra("label", Integer.toString(LABEL_HAND));
                intent.putExtra("input_case", input_case.getText().toString());
                intent.putExtra("exp_count", input_exp_cnt.getText().toString());
                intent.putExtra("exp_delay", input_exp_delay.getText().toString());
                break;

            case LABEL_USE:
                intent.putExtra("detail_action",  Use);
                intent.putExtra("label", Integer.toString(LABEL_USE));
                intent.putExtra("input_case", input_case.getText().toString());
                intent.putExtra("exp_count", input_exp_cnt.getText().toString());
                intent.putExtra("exp_delay", input_exp_delay.getText().toString());
                break;
        }
        startActivity(intent);
    }

    //EditText에 입력된 값이 숫자인지 판별하는 함수
    public boolean isInteger(String exp_cnt) {
        try {
            Integer.parseInt(exp_cnt);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Number Exception : " + e);
            return false;
        }
    }
}