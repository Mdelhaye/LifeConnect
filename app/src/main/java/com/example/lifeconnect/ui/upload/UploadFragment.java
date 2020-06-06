package com.example.lifeconnect.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifeconnect.MenuActivity;
import com.example.lifeconnect.R;
import com.example.lifeconnect.SignInActivity;
import com.example.lifeconnect.model.User;

public class UploadFragment extends Fragment {

    private UploadViewModel uploadViewModel;

    private Button button_save;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uploadViewModel =
                ViewModelProviders.of(this).get(UploadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        // Get User
        MenuActivity menuActivity = (MenuActivity) getActivity();
        user = menuActivity.getUser();

        button_save = root.findViewById(R.id.fragment_upload_button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        return root;
    }
}