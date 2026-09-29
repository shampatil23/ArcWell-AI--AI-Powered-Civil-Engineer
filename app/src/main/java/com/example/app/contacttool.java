package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class contacttool extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacttool);

        LinearLayout phoneLayout = findViewById(R.id.phoneLayout);
        LinearLayout emailLayout = findViewById(R.id.emailLayout);

        // Redirect to Dialer when clicking the phone layout (icon + number)
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9529811731"));
                startActivity(callIntent);
            }
        });

        // Redirect to Email app when clicking the email layout (icon + email)
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:shampatil7275@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I would like to ask about...");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (android.content.ActivityNotFoundException ex) {
                    // No email app installed
                }
            }
        });
    }
}
