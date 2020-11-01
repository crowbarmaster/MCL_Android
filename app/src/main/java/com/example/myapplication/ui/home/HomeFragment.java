package com.example.myapplication.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.DataManager;
import com.example.myapplication.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Context context = this.getContext();
        Button clean = new Button(this.getContext());
        Button review = new Button(this.getContext());
        Button login = new Button(this.getContext());
        String cleanTxt = "Begin cleaning";
        int cleanID = 0;
        String revTxt = "Review todays cleaning";
        int revID = 1;
        String LoginTxt = "Select user";
        int logID = 2;

        LinearLayout userLayout = root.findViewById(R.id.home_layout);
        clean.setText(cleanTxt);
        clean.setId(cleanID);
        clean.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                if(!DataManager.user.ID.equals("na")) {
                    navController.navigate(R.id.roomSelect);
                }else{
                    Toast.makeText(context, "No user selected! Please select a user first!", Toast.LENGTH_LONG).show();
                }
            }
        });

        review.setText(revTxt);
        review.setId(revID);
        review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

            }
        });


        login.setText(LoginTxt);
        login.setId(logID);
        login.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.userFragment);
            }
        });
        userLayout.addView(clean);
        userLayout.addView(review);
        userLayout.addView(login);
        return root;
    }
}