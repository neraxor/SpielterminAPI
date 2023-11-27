package com.example.boardgameapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // hier kann man z.b. beim laden alle nötigen querys abfeuern und die daten in unseren datenlayer speichern
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    private void appNavigation(){
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonProfil = findViewById(R.id.profil);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profil.class);
                startActivity(intent);
            }
        });
    }

}
