package eu.viaticumrescue.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final TextView detailsTextView = (TextView) findViewById(R.id.details);
        detailsTextView.setText(getResources().getString(R.string.current_language) + ManualHelper.GetLanguage(this));

        Spinner spinner = (Spinner) findViewById(R.id.select_language_spinner);
        try {
            Object pocketManuals = new ViaticumApiClient().execute().get();
            if (pocketManuals == null) {
                Log.e("About_Screen", "Unable to get List from Server");
                spinner.setVisibility(View.GONE);
                detailsTextView.setText(getResources().getString(R.string.current_language) + ManualHelper.GetLanguage(this) + "\nCannot connect to Servers");
            } else {
                HashMap<String, List<PdfVersion>> pdfVersions = ((PocketManuals) pocketManuals).PublicManuals;


                final List<String> list = new ArrayList<String>();
                list.add(getResources().getString(R.string.change_language));
                Iterator iterator = pdfVersions.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry me2 = (Map.Entry) iterator.next();
                    list.add((String) me2.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position > 0) {
                            String newlySelectLocale = list.get(position);
                            Log.v("Language_Change", "Changing language to: " + newlySelectLocale);
                            ManualHelper.SetLocalOnly(parentView.getContext(), newlySelectLocale);
                            Intent myIntent = new Intent(AboutActivity.this, LoadingScreen.class);
                            AboutActivity.this.startActivity(myIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            spinner.setVisibility(View.GONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            spinner.setVisibility(View.GONE);
        }
    }
}
