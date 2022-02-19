package com.example.tp_spinner;

public class DataHolder {

    private String data;
    private static DataHolder singleInstance = new DataHolder();

    public static DataHolder getInstance(){
        if(singleInstance == null)
            singleInstance = new DataHolder();
        return singleInstance;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }
}
