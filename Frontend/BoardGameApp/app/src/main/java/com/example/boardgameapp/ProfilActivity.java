package com.example.boardgameapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boardgameapp.Callbacks.CreateSpielgruppeCallback;
import com.example.boardgameapp.Callbacks.ProfilCallback;
import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielgruppeDTO;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {
    private BoardgameAPI boardgameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        appNavigation();
        boardgameAPI = new BoardgameAPI(this);
        loadProfilGruppen();
        final Button Gruppeerstellen = findViewById(R.id.Gruppeerstellen);
        Gruppeerstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGruppe();
            }
        });
    }

    private void appNavigation(){
        ImageButton buttonProfil = findViewById(R.id.profil);
        ImageButton buttonHome = findViewById(R.id.home);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProfilGruppen(){
        boardgameAPI.GetSpielgruppe( new ProfilCallback() {
            @Override
            public void onSuccess(ArrayList<SpielgruppeDTO> spielgruppeDTOS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addCardViewsDynamically(spielgruppeDTOS);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    private void addCardViewsDynamically(List<SpielgruppeDTO> spielGruppenListe) {
        LinearLayout container = findViewById(R.id.containerForCards);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < spielGruppenListe.size(); i++) {
            final int index = i;
            SpielgruppeDTO spielGruppe = spielGruppenListe.get(i);
            View cardView = inflater.inflate(R.layout.group_overview, container, false);

            TextView gruppenName = cardView.findViewById(R.id.gruppenName);
            //TextView spieler = cardView.findViewById(R.id.spieler);
            TextView gruppe = cardView.findViewById(R.id.gruppe);
            gruppenName.setText(spielGruppenListe.get(index).getName().toString());
            //gruppenName.setText("DefaultName");
            gruppe.setText(spielGruppenListe.get(index).getId().toString());
            //spieler.setText("DefaultSpieler");
            //gruppe.setText(spielGruppenListe.get(index));
            cardView.setOnClickListener(v -> {
                Log.d("MainActivity", "LinearLayout angeklickt! Index: " + index);
                Intent intent = new Intent(ProfilActivity.this, GroupActivity.class);
                intent.putExtra("SpielgruppeDTO", spielGruppenListe.get(index)); // "spielterminDto" ist der Key
                startActivity(intent);
            });
            container.addView(cardView);
        }
    }
    private void CreateGruppe(){
        EditText newGruppenName = findViewById(R.id.newGruppenName);
        if (newGruppenName.getText().toString().trim().isEmpty()) {
            popUp("Gruppe", "Gruppenname darf nicht leer sein");
            return;
        }
        boardgameAPI.CreateSpielgruppe(newGruppenName.getText().toString(), new CreateSpielgruppeCallback() {
            @Override
            public void onSuccess(SpielgruppeDTO spielgruppeDTO) {
                //boardgameAPI.CreateSpieltermin(spielgruppeDTO.getId(), date);
                //loadProfilGruppen();
                newGruppenName.setText("");
                popUp("Gruppe","Gruppe wurde erstellt");
            }
            @Override
            public void onFailure(Exception e) {
                popUp("Gruppe","Gruppe wurde nicht erstellt");
            }
        });
    }
    public void popUp(String title ,String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
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