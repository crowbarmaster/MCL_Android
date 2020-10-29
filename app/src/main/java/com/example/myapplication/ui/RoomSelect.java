package com.example.myapplication.ui;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.ClassTypes.Record;
import com.example.myapplication.ClassTypes.Room;
import com.example.myapplication.ClassTypes.User;
import com.example.myapplication.DataManager;
import com.example.myapplication.DataSynchronizer;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.myapplication.DataManager.*;
import static com.example.myapplication.DataManager.CompletedRooms;
import static com.example.myapplication.DataManager.record;
import static com.example.myapplication.DataManager.room;

public class RoomSelect extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_room_select, container, false);
        LinearLayout roomLayout = root.findViewById(R.id.RoomSelectLayout);
        Button btn;
        final int test;
        if (room != null && record != null && record.Time.equals("0")) {
            navController.navigate(R.id.fragment_cleaning);
        }
        if (RemainingRooms.isEmpty()) {
            finalizeRecord();
            TextView msg = new TextView(getContext());
            msg.setText("You are done cleaning for today!");
            msg.setTextSize(20);
            roomLayout.addView(msg);
        } else {
            if (RemainingRooms.size() <= 4) {
                test = RemainingRooms.size();
            } else {
                test = 4;
            }
            for (int i = 0; i < test; i++) {
                btn = new Button(this.getContext());
                String txt = "Room " + RemainingRooms.get(i);
                btn.setText(txt);
                btn.setId(i);
                btn.setTextSize(20);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int cRoomIndex = 0;
                        for(String[] cRoom: DataManager.CompletedRooms){

                            if(cRoom[0].equals(RemainingRooms.get(finalI))){
                                break;
                            }
                            cRoomIndex++;
                        }
                        if(cRoomIndex == CompletedRooms.size()){
                            CompletedRooms.add(new String[]{"0", "0", "0"});
                        }
                        room = Rooms.get(Integer.parseInt(RemainingRooms.get(finalI)));
                        if (record == null) {
                            record = new Record();
                            record.Data = "00000000";
                            record.Time = "0";
                            record.ID = RemainingRooms.get(finalI);
                            record.isNew = true;
                            CompletedRooms.set(cRoomIndex, new String[]{record.ID, record.Time, record.Data});
                            RemainingRooms.remove(finalI);
                            UpdateDB();
                        }
                        Log.d("Room click", "Room clicked with ID of: " + DataManager.room.ID);
                        navController.navigate(R.id.fragment_cleaning);
                    }
                });
                roomLayout.addView(btn);
            }
            final EditText RoomTxt = new EditText(getContext());
            String txt = "Custom room entry:";
            btn = new Button(getContext());
            btn.setText(txt);
            btn.setId(test + 1);
            btn.setTextSize(20);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(RoomTxt.getText().length() > 2){
                        int cRoomIndex = 0;
                        for(String[] cRoom: DataManager.CompletedRooms){

                            if(cRoom[0].equals(RoomTxt.getText().toString())){
                                break;
                            }
                            cRoomIndex++;
                        }
                        if(cRoomIndex == CompletedRooms.size()){
                            CompletedRooms.add(new String[]{"0", "0", "0"});
                        }
                        int id = Integer.parseInt(RoomTxt.getText().toString());
                        if(RemainingRooms.contains(String.valueOf(id))){
                            if(room == null) {
                                room = new Room();
                                room = Rooms.get(id);
                            }
                            RemainingRooms.remove(String.valueOf(id));
                            if (record == null) {
                                record = new Record();
                                record.Data = "00000000";
                                record.Time = "0";
                                record.ID = room.ID;
                                record.isNew = true;
                                CompletedRooms.set(cRoomIndex, new String[]{record.ID, record.Time, record.Data});
                                UpdateDB();
                            }
                            Log.d("Room click", "Room clicked with ID of: " + room.ID);
                            navController.navigate(R.id.fragment_cleaning);
                        }
                    }
                }
            });
            roomLayout.addView(btn);

            RoomTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
            RoomTxt.setTextSize(20);
            RoomTxt.setId(test + 2);
            RoomTxt.setFocusableInTouchMode(true);
            RoomTxt.requestFocus();
            roomLayout.addView(RoomTxt);
        }

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}