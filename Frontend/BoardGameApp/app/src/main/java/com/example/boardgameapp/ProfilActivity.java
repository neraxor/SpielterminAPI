package com.example.boardgameapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    }

    private void appNavigation(){
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonHome = findViewById(R.id.home);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
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
            TextView spieler = cardView.findViewById(R.id.spieler);
            TextView gruppe = cardView.findViewById(R.id.gruppe);

            gruppenName.setText("DefaultName");
            spieler.setText("DefaultSpieler");
            gruppe.setText("DefaultGruppe");
            cardView.setOnClickListener(v -> {
                Log.d("MainActivity", "LinearLayout angeklickt! Index: " + index);
                Intent intent = new Intent(ProfilActivity.this, GroupActivity.class);
                intent.putExtra("SpielgruppeDTO", spielGruppenListe.get(index)); // "spielterminDto" ist der Key
                startActivity(intent);
            });
            container.addView(cardView);
        }
    }
}