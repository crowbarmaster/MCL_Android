package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myapplication.ClassTypes.User;
import com.example.myapplication.DataManager;
import com.example.myapplication.R;

public class UserSelect extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_fragment, container, false);

        LinearLayout userLayout = root.findViewById(R.id.User_btn_layout);
        Button btn;
        for(final User user: DataManager.Users){
            String nameStr;
            nameStr = user.FirstName+ " " + user.LastName;
            btn = new Button(this.getContext());
            btn.setText(nameStr);
            btn.setId(Integer.parseInt(user.ID));
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(DataManager.user != null || DataManager.formerUser == null){
                        DataManager.formerUser = user;
                    }
                    DataManager.room = null;
                    DataManager.record = null;
                    DataManager.user = user;
                    DataManager.getUserRecord();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_home);

                }
            });
            userLayout.addView(btn);
        }

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }
}