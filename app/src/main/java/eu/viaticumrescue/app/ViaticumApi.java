package eu.viaticumrescue.app;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

class ViaticumApiClient extends AsyncTask<Void, Void, PocketManuals> {

    protected PocketManuals doInBackground(Void... voids) {
        try{
            ANRequest request = AndroidNetworking.get("https://www.viaticumrescue.eu/mobile_app/public_pocket_manuals.php")
                    .setPriority(Priority.HIGH)
                    .build();
            ANResponse response = request.executeForString();
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<PocketManuals>() {
            }.getType();

            return gson.fromJson(response.getResult().toString(), type);
        } catch (Exception e) {
            Log.e("ViaticumApiClient", e.toString());
        }
        return null;
    }

}

class PdfClient extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... passing) {
        ANRequest request = AndroidNetworking.download(passing[0], passing[2], passing[1] + ".pdf")
                .setPriority(Priority.HIGH)
                .build();
        ANResponse response = request.executeForDownload();
        return null;
    }
}

class PocketManuals {
    List<PdfVersion> RescueManuals;
    HashMap<String, List<PdfVersion>> PublicManuals;
}

class PdfVersion {
    String name;
    String location;
    String manual_code;
    Integer version;
}