package com.example.myapplication.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class CleaningFragment extends Fragment {
    public static OnBackPressedCallback onBackPressedCallback;
    public boolean updateEnabled = true;
    final DisplayMetrics displayMetrics = new DisplayMetrics();

    public boolean charToBool(char in) { return in == '1'; }

    public char[] dataArr = {'0','0','0','0','0','0','0','0'};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cleaning, container, false);
        final TimerTask Updater = new Update();
        new Timer().scheduleAtFixedRate(Updater, 1500, 5000);

        if(!DataManager.record.Data.equals("00000000")){ dataArr = DataManager.record.Data.toCharArray(); }
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
                DataManager.record.Data = String.valueOf(dataArr);
                DataManager.record.Time = DataManager.GetCurTime();
            }
        };

        LinearLayout btnLayout = root.findViewById(R.id.CleanBtnLayout);
        LinearLayout checkboxLayout = root.findViewById(R.id.CleanCbLayout);
        final NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Updater.cancel();
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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updater.cancel();
                DataManager.CompletedRooms.set(Integer.parseInt(DataManager.room.ID), new String[]{DataManager.room.ID, DataManager.GetCurTime(), String.valueOf(dataArr)});
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
                Updater.cancel();
                DataManager.CompletedRooms.set(Integer.parseInt(DataManager.room.ID), new String[]{DataManager.room.ID, DataManager.GetCurTime(), String.valueOf(dataArr)});
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
                DataManager.CompletedRooms.set(Integer.parseInt(DataManager.room.ID), new String[]{DataManager.room.ID, DataManager.GetCurTime(), String.valueOf(dataArr)});
                if(updateEnabled){DataManager.UpdateDB();}
                updateEnabled = false;
                DataManager.room = null;
                DataManager.record = null;
                Updater.cancel();
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
        float scale = displayMetrics.widthPixels * 0.0012f;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // layoutParams.setMargins((int)width, 20, 0, (int)height);
        for (int i = 0; i < Tasks.length; i++) {
            checkBox = new CheckBox(this.getContext());
            checkBox.setText(Tasks[i]);
            checkBox.setId(i);
            checkBox.setTextScaleX(1.3f);
            checkBox.setLayoutParams(layoutParams);
           // checkBox.setScaleX(1.5f);
          //  checkBox.setScaleY(1.5f);
            checkBox.setWidth((int)width);
            checkBox.setHeight((int)height);
            if (!String.valueOf(dataArr).equals("00000000")) {
                checkBox.setChecked(charToBool(dataArr[i]));
            }
            Room room = DataManager.room;
            switch (i) {
                case 0:
                    if (room.HasS) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
                    break;
                case 1:
                    if (room.HasT) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
                    break;
                case 2:
                    if (room.HasFloor) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
                    break;
                case 3:
                    checkBox.setEnabled(true);
                    break;
                case 4:
                    if (room.HasCarpet) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
                    break;
                case 5:
                case 6:
                    if (room.HasBRoom) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
                    break;
                case 7:
                    if (room.HasSani) {
                        checkBox.setEnabled(true);
                    } else {
                        checkBox.setEnabled(false);
                    }
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

    private class Update extends TimerTask{

        @Override
        public void run() {
            if (DataManager.room != null) {
                DataManager.CompletedRooms.set(Integer.parseInt(DataManager.room.ID), new String[]{DataManager.room.ID, DataManager.GetCurTime(), String.valueOf(dataArr)});
                DataManager.UpdateDB();
            }
        }
    }
}