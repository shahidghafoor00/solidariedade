package com.byteshaft.solidariedadediria.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.solidariedadediria.MainActivity;
import com.byteshaft.solidariedadediria.R;

public class Register extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private Button mRegisterButton;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mBaseView = inflater.inflate(R.layout.fragment_register, container, false);

        mRegisterButton = mBaseView.findViewById(R.id.button_register);

        mName = mBaseView.findViewById(R.id.name_edit_text);
        mEmail = mBaseView.findViewById(R.id.email_edit_text);
        mPassword = mBaseView.findViewById(R.id.password_edit_text);
        mRegisterButton.setOnClickListener(this);


        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                break;
        }
    }
}
