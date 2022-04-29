package com.hmdapps.greenpurse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FeedbackActivity extends AppCompatActivity {
    String to, subject, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button back= findViewById(R.id.btnBack);
        back.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),HomeActivity.class)));
        Button send = findViewById(R.id.fb_submit);
        send.setOnClickListener(view -> {
            Toast.makeText(FeedbackActivity.this, "Chose the Gmail's application to send your Feedback !", Toast.LENGTH_SHORT).show();
            generateFeedback();
        });

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    @SuppressLint("IntentReset")
    private void generateFeedback() {
        RadioGroup type = findViewById(R.id.fb_type);
        EditText feed = findViewById(R.id.fb_text);
        RatingBar ratingBar = findViewById(R.id.app_ratings);
        int tp= type.getCheckedRadioButtonId();
        RadioButton selectedType = findViewById(tp);
        String feedtype= selectedType.getText().toString();
        String feedback= feed.getText().toString();
        float rating= ratingBar.getRating();
        View focusView = null;
        boolean cancel = false;

        feed.setError(null);


        if (TextUtils.isEmpty(feedback)) {
            feed.setError(getString(R.string.error_field_required));
            focusView = feed;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        else
        {
            to="hmd1apps@gmail.com";
            subject=feedtype;
            message=feedback +"\n\nRating : "+rating+ "\n\nFrom GreenPurse.";
            Intent sendmail = new Intent(Intent.ACTION_SEND);
            sendmail.setData(Uri.parse("mailto:"));
            sendmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            sendmail.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendmail.putExtra(Intent.EXTRA_TEXT, message);
            sendmail.setType("message/rfc822");
            try {
                startActivityForResult(Intent.createChooser(sendmail, "Send mail..."),0);
                Log.i("Thank you", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(FeedbackActivity.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}