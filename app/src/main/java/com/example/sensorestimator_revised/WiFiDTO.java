package com.example.sensorestimator_revised;


import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class WiFiDTO {
    private String ssid;
    private String bssid;
    private int rssi;
    private long time_stamp;
    private  String input_case;

    public WiFiDTO(){
        this.ssid = "";
        this.bssid = "";
        this.rssi = 0;
        this.time_stamp = 0;
        this.input_case = "";
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getInput_case() {
        return input_case;
    }

    public void setInput_case(String input_case) {
        this.input_case = input_case;
    }

    JSONObject jsonObject = new JSONObject();

    void setJsonObject(String _ssid, String _bssid, int _rssi, long _time_stamp, String _case) {
        try {
           this.jsonObject.put("ssid", _ssid);
           this.jsonObject.put("mac", _bssid);
           this.jsonObject.put("rssi", _rssi);
           this.jsonObject.put("time_stamp", _time_stamp);
           this.jsonObject.put("case_number", _case);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject getJsonObject() { return jsonObject; }

    @NonNull
    @Override
    public String toString() {
        return ssid + "," + bssid + "," + rssi + "," + time_stamp + "," + input_case;
    }
}
