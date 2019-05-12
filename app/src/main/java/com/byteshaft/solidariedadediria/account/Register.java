package com.byteshaft.solidariedadediria.account;

import android.content.Intent;
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

import com.byteshaft.solidariedadediria.MainActivity;
import com.byteshaft.solidariedadediria.R;
import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.User;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

public class Register extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private Button mRegisterButton;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;

    private String mEmailString;
    private String mPasswordString;
    private String mNameString;
    private float initialAmmount = 10.00f;

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
        if (v.getId() == R.id.button_register) {
            if (validate()) {
                registerUser();
                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, mNameString);
                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, mEmailString);
                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, mPasswordString);
                AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, initialAmmount);
            }
        }
    }


    private boolean validate() {
        boolean valid = true;
        mEmailString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();
        mNameString = mName.getText().toString();

        if (mEmailString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmail.setError("please provide a valid email");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mPasswordString.isEmpty() || mPassword.length() < 4) {
            mPassword.setError("Enter minimum 4 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mNameString.isEmpty()) {
            mName.setError("Required");
            valid = false;
        } else {
            mName.setError(null);
        }
        return valid;
    }

    private void registerUser() {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                User user = new User();
                user.setUsername(mNameString);
                user.setEmail(mEmailString);
                user.setPassword(mPasswordString);
                user.setAmount(initialAmmount);
                DatabaseClient.getInstance(getContext())
                        .getAppDatabase()
                        .userDao()
                        .insert(user);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Registered", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                AppGlobals.loginState(true);
                AccountManager.getInstance().finish();
            }
        }

        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }
}
