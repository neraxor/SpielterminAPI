package com.example.boardgameapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView benutzernamen = findViewById(R.id.Benutzernamen);
        TextView password = findViewById(R.id.Password);
        TextView vorname = findViewById(R.id.Vorname);
        TextView nachname = findViewById(R.id.Nachname);
        TextView wohnort = findViewById(R.id.Wohnort);
        TextView strasse = findViewById(R.id.Strasse);
        TextView hausnummer = findViewById(R.id.hausnummer);
        TextView plz = findViewById(R.id.Postleitzahl);

        btnRegister.setOnClickListener(v -> {
            if (benutzernamen.getText().toString().isEmpty() || password.getText().toString().isEmpty() || vorname.getText().toString().isEmpty() || nachname.getText().toString().isEmpty() || wohnort.getText().toString().isEmpty() || strasse.getText().toString().isEmpty() || hausnummer.getText().toString().isEmpty() || plz.getText().toString().isEmpty()) {
                popUp("Fehler", "Bitte füllen Sie alle Felder aus",false);
            }
            else{
             register(benutzernamen.getText().toString().trim(), password.getText().toString().trim(), vorname.getText().toString().trim(), nachname.getText().toString().trim(), wohnort.getText().toString().trim(), strasse.getText().toString().trim(), hausnummer.getText().toString().trim(), plz.getText().toString().trim());
            }
        });
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
                    popUp("Registierung", "Registrierung erfolgreich",true);
                }
                else{
                    popUp("Registierung", "Registrierung fehlgeschlagen",false);
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
    public void popUp(String title ,String message, Boolean success){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (success) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}
