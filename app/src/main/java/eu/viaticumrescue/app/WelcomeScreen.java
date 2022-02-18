package eu.viaticumrescue.app;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ExecutionException;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void openSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.viaticumrescue.eu"));
        startActivity(intent);
    }

    public void openVids(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://viaticumrescue.eu/mobile_app/app_vids.php"));
        startActivity(intent);
    }

    public void openRescueManuals(View view) {
        Intent myIntent = new Intent(WelcomeScreen.this, RescueManuals.class);
        WelcomeScreen.this.startActivity(myIntent);
    }

    public void openPublicManuals(View view) {
        Intent myIntent = new Intent(WelcomeScreen.this, PublicManuals.class);
        WelcomeScreen.this.startActivity(myIntent);
    }

    public void openAboutPage(View view) {
        Intent myIntent = new Intent(WelcomeScreen.this, AboutActivity.class);
        WelcomeScreen.this.startActivity(myIntent);
    }
}