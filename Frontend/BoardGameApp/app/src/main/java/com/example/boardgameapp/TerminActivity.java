package com.example.boardgameapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgameapp.Callbacks.CreateAbendbewertungCallback;
import com.example.boardgameapp.Callbacks.EssensabstimmenCallback;
import com.example.boardgameapp.Callbacks.GetEssensrichtungCallback;
import com.example.boardgameapp.Callbacks.GetSpielerCallback;
import com.example.boardgameapp.Callbacks.GetSpielvorschlagCallback;
import com.example.boardgameapp.Callbacks.NachrichtCallback;
import com.example.boardgameapp.Callbacks.SpielvorschlagAbstimmungCallback;
import com.example.boardgameapp.Callbacks.SpielvorschlagCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.Essensrichtung;
import com.example.boardgameapp.DTO.SpielterminDto;
import com.example.boardgameapp.DTO.SpielvorschlagDto;

import java.util.ArrayList;
import java.util.List;

public class TerminActivity extends AppCompatActivity implements SpielvorschlagAbstimmungCallback {
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
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonProfil = findViewById(R.id.profil);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TerminActivity.this, CreatGroup.class);
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

        boardgameAPI.CreateSpielvorschlag(spieltermin.getId(),spielvorschlag,new SpielvorschlagCallback() {
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
        int verspätungsWert = Integer.parseInt(verspätung.getText().toString());

        boardgameAPI.CreateNachricht(spieltermin.getSpielgruppeId(),verspätungsWert,spieltermin.getId(),new NachrichtCallback() {
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
        TextView adresse = findViewById(R.id.tvAdresse);
        TextView gastgeber = findViewById(R.id.tvGastgeber);
        TextView termin = findViewById(R.id.tvSpielTermin);

        adresse.setText(String.valueOf(spieltermin.getGastgeberId()));
        gastgeber.setText(String.valueOf(spieltermin.getGastgeberId()));
        termin.setText(spieltermin.getTermin().toString());
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
        boardgameAPI.Essensabstimmen(spieltermin.getId(), selectedEssensrichtung.getId(), new EssensabstimmenCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Abstimmung", response);
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
        int essenswert = Integer.parseInt(essen.getText().toString());
        int gastgeberwert = Integer.parseInt(gastgeber.getText().toString());
        int abendwert = Integer.parseInt(abend.getText().toString());
        int spielerId = boardgameAPI.getSpielerIdFromToken();
        boardgameAPI.CreateAbendbewertung(spieltermin.getId(), gastgeberwert,essenswert,abendwert,spielerId, new CreateAbendbewertungCallback() {
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
                    teilnehmerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    teilnehmerView.setText(spieler);
                    teilnehmerView.setTextColor(getResources().getColor(R.color.wheat));
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
