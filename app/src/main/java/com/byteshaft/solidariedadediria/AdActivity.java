package com.byteshaft.solidariedadediria;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private Button dismissButton;
    private ImageView adImage;

    private static AdActivity sInstance;
    private int availableBalance = Integer.parseInt(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_AMOUNT));
    private String email = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
    private int newBalance;

    public static AdActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_ad);
        adImage = findViewById(R.id.ad_image);
        dismissButton = findViewById(R.id.button_dismiss);
        adImage.setOnClickListener(this);
        dismissButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()) {
         case R.id.button_dismiss:
             finish();
             break;
         case R.id.ad_image:
             newBalance = availableBalance + 5;
             update(String.valueOf(newBalance), email);
             finish();
     }
    }

    private void update(final String amount, final String email) {
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
                Toast.makeText(getApplicationContext(), "You have earned 5€", Toast.LENGTH_LONG).show();
                AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_AMOUNT, String.valueOf(newBalance));
            }
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
