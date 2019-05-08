package com.byteshaft.solidariedadediria.sidebar_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.solidariedadediria.R;

public class UserInfo extends Fragment {

    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.user_info));
        mBaseView = inflater.inflate(R.layout.fragment_user_info, container, false);
        return mBaseView;
    }
}
