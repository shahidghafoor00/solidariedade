package com.byteshaft.solidariedadediria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private Button offerButton;
    private Button dismissButton;

    private static AdActivity sInstance;

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

        offerButton = findViewById(R.id.button_offer);
        dismissButton = findViewById(R.id.button_dismiss);
        offerButton.setOnClickListener(this);
        dismissButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_offer:
                System.out.println("Offer Button clicked");
                break;
            case R.id.button_dismiss:
                finish();
                break;
        }
    }
}
