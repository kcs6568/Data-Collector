package com.example.sensorestimator_revised;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WiFiScanner extends AppCompatActivity {
    private static final String TAG = "WIFIScanner";
    TextView textStatus;

    Intent intent = null;
    String roomNum = "";
    String caseNum = "";
    int countNum = 0;
    int cur_cnt = 0;
    long time_stamp = 0;

    WifiManager wifiManager;
    List<ScanResult> mScanResult;

    NetworkTask networkTask = null;
    WiFiDTO wifiDTO;
    ArrayList<WiFiDTO> wifiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_scanner);

        initScreen();
    }

    private void initScreen(){
        Button btn_start_scan = findViewById(R.id.btn_start_scan);
        Button btn_stop_scan = findViewById(R.id.btn_stop_scan);
        Button btn_make_file = findViewById(R.id.btn_wifi_make_file);
        Button btn_send_data = findViewById(R.id.btn_wifi_send_data);
        Button btn_go_home = findViewById(R.id.btn_wifi_go_home);
        textStatus = findViewById(R.id.wifi_scan_status);

        btn_start_scan.setOnClickListener(btnWiFiClickListener);
        btn_stop_scan.setOnClickListener(btnWiFiClickListener);
        btn_make_file.setOnClickListener(btnWiFiClickListener);
        btn_send_data.setOnClickListener(btnWiFiClickListener);
        btn_go_home.setOnClickListener(btnWiFiClickListener);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "Setup WifiManager getSystemService");

        intent = getIntent();
        roomNum = intent.getStringExtra("Room_Number");
        caseNum = intent.getStringExtra("Case_Number");
        countNum = Integer.parseInt(intent.getStringExtra("Count_Number"));


        textStatus.setText("Room " + roomNum + "(Case : " + caseNum + ")"
                + " scan count is \t" + cur_cnt + " time \n");
    }

    private Button.OnClickListener btnWiFiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start_scan:
                    try {
                        Log.d(TAG, "-----WIFI Scan Start-----");
                        printToast("WIFI Scan Start!!");
                        scanWIfI();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.btn_stop_scan:
                    try {
                        Log.d(TAG, "-----WIFI Scan Stop-----");
                        printToast("WIFI Scan Stop!!");
                        unregisterReceiver(wifiReceiver);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.btn_wifi_make_file:{
                    makeFile();
                    printToast("Complete saving data in inner storage!");
                    break;
                }

                case R.id.btn_wifi_send_data:{
                    networkTask = new NetworkTask();
                    networkTask.execute();
                    printToast("Complete sending data to server!");
                    break;
                }

                case R.id.btn_wifi_go_home: {
                    if (mScanResult != null) mScanResult.clear();
                    wifiList.clear();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    printToast("Go to Home!");
                    break;
                }
            }
        }
    };

    public void printToast(String messageToast){
        Toast.makeText(this, messageToast, Toast.LENGTH_SHORT).show();
    }

    private void scanWIfI() {
        cur_cnt = 0;
        mScanResult = null;
        wifiList.clear();
        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(wifiReceiver, filter);
        wifiManager.startScan();

        Toast.makeText(this, "Scanning WiFi...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "initWIFIScan()");
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                getWIFIScanResult();
                wifiManager.startScan();
            } else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGE_ACTION"));
            }
        }
    };

    public void getWIFIScanResult(){
        mScanResult = wifiManager.getScanResults(); //Scan Result

        if(cur_cnt == countNum){
            unregisterReceiver(wifiReceiver);
            printToast("Counting is over.");
        } else {
            cur_cnt++;
            textStatus.setText("Room " + roomNum + "(Case : " + caseNum + ")"
                    + " scan count is \t" + cur_cnt + " time" + "\nCurrent Time : " + getCurrentTime());

            textStatus.append("\n===========================================\n");

            for(int i=0; i<mScanResult.size(); i++){
                wifiDTO = new WiFiDTO();
                ScanResult result = mScanResult.get(i);
                wifiDTO.setSsid(result.SSID);
                wifiDTO.setBssid(result.BSSID);
                wifiDTO.setRssi(result.level);
                wifiDTO.setTime_stamp(time_stamp);
                wifiDTO.setInput_case(caseNum);

                textStatus.append("(" + (i+1) + ")---------------------------------------\n");
                textStatus.append("\t SSID : " + wifiDTO.getSsid() +
                        "\n\t MAC : " + wifiDTO.getBssid() + "\n\t RSSI : " + wifiDTO.getRssi() + "\n");
                wifiList.add(wifiDTO);
            }
            textStatus.append("===========================================\n");
        }
    }

    private String getCurrentTime(){
        time_stamp = System.currentTimeMillis();
        Date date = new Date(time_stamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault());
        return sdf.format(date);
    }

    private void makeFile() {
        String dir_wifi = "WiFi";
        try {
            for (int i = 0; i < wifiList.size(); i++) {
                String file = getExternalFilesDir(null) +
                        "/" + dir_wifi + "/" + wifiList.get(i).getSsid() + "---"
                        + wifiList.get(i).getBssid() +"_" + caseNum + ".csv";

                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println(wifiList.get(i).toString());
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printDTO(){
        for(int i=0; i<wifiList.size(); i++){
            textStatus.append("Array Size : " + wifiList.size());
            textStatus.append("\n(" + (i+1) + ")---------------------------------------\n");
            textStatus.append("\t SSID : " + wifiList.get(i).getSsid() +
                   "\n\t MAC : " + wifiList.get(i).getBssid() + "\n\t RSSI : " + wifiList.get(i).getRssi() + "\n");
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, Integer> {
        int result;

        @Override
        protected Integer doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            String url = "http://203.255.81.72:9000/wifi";

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
                for (int i = 0; i < wifiList.size(); i++) {
                    WiFiDTO dtoObj = new WiFiDTO();

                    dtoObj.setSsid(wifiList.get(i).getSsid());
                    dtoObj.setBssid(wifiList.get(i).getBssid());
                    dtoObj.setRssi(wifiList.get(i).getRssi());
                    dtoObj.setTime_stamp(wifiList.get(i).getTime_stamp());
                    dtoObj.setInput_case(wifiList.get(i).getInput_case());
                    dtoObj.setJsonObject(dtoObj.getSsid(), dtoObj.getBssid(),
                            dtoObj.getRssi(), dtoObj.getTime_stamp(), dtoObj.getInput_case());

                    jsonArray.put(i, dtoObj.getJsonObject());
                    Log.d("Get " + i + " JsonData : ", dtoObj.getJsonObject().toString());
                }

                Log.d("JsonArray Data : ", jsonArray.toString());
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(jsonArray.toString().getBytes());
                dos.close();

                result = conn.getResponseCode();


            } catch (MalformedURLException e) {
                System.out.println("Wrong URL");
            } catch (SocketTimeoutException e) {
                System.out.println("Time Out");
            } catch (IOException e) {
                Log.e("Network Problem", "Network Error");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("networkerr", exceptionAsStrting);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


//    void scanWiFiList(Context context) {
//        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                final String action = intent.getAction();
//                if(action.equals(WifiManager.SCAN_ScanResult_AVAILABLE_ACTION)){
//                    scanData = wifiManager.getScanScanResult();
//                    Toast.makeText(getApplicationContext(), scanData.get(0).SSID, Toast.LENGTH_SHORT).show();
//                } else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
//                    sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
//                }
//                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_ScanResult_UPDATED, false);
//                if (success) {
//                    scanSuccess();
//                } else {
//                    scanFailure();
//                }
//            }
//        };
//    }

//    private void scanSuccess(){
//        List<ScanResult> ScanResult = wifiManager.getScanScanResult();
//    }
//
//    private void scanFailure(){
//
//    }


}
