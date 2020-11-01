package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.ClassTypes.Record;
import com.example.myapplication.ClassTypes.Room;
import com.example.myapplication.ClassTypes.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class DataManager {
    public static User user = new User();
    public static User formerUser;
    public static Room room;
    public static Record record;
    public static List<User> Users = new ArrayList<>();
    public static List<Room> Rooms = new ArrayList<>(300);
    public static List<String[]> CompletedRooms = new ArrayList<>();
    public static List<String> RemainingRooms = new ArrayList<>();

    public DataManager (){}

    public Net net = new Net();

    public static HashMap<String, String> getAllUsersHT(){
        HashMap<String, String> cmd = new HashMap<>();
        cmd.put("cmd", "get");
        cmd.put("val1", "`users`");
        cmd.put("val2", "*");
        return cmd;
    }
    public static HashMap<String, String> getAllTempRecordsHT(){
        HashMap<String, String> cmd = new HashMap<>();
        cmd.put("cmd", "get");
        cmd.put("val1", "`pending_records`");
        cmd.put("val2", "*");
        return cmd;
    }
    public static HashMap<String, String> getAllRoomsHT(){
        HashMap<String, String> cmd = new HashMap<>();
        cmd.put("cmd", "get");
        cmd.put("val1", "`rooms`");
        cmd.put("val2", "*");
        return cmd;
    }

    public static void ResetRemainingRooms() {
        RemainingRooms.clear();
    }

    public void LoadAllData() {
        for(int i = 0; i<299; i++){
            //CompletedRooms.add(new String[] {String.valueOf(i), "time", "00000000"});
            Rooms.add(new Room());
        }
        HashMap<String, HashMap<String, String>> getUsers = net.PullSQL(getAllUsersHT());
        HashMap<String, HashMap<String, String>> getRooms = net.PullSQL(getAllRoomsHT());
        if(!getUsers.isEmpty()) {
          //  Log.d("Loader", "User data start");
            for (String id : getUsers.keySet()) {
                User user = new User();
               // Log.d("UserLoader", "ID was: "+id);
                user.ID = id;
                user.FirstName = getUsers.get(id).get("fname");
               // Log.d("Loader", "User first name: "+user.FirstName+" With ID: "+user.ID);
                user.LastName = getUsers.get(id).get("lname");
                user.Bldg = getUsers.get(id).get("bldg");
                user.Shift = getUsers.get(id).get("shift");
                user.Rooms = getUsers.get(id).get("rooms").split(";");
                Users.add(user);
            }
        }
        if(!getRooms.isEmpty()) {
            for (String id : getRooms.keySet()) {
                Room room = new Room();
                StringBuilder str = new StringBuilder(getRooms.get(id).get("data"));
                room.ID = id;
                room.HasS = charToBool(str.charAt(0));
                room.HasT = charToBool(str.charAt(1));
                room.HasFloor = charToBool(str.charAt(2));
                room.HasCarpet = charToBool(str.charAt(3));
                room.HasBRoom = charToBool(str.charAt(4));
                room.HasSani = charToBool(str.charAt(5));
                room.hasLocalChanges = false;
                room.notNull = true;
                room.tName = getRooms.get(id).get("tname");
                Rooms.set(Integer.parseInt(id), room);
            }
        }
    }

    public static void finalizeRecord () {
        Net net = new Net();
        HashMap<String, HashMap<String, String>> getRecords = net.PullSQL(getAllTempRecordsHT());
        HashMap<String, String> ins = new HashMap<>();
        HashMap<String, String> rem = new HashMap<>();
        ins.put("cmd", "ins");
        ins.put("val1", "`records`");
        ins.put("val2", "`userid`, `date`, `data`");
        ins.put("val3", "'" + user.ID + "', '" + getRecords.get(user.ID).get("date") + "', '" + getRecords.get(user.ID).get("data") + "'");
        net.PushSQL(ins);
        rem.put("cmd", "rem");
        rem.put("val1", "`pending_records`");
        rem.put("val2", "userid=" + user.ID);
        net.PushSQL(rem);
    }

    public static void getUserRecord (){
        Net net = new Net();
        ResetRemainingRooms();
        //ResetCompletedRooms();
        CompletedRooms.clear();
        HashMap<String, HashMap<String, String>> getRecords = net.PullSQL(getAllTempRecordsHT());
        if(getRecords.containsKey(user.ID)) {
            if (getRecords.get(user.ID).get("date").equals(GetCurDate())) {
                Record rec = new Record();
                String[] split = getRecords.get(user.ID).get("data").split(">");
                String rid = split[split.length - 1].split(";")[0];
                for (String expand : split) {
                    DataManager.CompletedRooms.add(expand.split(";"));
                }
                if(split[split.length - 1].split(";")[1].equals("0")) {
                    rec.ID = rid;
                    rec.Time = "0";
                    rec.Data = split[split.length - 1].split(";")[2];
                    record = rec;
                    DataManager.room = DataManager.Rooms.get(Integer.parseInt(rid));
                }
            }else {
                finalizeRecord();
            }
        }
        DataManager.RemainingRooms.addAll(Arrays.asList(user.Rooms));
        if(DataManager.CompletedRooms.size() > 0) {
            for (String[] expand : DataManager.CompletedRooms) {
                if (!expand[1].equals("0")) {
                    RemainingRooms.remove(expand[0]);
                }
            }
        }

    }

    public static void UpdateDB (){
        Net net = new Net();
        HashMap<String, String> cmd = new HashMap<>();
        if(record != null && record.isNew) {
            cmd.put("cmd", "ins");
            cmd.put("val1", "`pending_records`");
            cmd.put("val2", "`userid`, `date`, `data`");
            cmd.put("val3", "'" + user.ID + "', '" + GetCurDate() + "', '" + ConcatRecords(CompletedRooms) + "'");
            net.PushSQL(cmd);
            record.isNew = false;
            Log.d("UpdateDB", "Adding record with user ID: " + user.ID);
        }else {
            cmd.put("cmd", "set");
            cmd.put("val1", "`pending_records`");
            cmd.put("val2", "`date`='"+GetCurDate()+"', `data`='"+ConcatRecords(CompletedRooms)+"'");
            cmd.put("val3", "userid="+user.ID);
            net.PushSQL(cmd);
            Log.d("UpdateDB", "Updating record with user ID: "+ user.ID);

        }
    }

    public static String ConcatRecords (List<String[]> inMap) {
        StringBuilder outStr = new StringBuilder();
        for (String[] key : inMap) {
            if(!key[1].equals("time") || key.length != 3) {
                if (outStr.toString().equals("")) {
                    outStr = new StringBuilder(key[0] + ";" + key[1] + ";" + key[2] + ">");
                } else {
                    outStr.append(key[0]).append(";").append(key[1]).append(";").append(key[2]).append(">");
                }
            }
        }
        return outStr.toString();
    }

    public static String GetCurDate (){
        LocalDate date = LocalDate.now();
        return date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
    }

    public static String GetCurTime (){
        LocalTime localTime = LocalTime.now();

        return localTime.getHour() + "." + localTime.getMinute() + "." + localTime.getSecond();  // using a # instead of : due to JSON array output using :
    }

    public boolean charToBool (char in){
        return in == '1';
    }

    public String[] readTxt (String abPath) {
        String[] dataArr = new String[0];
        try
        {
            File file = new File(abPath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int i = 0;
                String data = scanner.nextLine();
                dataArr[i] = data;
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Log.e("readTxt", "An error occurred.");
            e.printStackTrace();
        }
        return dataArr;
    }

    public void writeTxt (String name, String input) {
        File tmpFile = new File(Objects.requireNonNull(MainActivity.CoreContext.getExternalCacheDir()).getAbsolutePath(), name);
        try {
            FileWriter fw = new FileWriter(tmpFile);
            fw.write(input);
            fw.close();
        } catch (IOException e) {
            Log.e("writeTxt", "An error occurred.");
            e.printStackTrace();
        }
    }
}
