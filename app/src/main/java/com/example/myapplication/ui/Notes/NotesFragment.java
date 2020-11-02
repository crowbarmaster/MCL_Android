package com.example.myapplication.ui.Notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;

public class NotesFragment extends Fragment {
    NavController nav;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        Button cam = root.findViewById(R.id.btn_cam);
        Button vmsg = root.findViewById(R.id.btn_vmsg);
        nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        final int responseCode = 0;
        vmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), AudioActivity.class);
                startActivityForResult(myIntent, responseCode);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camIntent = new Intent(getActivity(), CamActivity.class);
                startActivityForResult(camIntent, responseCode);
            }
        });

        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        nav.navigate(R.id.fragment_cleaning);
    }
}