package com.example.boardgameapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //register("testuser", "testpassword", "testvorname", "testnachname", "testwohnort", "teststraße", "testhausnummer", "testplz");
    }

    private void register(String username, String password, String vorname, String nachname, String wohnort, String straße, String hausnummer, String plz) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("vorname", vorname);
            jsonObject.put("nachname", nachname);
            jsonObject.put("wohnort", wohnort);
            jsonObject.put("straße", straße);
            jsonObject.put("hausnummer", hausnummer);
            jsonObject.put("plz", plz);
            // leere Daten fürs DTO
            //jsonObject.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = getToken();
        RequestBody body = RequestBody.Companion.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://192.168.0.133:7063/api/Auth/register")
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
