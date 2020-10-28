package com.example.myapplication;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.myapplication.ClassTypes.Record;
import com.example.myapplication.ClassTypes.Room;
import com.example.myapplication.ClassTypes.User;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DataSynchronizer {

    public static int recID;
public DataManager dataManager = new DataManager();

    public void Start (){
        dataManager.LoadAllData();
    }
}
