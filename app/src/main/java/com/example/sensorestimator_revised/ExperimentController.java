package com.example.sensorestimator_revised;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class ExperimentController extends AppCompatActivity {
    private SensorManager mSensorManager;

    SensorEventListener mAccLis;
    SensorEventListener mGyroLis;
    SensorEventListener mOriLis;
    SensorEventListener mMagLis;

    Sensor mAccelometerSensor;
    Sensor mGyroscopeSensor;
    Sensor mOrientationSensor;
    Sensor mMagneticSensor;

    TextView title;
    TextView acc_x, acc_y, acc_z;
    TextView gyro_x, gyro_y, gyro_z;
    TextView ori_x, ori_y, ori_z;
    TextView mag_x, mag_y, mag_z;
    TextView esti_number;
    TextView info_act;
    TextView info_case;
    TextView info_cnt;

    Button btn_Start, btn_Stop;
    Button btn_GoToHome;
    Button btn_makeFile;
    Button btn_SendData;
    Button btn_RemoveData;

    private final static int BTN_START = 1;
    private final static int BTN_STOP = 2;
    private final static int BTN_GoHOME = 3;
    private final static int BTN_MakeFile = 4;
    private final static int BTN_SendData = 5;
    private final static int BTN_RemoveData = 6;

    private final static int MESSAGE_TIMER_START = 100;
    private final static int MESSAGE_TIMER_REPEAT = 101;
    private final static int MESSAGE_TIMER_STOP = 102;

    private static int esti_count;
    private static String main_action;
    private static String detail_action;
    private static String label;
    private static String input_case;
    private static String exp_count;
    private static String exp_delay;

    Intent intent;

    TimerHandler timerHandler = new TimerHandler();
    Handler mHandelr = new Handler();

    NetworkTask networkTask = null;
    SensorData SensorData;
    ArrayList<SensorData> sensorList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_controller);
        InitSensorView();
    }

    public void InitSensorView() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mAccLis = new AccelometerListener();
        mGyroLis = new GyroscopeListener();
        mOriLis = new OrientationListener();
        mMagLis = new MagneticListener();

        title = findViewById(R.id.title);
        acc_x = findViewById(R.id.acc_x);
        acc_y = findViewById(R.id.acc_y);
        acc_z = findViewById(R.id.acc_z);
        gyro_x = findViewById(R.id.gyro_x);
        gyro_y = findViewById(R.id.gyro_y);
        gyro_z =  findViewById(R.id.gyro_z);
        ori_x = findViewById(R.id.ori_x);
        ori_y = findViewById(R.id.ori_y);
        ori_z = findViewById(R.id.ori_z);
        mag_x = findViewById(R.id.mag_x);
        mag_y = findViewById(R.id.mag_y);
        mag_z = findViewById(R.id.mag_z);
        esti_number = findViewById(R.id.esti_number);
        info_act = findViewById(R.id.info_act);
        info_case = findViewById(R.id.info_case);
        info_cnt = findViewById(R.id.info_cnt);

        btn_Start = findViewById(R.id.btnStart);
        btn_Stop = findViewById(R.id.btnStop);
        btn_GoToHome = findViewById(R.id.btnGoToHome);
        btn_makeFile = findViewById(R.id.makeFile);
        btn_SendData = findViewById(R.id.btnSendToServer);
        btn_RemoveData = findViewById(R.id.btnRemoveData);

        btn_Start.setOnClickListener(mClickListener);
        btn_Stop.setOnClickListener(mClickListener);
        btn_GoToHome.setOnClickListener(mClickListener);
        btn_makeFile.setOnClickListener(mClickListener);
        btn_SendData.setOnClickListener(mClickListener);
        btn_RemoveData.setOnClickListener(mClickListener);

        intent = getIntent();
        main_action = intent.getStringExtra("main_action");
        detail_action = intent.getStringExtra("detail_action");
        label = intent.getStringExtra("label");
        input_case = intent.getStringExtra("input_case");
        exp_count = intent.getStringExtra("exp_count");
        exp_delay = intent.getStringExtra("exp_delay");

        info_act.setText(main_action + "&" + detail_action);
        info_case.setText(input_case);
        info_cnt.setText(exp_count);

        Log.d("Info_Act : ", info_act.getText().toString());
        Log.d("Info_Case : ", info_case.getText().toString());
        Log.d("Info_Count : ", info_cnt.getText().toString());
        
        /*익명클래스 예시
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */


    }

    //익명클래스 객체 선언
    private Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (returnButtonType(v)) {
                case BTN_START: {
                    ButtonHandler();
                    break;
                }

                case BTN_STOP: {
                    onPause();
                    timerHandler.sendEmptyMessage(MESSAGE_TIMER_STOP);
                    break;
                }

                case BTN_MakeFile: {
                    makeFile();
                    Toast.makeText(getApplicationContext(), "Complete saving data! : ["
                            + main_action + "&" + detail_action +"]", Toast.LENGTH_SHORT).show();
                    break;
                }

                case BTN_GoHOME: {
                    if(esti_count != 0){
                        timerHandler.sendEmptyMessage(MESSAGE_TIMER_STOP);
                        sensorList.clear();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Go to Home!", Toast.LENGTH_SHORT).show();
                    break;
                }

                case BTN_SendData: {
                    networkTask = new NetworkTask();
                    networkTask.execute();
                    Toast.makeText(getApplicationContext(),
                            "Complete sending data to server! : ["
                                    + main_action + "&" + detail_action + "]", Toast.LENGTH_SHORT).show();

                    break;
                }

                case BTN_RemoveData: {
                    sensorList.clear();
                    Toast.makeText(getApplicationContext(),
                            "Complete removing data list! : ["
                                    + main_action + "&" + detail_action + "]", Toast.LENGTH_SHORT).show();
                    break;
                }


            }
        }
    };

    private void makeFile() {
        String dir_sensor = "Sensor";
        try {
            String file = getExternalFilesDir(null) +
                    "/" + dir_sensor + "/" +
                    main_action + "&" + detail_action + "_" + input_case + ".csv";

            PrintWriter pw = new PrintWriter(file);

            for (int i = 0; i < sensorList.size(); i++) {
                pw.println(sensorList.get(i).toString() + "," + label);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ButtonHandler() {
        mHandelr.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSensorEstimation();
                timerHandler.sendEmptyMessage(MESSAGE_TIMER_START);
            }
        }, Long.valueOf(exp_delay)*1000);
    }

    private class TimerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MESSAGE_TIMER_START:
                    Log.d("TimerHandelr", "Timer Start");
                    esti_count = 0;
                    this.removeMessages(MESSAGE_TIMER_REPEAT);
                    this.sendEmptyMessage(MESSAGE_TIMER_REPEAT);
                    sensorList.clear();
                    Log.d("SensorList Size : ", String.valueOf(sensorList.size()));
                    break;

                case MESSAGE_TIMER_REPEAT:
                    Log.d("TimerHandelr", "Timer Repeat : " + esti_count);
                    this.sendEmptyMessageDelayed(MESSAGE_TIMER_REPEAT, 100);

                    if(esti_count == Integer.valueOf(exp_count)){
                        this.removeMessages(MESSAGE_TIMER_REPEAT);
                        Log.d("List Size : ", sensorList.size() + "");
                        onPause();
                    } else {
                        SensorData = new SensorData();
                        sensorList.add(SensorData);
                        esti_count++;
                    }
                    break;

                case MESSAGE_TIMER_STOP:
                    Log.d("TimerHandelr", "Timer Stop");
                    this.removeMessages(MESSAGE_TIMER_REPEAT);
                    break;
            }
        }
    }

    private void startSensorEstimation() {
        mSensorManager.registerListener(mAccLis, mAccelometerSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mGyroLis, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mOriLis, mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mMagLis, mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
        Log.i("Start Estimation", "Start Estimation");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Pause : ", "onPause()");
        mSensorManager.unregisterListener(mAccLis);
        mSensorManager.unregisterListener(mGyroLis);
        mSensorManager.unregisterListener(mOriLis);
        mSensorManager.unregisterListener(mMagLis);
        Log.d("Stop Estimation : ", "Stop Estimation");
    }

    private int returnButtonType(View v) {
        int isSystemRun;

        if (v == btn_Start) {
            isSystemRun = BTN_START;
        } else if (v == btn_Stop) {
            isSystemRun = BTN_STOP;
        } else if (v == btn_GoToHome) {
            isSystemRun = BTN_GoHOME;
        } else if (v == btn_makeFile) {
            isSystemRun = BTN_MakeFile;
        } else if (v == btn_SendData) {
            isSystemRun = BTN_SendData;
        } else isSystemRun = BTN_RemoveData;

        return isSystemRun;
    }

    private void printCount() {
        esti_number.setText(String.valueOf(esti_count));
    }

    public class NetworkTask extends AsyncTask<Void, Void, Integer> {
        int result;

        @Override
        protected Integer doInBackground(Void... params) {
            HttpURLConnection conn = null;
            String url = "http://[Your IP Address]";

            try {
                URL obj = new URL(url);
                conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Data_Type", "JSON");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");    //MIME 타입 설정. 이 설정을 통해 서버에서 http 요청에서 온 데이터의 타입 식별 가능
                conn.connect();

                JSONArray jsonArray = new JSONArray();
                for(int i=0; i<sensorList.size(); i++){
                    JsonData jsonObj = new JsonData();

                    jsonObj.setMain_action(main_action);
                    jsonObj.setDetail_action(detail_action);
                    jsonObj.setLabel(label);
                    jsonObj.setCase_number(input_case);
                    jsonObj.setAcc_x(sensorList.get(i).getAccValue_X());
                    jsonObj.setAcc_y(sensorList.get(i).getAccValue_Y());
                    jsonObj.setAcc_z(sensorList.get(i).getAccValue_Z());
                    jsonObj.setGyro_x(sensorList.get(i).getGyroValue_X());
                    jsonObj.setGyro_y(sensorList.get(i).getGyroValue_Y());
                    jsonObj.setGyro_z(sensorList.get(i).getGyroValue_Z());
                    jsonObj.setOri_x(sensorList.get(i).getOriValue_X());
                    jsonObj.setOri_y(sensorList.get(i).getOriValue_Y());
                    jsonObj.setOri_z(sensorList.get(i).getOriValue_Z());
                    jsonObj.setMag_x(sensorList.get(i).getMagneticValue_X());
                    jsonObj.setMag_y(sensorList.get(i).getMagneticValue_Y());
                    jsonObj.setMag_z(sensorList.get(i).getMagneticValue_Z());
                    jsonObj.setJsonObject(jsonObj.getMain_action(), jsonObj.getDetail_action(), jsonObj.getLabel(), jsonObj.getCase_number(),
                            jsonObj.getAcc_x(), jsonObj.getAcc_y(), jsonObj.getAcc_z(),
                            jsonObj.getGyro_x(), jsonObj.getGyro_y(), jsonObj.getGyro_z(),
                            jsonObj.getOri_x(), jsonObj.getOri_y(), jsonObj.getOri_z(),
                            jsonObj.getMag_x(), jsonObj.getMag_y(), jsonObj.getMag_z());
                    jsonArray.put(i, jsonObj.getJsonObject());
                    Log.d("Get " + i + " JsonData : ", jsonObj.getJsonObject().toString());
                }

                Log.d("JsonArray Data : ", jsonArray.toString());
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(jsonArray.toString().getBytes());
                dos.close();

                result = conn.getResponseCode();


            } catch (MalformedURLException e){
                System.out.println("Wrong URL");
            } catch (SocketTimeoutException e){
                System.out.println("Time Out");
            } catch (IOException e){
                Log.e("Network Problem", "Network Error");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("networkerr", exceptionAsStrting);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(conn != null){
                    conn.disconnect();
                }
            }

            return result;
        }

        //TODO AsyncTask 종료되는 시점 찾아서 구현 할 것
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == HttpURLConnection.HTTP_OK) {
                Log.d("상태 ---- ", "성공");
                Log.d("Network Status : ", networkTask.getStatus().toString());
            } else {
                Log.d("상태 ---- ", "실패");
                Log.d("로그", Integer.toString(result));
                Toast.makeText(getApplicationContext(), "Fail sending data to server! - "
                        + result + ": [" + main_action + "&" + detail_action + "]",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("isCancelled : ", networkTask.getStatus().toString());
        }
    }

    private class AccelometerListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            double accX = event.values[0];
            double accY = event.values[1];
            double accZ = event.values[2];

            acc_x.setText(String.valueOf(accX));
            acc_y.setText(String.valueOf(accY));
            acc_z.setText(String.valueOf(accZ));

            printCount();

            Log.d("LOG", "ACCELOMETER[X] : " + String.format("%.4f", event.values[0])
                    + " || ACCELOMETER[Y] : " + String.format("%.4f", event.values[1])
                    + " || ACCELOMETER[Z] : " + String.format("%.4f", event.values[2]));

            SensorData.setAccValueList(Double.toString(accX), Double.toString(accY), Double.toString(accZ));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class GyroscopeListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
                String gyroX = Double.toString(event.values[0]);
                String gyroY = Double.toString(event.values[1]);
                String gyroZ = Double.toString(event.values[2]);

                gyro_x.setText(gyroX);
                gyro_y.setText(gyroY);
                gyro_z.setText(gyroZ);

                Log.d("LOG", "GYROSCOPE [X] : " + String.format("%.4f", event.values[0])
                        + " || GYROSCOPE [Y] : " + String.format("%.4f", event.values[1])
                        + " || GYROSCOPE [Z] : " + String.format("%.4f", event.values[2]));

                SensorData.setGyroValueList(gyroX, gyroY, gyroZ);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class OrientationListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
                String oriX = Double.toString(event.values[0]);
                String oriY = Double.toString(event.values[1]);
                String oriZ = Double.toString(event.values[2]);

                ori_x.setText(oriX);
                ori_y.setText(oriY);
                ori_z.setText(oriZ);

                SensorData.setOriValueList(oriX, oriY, oriZ);

                Log.d("LOG", "ORIENTATION [X]" + String.format("%.4f", event.values[0])
                        + " || ORIENTATION [Y]:" + String.format("%.4f", event.values[1])
                        + " || ORIENTATION [Z]:" + String.format("%.4f", event.values[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class MagneticListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
                String magX = Double.toString(event.values[0]);
                String magY = Double.toString(event.values[1]);
                String magZ = Double.toString(event.values[2]);

                mag_x.setText(magX);
                mag_y.setText(magY);
                mag_z.setText(magZ);

                Log.d("LOG", "MAGNETIC [X]" + String.format("%.4f", event.values[0])
                        + " || MAGNETIC [Y]:" + String.format("%.4f", event.values[1])
                        + " || MAGNETIC [Z]:" + String.format("%.4f", event.values[2]));

                SensorData.setMagneticValueList(magX, magY, magZ);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}


