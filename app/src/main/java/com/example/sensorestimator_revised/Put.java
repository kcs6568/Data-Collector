package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class Put extends AppCompatActivity {
    Button bag, hand;
    Button ok;

    EditText input_case;
    EditText input_exp_cnt;
    EditText input_exp_delay;

    LayoutInflater inflater = null;

    private final static String Main_Action = "Put";

    private final static String Bag = "Bag";
    private final static String Hand = "Hand";

    private final static int LABEL_BAG = 51;
    private final static int LABEL_HAND = 52;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);
        this.InitializeView();
    }

    public void InitializeView() {
        bag = findViewById(R.id.Bag);
        hand = findViewById(R.id.Hand);

        bag.setOnClickListener(detailClickListener);
        hand.setOnClickListener(detailClickListener);
    }

    private Button.OnClickListener detailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Bag:
                    control_OK(LABEL_BAG);
                    break;

                case R.id.Hand:
                    control_OK(LABEL_HAND);
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
        FrameLayout frame = findViewById(R.id.IC_Space);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_input_controller, frame, false);
        frame.addView(v);

        return v;
    }

    private void OK_Processor(final int btnNumber){
        input_case = findViewById(R.id.input_case);
        input_exp_cnt = findViewById(R.id.input_exp_cnt);
        input_exp_delay = findViewById(R.id.input_exp_delay);
        Intent intent = new Intent(getApplicationContext(), ExperimentController.class);
        intent.putExtra("main_action", Main_Action);

        switch (btnNumber) {
            case LABEL_BAG:
                intent.putExtra("detail_action", Bag);
                intent.putExtra("label", Integer.toString(LABEL_BAG));
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
        }
        startActivity(intent);
    }
}
