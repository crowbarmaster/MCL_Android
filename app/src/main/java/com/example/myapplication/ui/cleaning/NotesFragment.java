package com.example.myapplication.ui.cleaning;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.ui.Notes.AudioActivity;
import com.example.myapplication.ui.Notes.CamActivity;

public class NotesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        Button cam = root.findViewById(R.id.btn_cam);
        Button vmsg = root.findViewById(R.id.btn_vmsg);
        vmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), AudioActivity.class);
                startActivity(myIntent);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camIntent = new Intent(getActivity(), CamActivity.class);
                startActivity(camIntent);
            }
        });
        return root;
    }
}