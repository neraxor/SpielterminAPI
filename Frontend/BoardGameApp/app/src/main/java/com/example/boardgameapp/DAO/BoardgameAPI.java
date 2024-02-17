package com.example.boardgameapp.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.boardgameapp.Callbacks.CreateAbendbewertungCallback;
import com.example.boardgameapp.Callbacks.CreateSpielgruppeCallback;
import com.example.boardgameapp.Callbacks.EssensabstimmenCallback;
import com.example.boardgameapp.Callbacks.GastgeberAdresseCallback;
import com.example.boardgameapp.Callbacks.GetEssensrichtungCallback;
import com.example.boardgameapp.Callbacks.GetSpielerCallback;
import com.example.boardgameapp.Callbacks.GetSpielvorschlagCallback;
import com.example.boardgameapp.Callbacks.NachrichtCallback;
import com.example.boardgameapp.Callbacks.ProfilCallback;
import com.example.boardgameapp.Callbacks.SpielgruppeByIdCallback;
import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.Callbacks.SpielvorschlagAbstimmungCallback;
import com.example.boardgameapp.Callbacks.SpielvorschlagCallback;
import com.example.boardgameapp.DTO.Essensrichtung;
import com.example.boardgameapp.DTO.SpielerDto;
import com.example.boardgameapp.DTO.SpielgruppeDTO;
import com.example.boardgameapp.DTO.SpielterminDto;
import com.example.boardgameapp.DTO.SpielvorschlagDto;
import com.example.boardgameapp.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private Context context;
    String localhost = "http://192.168.0.133:7063";

    public BoardgameAPI(Context contex) {
        this.context = contex;
    }
    //get
    public void GetGastgeberAdresse(int SpielterminID, GastgeberAdresseCallback callback) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", SpielterminID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/GetGastgeberAdresse").newBuilder();
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
                String responseData = response.body().string();
                if (response.isSuccessful())
                {
                    Gson gson = new Gson();
                    SpielerDto spielerDto = gson.fromJson(responseData, SpielerDto.class);
                    callback.onSuccess(spielerDto);
                }
            }
        });
    }
    //post
    public void CreateAbendbewertung(int spielterminId, int gastgeberBewertung, int essensBewertung, int abendBewertung, int spielerId, CreateAbendbewertungCallback callback) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("abendbewertungName", abendBewertung);
            jsonObject.put("essensbewertung", essensBewertung);
            jsonObject.put("gastgeberbewertung", gastgeberBewertung);
            jsonObject.put("spielerId", spielerId);
            jsonObject.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Abendbewertung/create-abendbewertung")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {e.printStackTrace();}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onSuccess(response.body().string());
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Abendbewertung/GetBewertungenByGruppe").newBuilder();
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Abendbewertung/GetBewertungenByTermin").newBuilder();
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
                }
            }
        });
    }
    //Post
    public void Essensabstimmen(int spielterminId, int essensrichtungId, EssensabstimmenCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("essensrichtungId", essensrichtungId);
            jsonObject.put("id", 0);
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Essensabstimmung/Essenabstimmen")
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
                String responseData = response.body().string();
                callback.onSuccess(responseData);
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Essensabstimmung/GetSiegerEssensrichtung").newBuilder();
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
                }
            }
        });
    }

    // GetEssensrichtungen
    public void Essensrichtung(GetEssensrichtungCallback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Essensrichtung").newBuilder();
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
                    Gson gson = GsonUtil.getGson();
                    Type listType = new TypeToken<ArrayList<Essensrichtung>>(){}.getType();
                    ArrayList<Essensrichtung> essensrichtungen = gson.fromJson(responseData, listType);
                    callback.onSuccess(essensrichtungen);
                }
            }
        });
    }

    // GetSpieler
    public void GetSpieler(int spielgruppeId,int spielterminId,GetSpielerCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SpielgruppenId", spielgruppeId);
            jsonObject.put("SpielterminId", spielterminId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/GetSpieler").newBuilder();
        urlBuilder.addQueryParameter("SpielgruppenId", String.valueOf(spielgruppeId));
        urlBuilder.addQueryParameter("SpielterminId", String.valueOf(spielterminId));
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
                String responseData = response.body().string();

                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    ArrayList<String> vornamenListe = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String vorname = jsonObject.getString("vorname");
                        int verspätung = Integer.parseInt(jsonObject.getString("verspätung"));
                        if (verspätung > 0 ) {
                            //ggf. noch Nachnamen oder Username nutzen
                            vorname = vorname +" "+ verspätung+" Min verspätet";
                        }
                        vornamenListe.add(vorname);
                    }
                    callback.onSuccess(vornamenListe);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } ;
            }
        });
    }
    //post
    //RequiresApi wird für localdatetime benötigt
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateNachricht(int spielgruppeId, int nachrichtText,int spielterminId, NachrichtCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
            jsonObject.put("nachrichtText", nachrichtText);
            jsonObject.put("spielterminId", spielterminId);
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
                .url(localhost+"/api/Nachricht/create-nachricht")
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
                String responseData = response.body().string();
                callback.onSuccess(responseData);
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Nachricht/get-nachrichten").newBuilder();
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
                }
            }
        });
    }
    //post
    public void CreateSpielabstimmung(int spielgruppeId, int spielvorschlagId, boolean zustimmung, SpielvorschlagAbstimmungCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
            jsonObject.put("spielvorschlagId", spielvorschlagId);
            jsonObject.put("zustimmung", zustimmung);
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Spielabstimmung/create-spielabstimmung")
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
                String responseData = response.body().string();
                callback.onSuccess(responseData);
            }
        });
    }
    //get Spielgruppe
    public void GetSpielgruppe(ProfilCallback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Spielgruppe").newBuilder();
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
                    Gson gson = GsonUtil.getGson();
                    Type spielgruppeListType = new TypeToken<ArrayList<SpielgruppeDTO>>(){}.getType();
                    ArrayList<SpielgruppeDTO> spielgruppen = gson.fromJson(responseData, spielgruppeListType);
                    callback.onSuccess(spielgruppen);
                }
            }
        });
    }

    public void getSpielgruppeById(int spielgruppeId, SpielgruppeByIdCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/GetSpielgruppeNameById").newBuilder();
        urlBuilder.addQueryParameter("spielgruppeId", String.valueOf(spielgruppeId));
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
                    callback.onSuccess(responseData);
                }
            }
        });
    }
    //post - Spielgruppe
    public SpielgruppeDTO CreateSpielgruppe(String name, CreateSpielgruppeCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Spielgruppe")
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();
        SpielgruppeDTO spielgruppeDTO = new SpielgruppeDTO();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    SpielgruppeDTO spielgruppeDTO = gson.fromJson(responseData, SpielgruppeDTO.class);
                    callback.onSuccess(spielgruppeDTO);
                }
                else
                {
                    callback.onFailure(new IOException("Anfrage fehlgeschlagen mit HTTP Code: " + response.code()));
                }
            }
        });
        return spielgruppeDTO;
    }
    //post AddSpieler
    public void AddSpielerToSpielgruppe(String username, int spielgruppeId, CreateAbendbewertungCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielgruppeId", spielgruppeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Spielgruppe/AddSpieler").newBuilder();
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
                String responseData = response.body().string();
                if (response.isSuccessful())
                {
                    callback.onSuccess("Spieler wurde hinzugefügt");
                }
                else{
                    callback.onSuccess(responseData);
                }
            }
        });
    }
    //get
    public ArrayList<SpielterminDto> GetSpieltermineBySpieler(Boolean filterByDate, SpielterminCallback callback) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filterByDate", filterByDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/GetSpieltermineBySpieler").newBuilder();
        urlBuilder.addQueryParameter("filterByDate", String.valueOf(filterByDate));
        String url = urlBuilder.build().toString();
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "Bearer " + token)
                .build();
        ArrayList<SpielterminDto> spielterminDto = new ArrayList<SpielterminDto>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // GsonUtil zum Anpassen vom Datum
                    Gson gson = GsonUtil.getGson();
                    Type spieltermineListType = new TypeToken<ArrayList<SpielterminDto>>(){}.getType();
                    ArrayList<SpielterminDto> spieltermine = gson.fromJson(responseData, spieltermineListType);
                    callback.onSuccess(spieltermine);
                }
            }
        });
        return spielterminDto;
    }
    //get
    public void GetSpieltermineByGruppe(boolean filterByDate, int SpielgruppenId, SpielterminCallback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filterByDate", filterByDate);
            jsonObject.put("SpielgruppenId", SpielgruppenId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/GetSpieltermineByGruppe").newBuilder();
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
                    Gson gson = GsonUtil.getGson();
                    Type spieltermineListType = new TypeToken<ArrayList<SpielterminDto>>(){}.getType();
                    ArrayList<SpielterminDto> spieltermine = gson.fromJson(responseData, spieltermineListType);
                    callback.onSuccess(spieltermine);
                }
            }
        });
    }
    //post
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateSpieltermin(int spielgruppeId, Date termin){
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
            jsonObject.put("gastgeberId", 0);
            jsonObject.put("id", 0);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Spieltermin/create-spieltermin")
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
    public void CreateSpielvorschlag(int spielterminId, String spielvorschlagName, SpielvorschlagCallback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("spielterminId", spielterminId);
            jsonObject.put("spielvorschlagName", spielvorschlagName);
            jsonObject.put("spielerId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(localhost+"/api/Spielvorschlag/create-spielvorschlag")
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

                String responseData = response.body().string();
                callback.onSuccess(responseData);
            }
        });
    }
    public void getSpielvorschlag(int spielterminId, GetSpielvorschlagCallback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(localhost+"/api/Spielvorschlag/get-spielvorschlag").newBuilder();
        urlBuilder.addQueryParameter("SpielterminId", String.valueOf(spielterminId));
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
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Type spielvorschlagListType = new TypeToken<ArrayList<SpielvorschlagDto>>(){}.getType();
                List<SpielvorschlagDto> spielvorschlaege = gson.fromJson(responseData, spielvorschlagListType);
                callback.onSuccess(spielvorschlaege);
            }
        });
    }
    public String getToken() {
        try {
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "encrypted_prefs",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return sharedPreferences.getString("token", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Integer getSpielerIdFromToken() {
        try {
            String token = getToken();
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = parts[1];
                String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
                JSONObject payloadObj = new JSONObject(decodedPayload);
                payloadObj.getString("SpielerId");
                return Integer.parseInt(payloadObj.getString("SpielerId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
