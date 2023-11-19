package com.example.boardgameapp.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.boardgameapp.Spielgruppe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BoardgameAPI extends AppCompatActivity {
    //hier folgen nun alle querys für die api
    //die query ist immer recht ähnlich aufgebaut und entweder ein post oder ein get
    //aktuell hab ich noch meine lokale ip angegeben, die muss dann noch geändert werden bzw in eine config
    //get
    private void GetGastgeberAdresse(int SpielterminID) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", SpielterminID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/GetGastgeberAdresse").newBuilder();
        urlBuilder.addQueryParameter("spielterminId", String.valueOf(SpielterminID));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    try {
                        Spielgruppe spielgruppe = new Spielgruppe();
                        spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
    //post
    private void CreateAbendbewertung(int spielterminId, int gastgeberBewertung, int essensBewertung, int abendBewertung,int spielerId) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("abendbewertungName", abendBewertung);
            jsonObject.put("essensbewertung", essensBewertung);
            jsonObject.put("gastgeberbewertung", gastgeberBewertung);
            jsonObject.put("spielerId", spielerId);
            // leere Daten fürs DTO
            jsonObject.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Abendbewertung/create-abendbewertung")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //Get
    private void GetBewertungenByGruppe(int spielgruppeId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Abendbewertung/GetBewertungenByGruppe").newBuilder();
        urlBuilder.addQueryParameter("spielgruppeId", String.valueOf(spielgruppeId));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //get
    private void GetBewertungenByTermin(int spielterminId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Abendbewertung/GetBewertungenByTermin").newBuilder();
        urlBuilder.addQueryParameter("spielterminId", String.valueOf(spielterminId));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //Post
    private void Essensabstimmen(int spielterminId, int essensrichtungId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("essensrichtungId", essensrichtungId);
            // leere Daten fürs DTO
            jsonObject.put("id", 0);
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Essensabstimmung/Essenabstimmen")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //Get
    private void GetSiegerEssensrichtung(int spielterminId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Essensabstimmung/GetSiegerEssensrichtung").newBuilder();
        urlBuilder.addQueryParameter("spielterminId", String.valueOf(spielterminId));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }

    // GetEssensrichtungen
    private void Esseensrichtung(){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Essensrichtung").newBuilder();
        String url = urlBuilder.build().toString();
        String token = getToken();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //post
    //RequiresApi wird für localdatetime benötigt
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNachricht(int spielgruppeId, String nachrichtText){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
            jsonObject.put("nachrichtText", nachrichtText);
            // leere Daten fürs DTO
            jsonObject.put("id", 0);
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            String formattedDateTime = now.format(formatter);
            if (formattedDateTime != null) {
                jsonObject.put("uhrzeit", formattedDateTime);
            }
            else {
                jsonObject.put("uhrzeit", "");

            }
            jsonObject.put("absenderId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Nachricht/create-nachricht")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //get
    private void GetNachrichten(int spielgruppeId, int anzahlNachrichten, boolean limitAnzahl){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
            jsonObject.put("anzahlNachrichten", anzahlNachrichten);
            jsonObject.put("limitAnzahl", limitAnzahl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Nachricht/get-nachrichten").newBuilder();
        urlBuilder.addQueryParameter("spielgruppeId", String.valueOf(spielgruppeId));
        urlBuilder.addQueryParameter("anzahlNachrichten", String.valueOf(anzahlNachrichten));
        urlBuilder.addQueryParameter("limitAnzahl", String.valueOf(limitAnzahl));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //post
    private void CreateSpielabstimmung(int spielgruppeId,int spielvorschlagId, int zustimmung){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
            jsonObject.put("spielvorschlagId", spielvorschlagId);
            jsonObject.put("zustimmung", zustimmung);
            // leere Daten fürs DTO
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Spielabstimmung/create-spielabstimmung")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //get Spielgruppe
    private void GetSpielgruppe(){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Spielgruppe").newBuilder();
        String url = urlBuilder.build().toString();
        String token = getToken();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //post - Spielgruppe
    private void CreateSpielgruppe(String name){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            // leere Daten fürs DTO
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Spielgruppe")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //post AddSpieler
    private void AddSpielerToSpielgruppe(String username, int spielgruppeId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("username", username);
            jsonObject.put("spielgruppeId", spielgruppeId);
            // leere Daten fürs DTO
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/api/Spielgruppe/AddSpieler").newBuilder();
        urlBuilder.addQueryParameter("username", String.valueOf(username));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //get
    private void GetSpieltermineBySpieler(boolean filterByDate){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filterByDate", filterByDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/GetSpieltermineBySpieler").newBuilder();
        urlBuilder.addQueryParameter("filterByDate", String.valueOf(filterByDate));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //get
    private void GetSpieltermineByGruppe(boolean filterByDate, int SpielgruppenId){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filterByDate", filterByDate);
            jsonObject.put("SpielgruppenId", SpielgruppenId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.0.133:7063/GetSpieltermineByGruppe").newBuilder();
        urlBuilder.addQueryParameter("filterByDate", String.valueOf(filterByDate));
        urlBuilder.addQueryParameter("SpielgruppenId", String.valueOf(SpielgruppenId));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                    }
                    //ergebnise setzen wie z.b.
                    //spielgruppe.setGastgeberOrt(jsonObject.getString("wohnort"));
                }
            }
        });
    }
    //post
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateSpieltermin(int spielgruppeId, Date termin){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("username", username);
            jsonObject.put("spielgruppeId", spielgruppeId);

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDateTime = isoFormat.format(termin);
            jsonObject.put("termin", formattedDateTime);

            //jsonObject.put("termin", termin);
            // leere Daten fürs DTO
            jsonObject.put("gastgeberId", 0);
            jsonObject.put("id", 0);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Spieltermin/create-spieltermin")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    //post
    private void CreateSpielvorschlag(int spielterminId, String spielvorschlagName)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("spielvorschlagName", spielvorschlagName);
            // leere Daten fürs DTO
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Spielvorschlag/create-spielvorschlag")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }

    public String getToken() {
        try {
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "encrypted_prefs",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return sharedPreferences.getString("token", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
