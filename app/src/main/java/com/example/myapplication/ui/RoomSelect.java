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
        if (DataManager.room != null) {
            navController.navigate(R.id.fragment_cleaning);
        }
        if (DataManager.RemainingRooms.isEmpty()) {
            DataManager.finalizeRecord();
            TextView msg = new TextView(getContext());
            msg.setText("You are done cleaning for today!");
            msg.setTextSize(20);
            roomLayout.addView(msg);
        } else {
            if (DataManager.RemainingRooms.size() <= 4) {
                test = DataManager.RemainingRooms.size();
            } else {
                test = 4;
            }
            for (int i = 0; i < test; i++) {
                btn = new Button(this.getContext());
                String txt = "Room " + DataManager.RemainingRooms.get(i);
                btn.setText(txt);
                btn.setId(i);
                btn.setTextSize(20);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int room = Integer.parseInt(DataManager.RemainingRooms.get(finalI));
                        DataManager.room = DataManager.Rooms.get(room);
                        if (DataManager.record == null) {
                            Record record = new Record();
                            record.Data = "00000000";
                            record.Time = DataManager.GetCurTime();
                            record.ID = DataManager.RemainingRooms.get(finalI);
                            record.isNew = true;
                            DataManager.CompletedRooms.set(Integer.parseInt(DataManager.RemainingRooms.get(finalI)), new String[]{record.ID, record.Time, record.Data});
                            DataManager.record = record;
                            DataManager.RemainingRooms.remove(finalI);
                            DataManager.UpdateDB();
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
                    int id = Integer.parseInt(RoomTxt.getText().toString());
                    if(!RoomTxt.getText().equals("")){
                        if(DataManager.RemainingRooms.contains(String.valueOf(id))){
                            DataManager.room = DataManager.Rooms.get(id);
                            DataManager.RemainingRooms.remove(String.valueOf(id));
                            if (DataManager.record == null) {
                                Record record = new Record();
                                record.Data = "00000000";
                                record.Time = DataManager.GetCurTime();
                                record.ID = DataManager.room.ID;
                                record.isNew = true;
                                DataManager.CompletedRooms.set(Integer.parseInt(record.ID), new String[]{record.ID, record.Time, record.Data});
                                DataManager.record = record;
                                DataManager.UpdateDB();
                            }
                            Log.d("Room click", "Room clicked with ID of: " + DataManager.room.ID);
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