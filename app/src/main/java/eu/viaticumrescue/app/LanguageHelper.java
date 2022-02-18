package eu.viaticumrescue.app;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageHelper {
    public static void UpdateLocale(Context currentContext) {
        String languageToLoad = ManualHelper.GetLanguage(currentContext); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        currentContext.getResources().updateConfiguration(config, currentContext.getResources().getDisplayMetrics());
    }
}
