package com.byteshaft.solidariedadediria;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.Movement;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout instituteOne;
    private LinearLayout instituteTwo;
    private LinearLayout instituteThree;
    private LinearLayout instituteFour;

    private EditText amountEditText;
    private String mInstituteString = "";
    private String mAmountString;
    private float availableBalance = AppGlobals.getMoneyFromSharedPreferences(AppGlobals.KEY_AMOUNT);
    private String email = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
    private float newBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        instituteOne = findViewById(R.id.institute_one);
        instituteTwo = findViewById(R.id.institute_two);
        instituteThree = findViewById(R.id.institute_three);
        instituteFour = findViewById(R.id.institute_four);
        amountEditText = findViewById(R.id.et_amount);

        instituteOne.setOnClickListener(this);
        instituteTwo.setOnClickListener(this);
        instituteThree.setOnClickListener(this);
        instituteFour.setOnClickListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().isEmpty()) {
                        if (availableBalance > Float.parseFloat(s.toString())) {
                            amountEditText.setTextColor(Color.BLACK);
                            System.out.println("pay");
                            instituteOne.setClickable(true);
                            instituteTwo.setClickable(true);
                            instituteThree.setClickable(true);
                            instituteFour.setClickable(true);

                        } else {
                            amountEditText.setTextColor(Color.RED);
                            amountEditText.setError("Not Enough Money");
                            instituteOne.setClickable(false);
                            instituteTwo.setClickable(false);
                            instituteThree.setClickable(false);
                            instituteFour.setClickable(false);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("something went wrong");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validate() {
        boolean valid = true;

        mAmountString = amountEditText.getText().toString();


        if (mAmountString.trim().isEmpty() || mAmountString.equals("0")) {
            amountEditText.setError("Please enter some amount");
            valid = false;
        } else {
            amountEditText.setError(null);
        }
        return valid;
    }

    private void sendMoney(final String name, final float money) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Movement movement = new Movement();
                movement.setInstituteName(name);
                movement.setMoney(money);
                movement.setUser_id(Integer.parseInt(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID)));
                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .movementDao()
                        .insert(movement);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Payment Done!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.institute_one:
                mInstituteString = "Institute 1";
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                    newBalance = availableBalance - Float.parseFloat(mAmountString);
                    AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);
                    update(newBalance, email);
                    System.out.println(newBalance);
                }
                break;

            case R.id.institute_two:
                mInstituteString = "Institute 2";
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                    newBalance = availableBalance - Float.parseFloat(mAmountString);
                    AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);
                    update(newBalance, email);
                    System.out.println(newBalance);
                }
                break;

            case R.id.institute_three:
                mInstituteString = "Institute 3";
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                    newBalance = availableBalance - Float.parseFloat(mAmountString);
                    AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);
                    update(newBalance, email);
                    System.out.println(newBalance);
                }
                break;

            case R.id.institute_four:
                mInstituteString = "Institute 4";
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                    newBalance = availableBalance - Float.parseFloat(mAmountString);
                    AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);
                    update(newBalance, email);
                    System.out.println(newBalance);
                }
                break;
        }
    }

    private void update(final float amount, final String email) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplication())
                        .getAppDatabase()
                        .userDao().updateAmount(amount, email);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
