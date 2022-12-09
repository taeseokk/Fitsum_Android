package org.fitsum.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.ChangeNicknameActivity;
import org.fitsum.ChangePasswordActivity;
import org.fitsum.FirstPart.FirstActivity;
import org.fitsum.MainActivity;


public class ProfileFragment extends BottomSheetDialogFragment {

    Button ch_nickname, ch_passwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ch_nickname = (Button) view.findViewById(R.id.ch_nickname);
        ch_passwd = (Button) view.findViewById(R.id.ch_passwd);


        ch_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeNicknameActivity.class);
                startActivity(intent);
            }
        });

        ch_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

}