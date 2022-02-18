package eu.viaticumrescue.app;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LoadingScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageHelper.UpdateLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                CheckVersions();
                Intent myIntent = new Intent(LoadingScreen.this, WelcomeScreen.class);
                LoadingScreen.this.startActivity(myIntent);
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    public void CheckVersions() {
        //Get current locale
        String currentLocale = ManualHelper.GetLanguage(this);

        //Check if the network is available
        boolean networkAvailable = ManualHelper.IsNetworkAvailable(this);
        if (networkAvailable) {
            try {
                Log.v("PDF_Download", "Internet Access Granted...Checking Manuals");

                PocketManuals pocketManuals = new ViaticumApiClient().execute().get();
                if (pocketManuals == null) {
                    Log.e("Welcome_Screen", "Manuals are null");

                    if (ManualHelper.GetManualList(this) != null) {
                        Log.v("Welcome_Screen", "Cannot get manuals from Internet, but offline manuals found");
                        return;
                    } else {
                        Log.e("Welcome_Screen", "Cannot get manuals and no manuals found in context");
                        return;
                    }
                }

                //First check the locale
                if (!pocketManuals.PublicManuals.containsKey(currentLocale)) {
                    currentLocale = "en";
                    ManualHelper.SetLocalOnly(this, currentLocale);
                }

                //Check all Rescue Manuals
                for (PdfVersion currentPdfVersion : pocketManuals.RescueManuals) {
                    if (currentPdfVersion.version != ManualHelper.GetVersion(this, currentPdfVersion.manual_code)) {
                        Log.v("Main_Logger", "Version Mis-Match: " + currentPdfVersion.manual_code + ". Downloading latest. API: " + ManualHelper.GetVersion(this, currentPdfVersion.manual_code) + " Device Version: " + currentPdfVersion.version);
                        new PdfClient().execute(new String[]{currentPdfVersion.location, currentPdfVersion.manual_code, this.getFilesDir().getAbsolutePath()}).get();
                        ManualHelper.SetVersion(this, currentPdfVersion.version, currentPdfVersion.manual_code);
                    } else {
                        Log.v("Main_Logger", "Local Version Found");
                    }
                }

                //Check all Language Manuals
                for (PdfVersion currentPdfVersion : pocketManuals.PublicManuals.get(currentLocale)) {
                    if (currentPdfVersion.version != ManualHelper.GetVersion(this, currentPdfVersion.manual_code)) {
                        Log.v("Main_Logger", "Version Mis-Match: " + currentPdfVersion.manual_code + ". Downloading latest. API: " + ManualHelper.GetVersion(this, currentPdfVersion.manual_code) + " Device Version: " + currentPdfVersion.version);
                        new PdfClient().execute(new String[]{currentPdfVersion.location, currentPdfVersion.manual_code, this.getFilesDir().getAbsolutePath()}).get();
                        ManualHelper.SetVersion(this, currentPdfVersion.version, currentPdfVersion.manual_code);
                    } else {
                        Log.v("Main_Logger", "Local Version Found");
                    }
                }
                ManualHelper.SetManualList(this, pocketManuals);
            } catch (Exception e) {
                Log.v("Error_Logger", e.toString());
            }
        }
    }
}