package com.example.boardgameapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

public class MainActivity extends AppCompatActivity {

    // hier kann man z.b. beim laden alle nötigen querys abfeuern und die daten in unseren datenlayer speichern
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Test Get
        //GetGastgeberAdresse(1);
        //Test Post
        //CreateAbendbewertung(1,6,5,3,1);

        //Get
        //GetBewertungenByGruppe(1);
        //GetBewertungenByTermin(1);
        //GetSiegerEssensrichtung(1);
        //Esseensrichtung();
        //GetNachrichten(1,5,true);
        //GetSpielgruppe();
        //GetSpieltermineBySpieler(true);
        //GetSpieltermineByGruppe(true,1);
        //Post aufrufe
        //Essensabstimmen(1,1);
        //CreateNachricht(1,"Test");
        //CreateSpielabstimmung(1,1,1));
        //CreateSpielgruppe("Test");
        //AddSpielerToSpielgruppe("1003",2);
        //CreateSpieltermin(1, new Date());
        //CreateSpielvorschlag(1,"Test");
    }
}
