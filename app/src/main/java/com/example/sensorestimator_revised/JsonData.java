package com.example.sensorestimator_revised;

import org.json.JSONException;
import org.json.JSONObject;

class JsonData {
    private String main_action = null;
    private String detail_action = null;
    private String label = null;
    private String case_number = null;
    private String Acc_x = null;
    private String Acc_y = null;
    private String Acc_z = null;
    private String Gyro_x = null;
    private String Gyro_y = null;
    private String Gyro_z = null;
    private String Ori_x = null;
    private String Ori_y = null;
    private String Ori_z = null;
    private String Mag_x = null;
    private String Mag_y = null;
    private String Mag_z = null;

//    JsonData(String _main_action, String _file_name, String _label, String _case_number,
//             String _Acc_x, String _Acc_y, String _Acc_z,
//             String _Gyro_x, String _Gyro_y, String _Gyro_z,
//             String _Ori_x, String _Ori_y, String _Ori_z,
//             String _Mag_x, String _Mag_y, String _Mag_z) {
//        this.main_action = _main_action;
//        this.file_name = _file_name;
//        this.label = _label;
//        this.case_number = _case_number;
//        this.Acc_x = _Acc_x;
//        this.Acc_y = _Acc_y;
//        this.Acc_z = _Acc_z;
//        this.Gyro_x = _Gyro_x;
//        this.Gyro_y = _Gyro_y;
//        this.Gyro_z = _Gyro_z;
//        this.Ori_x = _Ori_x;
//        this.Ori_y = _Ori_y;
//        this.Ori_z = _Ori_z;
//        this.Mag_x = _Mag_x;
//        this.Mag_y = _Mag_y;
//        this.Mag_z = _Mag_z;
//    }


    public String getMain_action() { return main_action; }

    public void setMain_action(String main_action) { this.main_action = main_action; }

    public String getDetail_action() { return detail_action; }

    public void setDetail_action(String detail_action) { this.detail_action = detail_action; }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCase_number() {
        return case_number;
    }

    public void setCase_number(String case_number) {
        this.case_number = case_number;
    }

    public String getAcc_x() {
        return Acc_x;
    }

    public void setAcc_x(String acc_x) {
        Acc_x = acc_x;
    }

    public String getAcc_y() {
        return Acc_y;
    }

    public void setAcc_y(String acc_y) {
        Acc_y = acc_y;
    }

    public String getAcc_z() {
        return Acc_z;
    }

    public void setAcc_z(String acc_z) {
        Acc_z = acc_z;
    }

    public String getGyro_x() {
        return Gyro_x;
    }

    public void setGyro_x(String gyro_x) {
        Gyro_x = gyro_x;
    }

    public String getGyro_y() {
        return Gyro_y;
    }

    public void setGyro_y(String gyro_y) {
        Gyro_y = gyro_y;
    }

    public String getGyro_z() {
        return Gyro_z;
    }

    public void setGyro_z(String gyro_z) {
        Gyro_z = gyro_z;
    }

    public String getOri_x() {
        return Ori_x;
    }

    public void setOri_x(String ori_x) {
        Ori_x = ori_x;
    }

    public String getOri_y() {
        return Ori_y;
    }

    public void setOri_y(String ori_y) {
        Ori_y = ori_y;
    }

    public String getOri_z() {
        return Ori_z;
    }

    public void setOri_z(String ori_z) {
        Ori_z = ori_z;
    }

    public String getMag_x() {
        return Mag_x;
    }

    public void setMag_x(String mag_x) {
        Mag_x = mag_x;
    }

    public String getMag_y() {
        return Mag_y;
    }

    public void setMag_y(String mag_y) {
        Mag_y = mag_y;
    }

    public String getMag_z() {
        return Mag_z;
    }

    public void setMag_z(String mag_z) {
        Mag_z = mag_z;
    }

    private JSONObject jsonObject = new JSONObject();

    void setJsonObject(String _main_action, String _file_name, String _label, String _case_number,
                       String _Acc_x, String _Acc_y, String _Acc_z,
                       String _Gyro_x, String _Gyro_y, String _Gyro_z,
                       String _Ori_x, String _Ori_y, String _Ori_z,
                       String _Mag_x, String _Mag_y, String _Mag_z) {
        try {
            this.jsonObject.put("main_action", _main_action);
            this.jsonObject.put("detail_action", _file_name);
            this.jsonObject.put("label", _label);
            this.jsonObject.put("case_number", _case_number);
            this.jsonObject.put("Acc_x", _Acc_x);
            this.jsonObject.put("Acc_y", _Acc_y);
            this.jsonObject.put("Acc_z", _Acc_z);
            this.jsonObject.put("Gyro_x", _Gyro_x);
            this.jsonObject.put("Gyro_y", _Gyro_y);
            this.jsonObject.put("Gyro_z", _Gyro_z);
            this.jsonObject.put("Ori_x", _Ori_x);
            this.jsonObject.put("Ori_y", _Ori_y);
            this.jsonObject.put("Ori_z", _Ori_z);
            this.jsonObject.put("Mag_x", _Mag_x);
            this.jsonObject.put("Mag_y", _Mag_y);
            this.jsonObject.put("Mag_z", _Mag_z);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject getJsonObject() { return jsonObject; }
}
