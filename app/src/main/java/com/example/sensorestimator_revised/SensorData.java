package com.example.sensorestimator_revised;

import androidx.annotation.NonNull;

class SensorData {

    private String accValue_X, accValue_Y, accValue_Z;
    private String gyroValue_X, gyroValue_Y, gyroValue_Z;
    private String oriValue_X, oriValue_Y, oriValue_Z;
    private String magValue_X, magValue_Y, magValue_Z;

    SensorData(){
        this.accValue_X = "";
        this.accValue_Y = "";
        this.accValue_Z = "";

        this.gyroValue_X = "";
        this.gyroValue_Y = "";
        this.gyroValue_Z = "";

        this.oriValue_X = "";
        this.oriValue_Y = "";
        this.oriValue_Z = "";

        this.magValue_X = "";
        this.magValue_Y = "";
        this.magValue_Z = "";
    }

    void setAccValueList(String accValue_X, String accValue_Y, String accValue_Z) {
        setAccValue_X(accValue_X);
        setAccValue_Y(accValue_Y);
        setAccValue_Z(accValue_Z);
    }

    void setGyroValueList(String gyroValue_X, String gyroValue_Y, String gyroValue_Z) {
        setGyroValue_X(gyroValue_X);
        setGyroValue_Y(gyroValue_Y);
        setGyroValue_Z(gyroValue_Z);
    }

    void setOriValueList(String oriValue_X, String oriValue_Y, String oriValue_Z) {
        setOriValue_X(oriValue_X);
        setOriValue_Y(oriValue_Y);
        setOriValue_Z(oriValue_Z);
    }

    void setMagneticValueList(String magValue_X, String magValue_Y, String magValue_Z) {
        setMagneticValue_X(magValue_X);
        setMagneticValue_Y(magValue_Y);
        setMagneticValue_Z(magValue_Z);
    }

    //-------------------------------------------------------------------------------------------------
    String getAccValue_X() {
        return accValue_X;
    }

    private void setAccValue_X(String accValue_X) {
        this.accValue_X = accValue_X;
    }

    String getAccValue_Y() {
        return accValue_Y;
    }

    private void setAccValue_Y(String accValue_Y) {
        this.accValue_Y = accValue_Y;
    }

    String getAccValue_Z() {
        return accValue_Z;
    }

    private void setAccValue_Z(String accValue_Z) {
        this.accValue_Z = accValue_Z;
    }

//-------------------------------------------------------------------------------------------------

    String getGyroValue_X() {
        return gyroValue_X;
    }

    private void setGyroValue_X(String gyroValue_X) {
        this.gyroValue_X = gyroValue_X;
    }

    String getGyroValue_Y() {
        return gyroValue_Y;
    }

    private void setGyroValue_Y(String gyroValue_Y) {
        this.gyroValue_Y = gyroValue_Y;
    }

    String getGyroValue_Z() {
        return gyroValue_Z;
    }

    private void setGyroValue_Z(String gyroValue_Z) {
        this.gyroValue_Z = gyroValue_Z;
    }

//-------------------------------------------------------------------------------------------------

    String getOriValue_X() {
        return oriValue_X;
    }

    private void setOriValue_X(String oriValue_X) {
        this.oriValue_X = oriValue_X;
    }

    String getOriValue_Y() {
        return oriValue_Y;
    }

    private void setOriValue_Y(String oriValue_Y) {
        this.oriValue_Y = oriValue_Y;
    }

    String getOriValue_Z() {
        return oriValue_Z;
    }

    private void setOriValue_Z(String oriValue_Z) {
        this.oriValue_Z = oriValue_Z;
    }

//-------------------------------------------------------------------------------------------------

    String getMagneticValue_X() { return magValue_X; }

    private void setMagneticValue_X(String magValue_X) { this.magValue_X = magValue_X; }

    String getMagneticValue_Y(){ return magValue_Y; }

    private void setMagneticValue_Y(String magValue_Y){ this.magValue_Y = magValue_Y; }

    String getMagneticValue_Z(){ return magValue_Z; }

    private void setMagneticValue_Z(String magValue_Z){ this.magValue_Z = magValue_Z; }

//-------------------------------------------------------------------------------------------------


    @NonNull
    @Override
    public String toString() {
        return accValue_X + "," + accValue_Y + "," + accValue_Z + "," +
                gyroValue_X  + "," + gyroValue_Y + "," + gyroValue_Z + "," +
                oriValue_X  + "," +oriValue_Y + "," +oriValue_Z + "," +
                magValue_X  + "," + magValue_Y + "," + magValue_Z;

    }
}

