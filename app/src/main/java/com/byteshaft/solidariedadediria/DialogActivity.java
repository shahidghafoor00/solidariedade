package com.byteshaft.solidariedadediria;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.Movement;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout instituteOne;
    private LinearLayout instituteTwo;
    private LinearLayout instituteThree;
    private LinearLayout instituteFour;
    private EditText amountEditText;
    private String mInstituteString = "";
    private String mAmountString;

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

    private void sendMoney(final String name, final int money) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Movement movement = new Movement();
                movement.setInstituteName(name);
                movement.setMoney(money);

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
                    sendMoney(mInstituteString, Integer.parseInt(mAmountString));
                }
                break;

            case R.id.institute_two:
                mInstituteString = "Institute 2";
                if (validate()) {
                    sendMoney(mInstituteString, Integer.parseInt(mAmountString));
                }
                break;

            case R.id.institute_three:
                mInstituteString = "Institute 3";
                if (validate()) {
                    sendMoney(mInstituteString, Integer.parseInt(mAmountString));
                }
                break;

            case R.id.institute_four:
                mInstituteString = "Institute 4";
                if (validate()) {
                    sendMoney(mInstituteString, Integer.parseInt(mAmountString));
                }
                break;
        }
    }
}
