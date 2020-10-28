package com.example.myapplication.ClassTypes;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class User {
    public String ID = "na";
    public String FirstName;
    public String LastName;
    public String Bldg;
    public String Shift;
    public String[] Rooms;
    public Record UserRecord;

    public User(){}

    public class Record {
        public String ID;
        public String Data;
        public String Time;
        public boolean hasLocalChanges = false;
        public boolean isFinal = false;
        public boolean RemoveMark = false;
        public boolean isNew = false;
    }
}

