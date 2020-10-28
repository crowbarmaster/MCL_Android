package com.example.myapplication.ClassTypes;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Room {
    public String ID;
    public boolean HasS = false;
    public boolean HasT = false;
    public boolean HasFloor = false;
    public boolean HasCarpet = false;
    public boolean HasBRoom = false;
    public boolean HasSani = false;
    public boolean hasLocalChanges = false;
    public boolean RemoveMark = false;
    public boolean notNull = false;
    public String tName;

    public boolean charToBool (char in){
        if(in == '1'){
            return true;
        }else{
            return false;
        }
    }

    public char boolToChar (boolean in){
        if(in){
            return '1';
        }
        return '0';
    }

    public Room(){}

    public String getDataString (){
        StringBuilder str = new StringBuilder("000000");
        str.setCharAt(0, boolToChar(HasS));
        str.setCharAt(1, boolToChar(HasT));
        str.setCharAt(2, boolToChar(HasFloor));
        str.setCharAt(3, boolToChar(HasCarpet));
        str.setCharAt(4, boolToChar(HasBRoom));
        str.setCharAt(5, boolToChar(HasSani));
        return str.toString();
    }
}



