package eu.viaticumrescue.app;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static eu.viaticumrescue.app.R.color.viaticum_text_color;

public class RescueManuals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_manuals);
        View rescueManualsView = findViewById(R.id.rescue_manuals_linear_layout);

        for (PdfVersion currentPdf : ManualHelper.GetManualList(this).RescueManuals) {
            // Add card view
            CardView newCardView = new CardView(this);
            int margin = ManualHelper.ConvertDpToPixel(30, this);
            newCardView.setMinimumHeight(margin);
            newCardView.setRadius(ManualHelper.ConvertDpToPixel(8, this));

            //Add Linear Layout
            View childLinearLayout = new LinearLayout(this);
            childLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.viaticum_red));
            childLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            newCardView.addView(childLinearLayout);

            //Add Text View
            TextView currentManual = new TextView(this);
            currentManual.setText(currentPdf.name);
            currentManual.setTextColor(ContextCompat.getColor(this, viaticum_text_color));
            currentManual.setTextSize(20);
            currentManual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            currentManual.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            ((LinearLayout) childLinearLayout).addView(currentManual);
            currentManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RescueManuals.this, MainActivity.class);
                    TextView textview = (TextView) view;
                    String currentManualName = textview.getText().toString();

                    PdfVersion result = null;

                    //This is done on purpose due to Android limitations
                    for (PdfVersion currentPdf : ManualHelper.GetManualList(view.getContext()).RescueManuals) {
                        if (currentPdf.name.equals(currentManualName)) {
                            result = currentPdf;
                        }
                    }

                    Bundle b = new Bundle();
                    b.putString("current_manual", result.manual_code);
                    intent.putExtras(b);
                    RescueManuals.this.startActivity(intent);
                    finish();
                }
            });

            ((LinearLayout) rescueManualsView).addView(newCardView);

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) newCardView.getLayoutParams();
            layoutParams.setMargins(margin, margin, margin, 0);
        }
    }
}