package com.byteshaft.solidariedadediria.sidebar_fragments;

import android.os.AsyncTask;
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
import android.widget.Toast;

import com.byteshaft.solidariedadediria.R;
import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.User;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

public class UserInfo extends Fragment {

    private View mBaseView;
    private Button mChangeButton;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.user_info));
        mBaseView = inflater.inflate(R.layout.fragment_user_info, container, false);
        mNameEditText = mBaseView.findViewById(R.id.et_name);
        mPasswordEditText = mBaseView.findViewById(R.id.et_password);
        mEmailEditText = mBaseView.findViewById(R.id.et_email);
        mEmailEditText.setEnabled(false);

        mChangeButton = mBaseView.findViewById(R.id.button_change);
        mNameEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_NAME));
        mPasswordEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_PASSWORD));
        mEmailEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(
                        mNameEditText.getText().toString(),
                        mPasswordEditText.getText().toString(),
                        mEmailEditText.getText().toString());

                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, mNameEditText.getText().toString());
                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, mPasswordEditText.getText().toString());
            }
        });
        return mBaseView;
    }


    private void update(final String name, final String password, final String email) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getContext())
                        .getAppDatabase()
                        .userDao().update(name, password, email);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
