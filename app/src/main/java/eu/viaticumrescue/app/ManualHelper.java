package eu.viaticumrescue.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Locale;

public class ManualHelper {
    private static String PREFS_NAME = "viaAppStorage";
    private static String Pocket_Manuals_PREF_Name = "ViaAppStoragePocketManuals";
    private static String Version_PREF_Name = "ViaAppStorageVersion";
    private static String Language_PREF_Name = "ViaAppStorageLanguage";

    public static boolean IsNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.v("Network", "Network is available: " + networkAvailable);
        return networkAvailable;
    }

    public static String GetLanguage(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        if (settings.contains(Language_PREF_Name)) {
            String currentLanguage = settings.getString(Language_PREF_Name, null);
            Log.v("Storage_Logger", "Found Language On Disk: " + currentLanguage);
            return currentLanguage;
        } else {
            String currentLocale = Locale.getDefault().getLanguage();
            Log.v("Locale", currentLocale);
            return currentLocale;
        }
    }

    public static Integer GetVersion(Context context, String manualCode) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        if (settings.contains(GetManualCode(manualCode))) {
            Integer currentVersion = settings.getInt(GetManualCode(manualCode), 0);
            Log.v("Storage_Logger", "Found Version On Disk: " + settings.getInt(GetManualCode(manualCode), 0));
            return currentVersion;
        } else {
            return 0;
        }
    }

    public static void SetManualList(Context context, PocketManuals pocketManuals) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(pocketManuals);
        editor.putString(Pocket_Manuals_PREF_Name, jsonString);
        editor.commit();
    }

    public static PocketManuals GetManualList(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        if (settings.contains(Pocket_Manuals_PREF_Name)) {
            try {
                String pdfManualsString = settings.getString(Pocket_Manuals_PREF_Name, "");
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<PocketManuals>() {
                }.getType();
                return gson.fromJson(pdfManualsString, type);
            } catch (Exception e) {
                Log.e("ManualListError", e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public static void SetVersion(Context context, int version, String manual_code) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt(GetManualCode(manual_code), version);
        editor.commit();
    }

    public static void SetLocalOnly(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(Language_PREF_Name, language);
        editor.commit();
    }

    public static File GetManual(Context context, String manualName) {
        File file = new File(context.getFilesDir(), manualName + ".pdf");
        Log.v("PDF_Location", file.getAbsolutePath());
        return file;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int ConvertDpToPixel(int dp, Context context) {
        return dp * ((int) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String GetManualCode(String manualCode) {
        return Version_PREF_Name + manualCode;
    }
}
