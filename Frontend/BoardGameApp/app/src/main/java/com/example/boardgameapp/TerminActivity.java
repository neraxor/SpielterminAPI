package com.example.boardgameapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgameapp.Callbacks.BasicCallback;
import com.example.boardgameapp.Callbacks.GastgeberAdresseCallback;
import com.example.boardgameapp.Callbacks.GetEssensrichtungCallback;
import com.example.boardgameapp.Callbacks.GetSpielerCallback;
import com.example.boardgameapp.Callbacks.GetSpielvorschlagCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.Essensrichtung;
import com.example.boardgameapp.DTO.SpielerDto;
import com.example.boardgameapp.DTO.SpielterminDto;
import com.example.boardgameapp.DTO.SpielvorschlagDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TerminActivity extends AppCompatActivity implements BasicCallback {
    @Override
    public void onSuccess(String response) {
        popUp("Spielabstimmung", response);
    }

    @Override
    public void onFailure(Exception e) {
        popUp("Spielabstimmung", e.getMessage());
    }
    private BoardgameAPI boardgameAPI;
    private SpielterminDto spieltermin;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termin);
        appNavigation();
        boardgameAPI = new BoardgameAPI(this);
        setupAbstimmungen();
        setupEssensrichtung();
        spieltermin = (SpielterminDto) getIntent().getSerializableExtra("spieltermin");
        setupHeadDaten();
        setupSpinner();
        setupVerspätung();
        setupSpielvorschlag();
        fillTeilnehmerListe();
        loadSpielvorschlaege(spieltermin.getId());
        loadSiegerEssensrichtung();
    }
    private void loadSiegerEssensrichtung() {
        TextView siegerEssensrichtung = findViewById(R.id.tvGewinnerEssen);
        if(spieltermin.getGastgeberId() != boardgameAPI.getSpielerIdFromToken()){
            return;
        }
        boardgameAPI.GetSiegerEssensrichtung(spieltermin.getId(), new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    siegerEssensrichtung.setText("Sieger der Essensabstimmung: " + response);
                });
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadSpielvorschlaege(int spielterminId) {
        List<SpielvorschlagDto> spielvorschlaegeListe;
        SpielvorschlagAdapter adapter;
        RecyclerView recyclerView;
        spielvorschlaegeListe = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewSpielvorschlaege);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SpielvorschlagAdapter(new ArrayList<>(), boardgameAPI, spieltermin,this);
        recyclerView.setAdapter(adapter);
        boardgameAPI.getSpielvorschlag(spielterminId, new GetSpielvorschlagCallback() {
            @Override
            public void onSuccess(List<SpielvorschlagDto> spielvorschlaege) {
                runOnUiThread(() -> {
                    spielvorschlaegeListe.clear();
                    spielvorschlaegeListe.addAll(spielvorschlaege);
                    adapter.updateSpielvorschlaege(spielvorschlaegeListe);
                });
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void appNavigation(){
        ImageButton buttonProfil = findViewById(R.id.profil);
        ImageButton buttonHome = findViewById(R.id.home);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TerminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TerminActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setupVerspätung(){
        Button verspätungSenden = findViewById(R.id.VerspätungSenden);
        verspätungSenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerspätung();
            }
        });
    }
    private void setupSpielvorschlag(){
        Button einladungSenden = findViewById(R.id.einladungButton);
        einladungSenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSpielvorschlag();
            }
        });
    }
    private void SendSpielvorschlag(){
        TextView tvSpielvorschlag = findViewById(R.id.Spielvorschlag);
        String spielvorschlag =tvSpielvorschlag.getText().toString();
        if (spielvorschlag.trim().isEmpty()){
            popUp("Spielvorschlag", "Bitte geben Sie einen Spielvorschlag ein");
            return;
        }
        boardgameAPI.CreateSpielvorschlag(spieltermin.getId(),spielvorschlag,new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Spielvorschlag", response);
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void SendVerspätung(){
        TextView verspätung = findViewById(R.id.Verspätung);
        int verspätungsWert = 0;
        try {
            verspätungsWert = Integer.parseInt(verspätung.getText().toString().trim());
        }
        catch(NumberFormatException e){
            popUp("Verspätung", "Bitte geben Sie eine Zahl ein");
            return;
        }
        if (verspätungsWert <= 0){
            popUp("Verspätung", "Bitte geben Sie eine Zahl ein");
            return;
        }
        boardgameAPI.CreateNachricht(spieltermin.getSpielgruppeId(),verspätungsWert,spieltermin.getId(),new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Verspätung", response);
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void setupHeadDaten(){
        TextView termin = findViewById(R.id.tvSpielTermin);
        LocalDateTime datum = spieltermin.getTermin();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatiertesDatum = datum.format(formatter);
        termin.setText(formatiertesDatum);
        //termin.setText(spieltermin.getTermin().toString());
        loadAdresseForSpieltermin();
        loadGruppennamen(spieltermin.getSpielgruppeId());
    }

    private void loadAdresseForSpieltermin() {
        TextView gastgeber = findViewById(R.id.tvGastgeber);
        TextView adresse = findViewById(R.id.tvAdresse);
        boardgameAPI.GetGastgeberAdresse(spieltermin.getId(), new GastgeberAdresseCallback() {
            @Override
            public void onSuccess(SpielerDto response) {
                if (response != null) {
                    runOnUiThread(() -> {
                        gastgeber.setText(response.getVorname()+" "+response.getNachname());
                        adresse.setText(response.getWohnort()+" "+ response.getPlz()+" "+response.getStraße()+" "+response.getHausnummer());
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void loadGruppennamen(int spielgruppeId) {
        boardgameAPI.getSpielgruppeById(spielgruppeId, new BasicCallback() {
            TextView gruppenName = findViewById(R.id.tvGruppenname);
            @Override
            public void onSuccess(String response) {
                Log.d("MainActivity", "SpielgruppeByIdCallback onSuccess: " + response);
                if (response != null) {
                    runOnUiThread(() -> {
                        gruppenName.setText(response);
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("MainActivity", "SpielgruppeByIdCallback onFailure: " + e.getMessage());
            }
        });
    }
    private void setupAbstimmungen(){
        Button btnEssenSenden = findViewById(R.id.BewertungSenden);
        btnEssenSenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendeAbendBewertung();
            }
        });
    }
    private void setupEssensrichtung(){
        Button essensRichtung = findViewById(R.id.btnEssensauswahl);
        essensRichtung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                essensrichtungWahl();
            }
        });
    }
    private void essensrichtungWahl(){
        Spinner abstimmungEssenSpinner = findViewById(R.id.abstimmungEssenSpinner);
        Essensrichtung selectedEssensrichtung = (Essensrichtung) abstimmungEssenSpinner.getSelectedItem();
        boardgameAPI.Essensabstimmen(spieltermin.getId(), selectedEssensrichtung.getId(), new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Abstimmung", response);
                loadSiegerEssensrichtung();
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void sendeAbendBewertung() {
        TextView abend = findViewById(R.id.Abend);
        TextView essen = findViewById(R.id.Essen);
        TextView gastgeber = findViewById(R.id.Gastgeber);
        int essenswert = 0;
        int gastgeberwert = 0;
        int abendwert = 0;
        try {
            essenswert = Integer.parseInt(essen.getText().toString().trim());
            gastgeberwert = Integer.parseInt(gastgeber.getText().toString().trim());
            abendwert = Integer.parseInt(abend.getText().toString().trim());
            if (essenswert < 1 || essenswert > 10) {
                popUp("Fehler", "Bitte geben Sie für das Essen einen Wert zwischen 1 und 10 ein.");
                return;
            }
            if (gastgeberwert < 1 || gastgeberwert > 10) {
                popUp("Fehler", "Bitte geben Sie für den Gastgeber einen Wert zwischen 1 und 10 ein.");
                return;
            }
            if (abendwert < 1 || abendwert > 10) {
                popUp("Fehler", "Bitte geben Sie für den Abend einen Wert zwischen 1 und 10 ein.");
                return;
            }
        } catch (NumberFormatException e) {
            popUp("Fehler", "Bitte alle 3 Felder füllen und nur numerische Werte eingeben.");
            return;
        }

        int spielerId = boardgameAPI.getSpielerIdFromToken();
        boardgameAPI.CreateAbendbewertung(spieltermin.getId(), gastgeberwert,essenswert,abendwert,spielerId, new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Bewertung", response);
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void setupSpinner() {
        Spinner abstimmungEssenSpinner = findViewById(R.id.abstimmungEssenSpinner);
        boardgameAPI.Essensrichtung(new GetEssensrichtungCallback(){
            @Override
            public void onSuccess(ArrayList<Essensrichtung>  response) {
                EssensrichtungAdapter adapter = new EssensrichtungAdapter(TerminActivity.this, android.R.layout.simple_spinner_item, response);
                abstimmungEssenSpinner.setAdapter(adapter);
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void fillTeilnehmerListe() {
        LinearLayout teilnehmerListe = findViewById(R.id.llTeilnehmer);
        boardgameAPI.GetSpieler(spieltermin.getSpielgruppeId(),spieltermin.getId(), new GetSpielerCallback() {
            @Override
            public void onSuccess(ArrayList<String> response) {
                for (String spieler : response) {
                    TextView teilnehmerView = new TextView(TerminActivity.this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                    layoutParams.setMargins(dp8, 0, 0, 0);
                    teilnehmerView.setLayoutParams(layoutParams);
                    //teilnehmerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    teilnehmerView.setText(spieler);
                    teilnehmerView.setTextColor(getResources().getColor(R.color.beige));
                    teilnehmerView.setTextSize(16);
                    teilnehmerListe.addView(teilnehmerView);
                }
            }
            @Override
            public void onFailure(Exception e) {
            }
        });

    }
    public void popUp(String title ,String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(TerminActivity.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}
