package com.example.myapplication.ui.cleaning;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.ClassTypes.Room;
import com.example.myapplication.DataManager;
import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.DataManager.CompletedRooms;

public class CleaningFragment extends Fragment {
    public static OnBackPressedCallback onBackPressedCallback;
    public static boolean updateEnabled = true;
    final DisplayMetrics displayMetrics = new DisplayMetrics();

    public boolean charToBool(char in) { return in == '1'; }

    public char[] dataArr = {'0','0','0','0','0','0','0','0'};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cleaning, container, false);
        updateEnabled = true;
        int cRoomIndex = 0;
        for(String[] cRoom: CompletedRooms){
            if(!DataManager.room.equals(null) && cRoom[0].equals(DataManager.room.ID)){
                break;
            }
            cRoomIndex++;
        }
        if(!DataManager.record.Data.equals("NA")){ dataArr = DataManager.record.Data.toCharArray(); }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        CompoundButton.OnCheckedChangeListener cblisten = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.d("Final check", "Checkbox ID was:" + buttonView.getId());
                    dataArr[buttonView.getId()] = '1';
                } else {
                    dataArr[buttonView.getId()] = '0';
                }
                DataManager.record.LastData = String.valueOf(dataArr);
                DataManager.record.Data = String.valueOf(dataArr);
                if(updateEnabled){DataManager.UpdateDB();}
            }
        };

        LinearLayout btnLayout = root.findViewById(R.id.CleanBtnLayout);
        LinearLayout checkboxLayout = root.findViewById(R.id.CleanCbLayout);
        final NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(updateEnabled){DataManager.UpdateDB();}
                updateEnabled = false;
                navController.navigate(R.id.navigation_home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
        double textScale = (displayMetrics.heightPixels * .01) + 4;
        CheckBox checkBox;
        String[] buttons = {"Main menu", "Notes", "Finish"};
        int menuID = 0;
        int noteID = 1;
        final int finishID = 2;
        String[] Tasks = {"Disinfected student desks and chairs", "Disinfected teacher desk and chair", "Cleaned floor", "Handles disinfected", "Vacuumed carpet(s)", "Cleaned bathroom(s)", "Disinfected bathrooms", "Checked for sanitizer"};
        Button menu = new Button(this.getContext());
        menu.setText(buttons[menuID]);
        menu.setId(menuID);
        menu.setTextSize((int)textScale);
        menu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final int finalCRoomIndex = cRoomIndex;
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.record.LastRoom = DataManager.room.ID;
                DataManager.record.LastData = DataManager.record.Data;
                if(updateEnabled){DataManager.UpdateDB();}
                updateEnabled = false;
                navController.navigate(R.id.navigation_home);
            }
        });

        Button notes = new Button(this.getContext());
        notes.setText(buttons[noteID]);
        notes.setId(noteID);
        notes.setTextSize((int)textScale);
        notes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.record.LastRoom = DataManager.room.ID;
                DataManager.record.LastData = DataManager.record.Data;
                if(updateEnabled){DataManager.UpdateDB();}
                navController.navigate(R.id.NotesFragments);
            }
        });

        Button finalize = new Button(this.getContext());
        finalize.setText(buttons[finishID]);
        finalize.setId(finishID);
        finalize.setTextSize((int)textScale);
        finalize.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.record.Data = String.valueOf(dataArr);
                DataManager.record.Time = DataManager.GetCurTime();
                DataManager.CompletedRooms.add(new String[]{DataManager.room.ID, DataManager.GetCurTime(), String.valueOf(dataArr)});
                DataManager.record.isFinal = true;
                if(updateEnabled){DataManager.UpdateDB();}
                updateEnabled = false;
                DataManager.room = null;
                DataManager.record = null;
                navController.navigate(R.id.roomSelect);
            }
        });

        //btnLayout.removeAllViews();
        btnLayout.addView(menu);
        btnLayout.addView(notes);
        btnLayout.addView(finalize);
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double height = (displayMetrics.heightPixels * .6) / 8;
        double width = displayMetrics.widthPixels* .95;
        if(width < 0){width = 0;}
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < Tasks.length; i++) {
            checkBox = new CheckBox(this.getContext());
            checkBox.setText(Tasks[i]);
            checkBox.setId(i);
            checkBox.setTextScaleX(1.3f);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setBackgroundColor(Color.BLUE);
            checkBox.setWidth((int)width);
            checkBox.setHeight((int)height);
            if (!String.valueOf(dataArr).equals("NA")) {
                checkBox.setChecked(charToBool(dataArr[i]));
            }
            Room room = DataManager.room;
            switch (i) {
                case 0:
                    checkBox.setEnabled(room.HasS);
                    break;
                case 1:
                    checkBox.setEnabled(room.HasT);
                    break;
                case 2:
                    checkBox.setEnabled(room.HasFloor);
                    break;
                case 3:
                    checkBox.setEnabled(true);
                    break;
                case 4:
                    checkBox.setEnabled(room.HasCarpet);
                    break;
                case 5:
                case 6:
                    checkBox.setEnabled(room.HasBRoom);
                    break;
                case 7:
                    checkBox.setEnabled(room.HasSani);
                    break;
            }
            checkBox.setOnCheckedChangeListener(cblisten);
            checkboxLayout.addView(checkBox);
        }
        TextView info = root.findViewById(R.id.Clean_info);
        String setText = "Currently cleaning room :"+DataManager.room.ID;
        info.setTextSize((int)textScale);
        info.setText(setText);
        return root;
    }
}