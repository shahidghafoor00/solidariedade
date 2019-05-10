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
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.solidariedadediria.MainActivity;
import com.byteshaft.solidariedadediria.R;
import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.Movement;
import com.byteshaft.solidariedadediria.database.User;
import com.byteshaft.solidariedadediria.sidebar_fragments.Movements;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

import java.util.List;

public class Login extends Fragment implements View.OnClickListener {

    private View mBaseView;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mSignUpTextView;
    private String mPasswordString;
    private String mEmailString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mBaseView = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = mBaseView.findViewById(R.id.email_edit_text);
        mPassword = mBaseView.findViewById(R.id.password_edit_text);
        mLoginButton = mBaseView.findViewById(R.id.button_login);
        mSignUpTextView = mBaseView.findViewById(R.id.sign_up_text_view);

        mLoginButton.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                if (validate()) {
                    loginUser(mEmailString, mPasswordString);
                }
                break;
            case R.id.sign_up_text_view:
                AccountManager.getInstance().loadFragment(new Register());
                break;
        }
    }

    private boolean validate() {
        boolean valid = true;

        mEmailString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();

        if (mEmailString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmail.setError("please provide a valid email");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mPasswordString.isEmpty() || mPassword.length() < 4) {
            mPassword.setError("Enter minimum 4 characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }



    private void loginUser(final String email, final String password) {
        class GetUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {
                User user = DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .userDao().getUser(email, password);
                System.out.println(user);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if (user == null) {
                    Toast.makeText(getContext(), "Invalid Password or Email", Toast.LENGTH_SHORT).show();
                } else {
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, user.getUsername());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, user.getEmail());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, user.getPassword());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_AMOUNT, user.getAmount());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_ID, String.valueOf(user.getId()));
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    AccountManager.getInstance().finish();
                    AppGlobals.loginState(true);
                }
            }
        }

        GetUser getUser = new GetUser();
        getUser.execute();
    }
}
