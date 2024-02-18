package com.example.boardgameapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boardgameapp.Callbacks.BasicCallback;
import com.example.boardgameapp.Callbacks.GastgeberAdresseCallback;
import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielerAdvancedDto;
import com.example.boardgameapp.DTO.SpielerDto;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private BoardgameAPI boardgameAPI;
    private ArrayList<SpielterminDto> spieltermineList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appNavigation();
        boardgameAPI = new BoardgameAPI(this);
        LoadSpieltermine(true);
    }
    private Map<Integer, SpielerAdvancedDto> adressenMap = new HashMap<>();

    private void addCardViewsDynamically(List<SpielterminDto> spieltermineList) {
        LinearLayout container = findViewById(R.id.containerForCards);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < spieltermineList.size(); i++) {
            final int index = i;
            final SpielterminDto spieltermin = spieltermineList.get(i);
            View cardView = inflater.inflate(R.layout.termin_view, container, false);

            TextView gruppenName = cardView.findViewById(R.id.gruppenName);
            TextView gruppenId = cardView.findViewById(R.id.terminId);
            TextView ort = cardView.findViewById(R.id.ort);
            TextView termin = cardView.findViewById(R.id.termin);
            TextView tvgastgeber = cardView.findViewById(R.id.gastgeber);
            gruppenId.setText(String.valueOf(spieltermin.getId()));
            LocalDateTime datum = spieltermin.getTermin();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formatiertesDatum = datum.format(formatter);
            termin.setText(formatiertesDatum);


            SpielerAdvancedDto spielerAdvancedDto = adressenMap.get(spieltermin.getId());
            if (spielerAdvancedDto != null) {
                SpielerDto gastgeber = spielerAdvancedDto.getSpielerDto();
                ort.setText(gastgeber.getWohnort() + " " + gastgeber.getPlz()+" "+gastgeber.getStraße()+" "+gastgeber.getHausnummer());
                tvgastgeber.setText(gastgeber.getVorname()+" "+gastgeber.getNachname());
                gruppenName.setText(spielerAdvancedDto.getGruppenName());
            }
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TerminActivity.class);
                intent.putExtra("spieltermin", spieltermin);
                startActivity(intent);
            });
            container.addView(cardView);
        }
    }
    private void appNavigation(){
        ImageButton buttonProfil = findViewById(R.id.profil);
        ImageButton buttonHome = findViewById(R.id.home);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
    private void LoadSpieltermine(Boolean filterByDate){
        boardgameAPI.GetSpieltermineBySpieler(filterByDate, new SpielterminCallback() {
            @Override
            public void onSuccess(ArrayList<SpielterminDto> spielterminDto) {
                spieltermineList= spielterminDto;
                for (SpielterminDto spieltermin : spielterminDto) {
                    loadAdresseForSpieltermin(spieltermin);
                }
                runOnUiThread(() ->  {
                        if (adressenMap.size() == spieltermineList.size()) {
                            addCardViewsDynamically(spielterminDto);
                        }
                });
            }
            @Override
            public void onFailure(Exception e) {
                spieltermineList = new ArrayList<>();
            }
        });
    }
    // Adressen vom Gastgeber des Spieltermins vorausladen
    private void loadAdresseForSpieltermin(SpielterminDto spieltermin) {
        SpielerAdvancedDto spielerAdvancedDto = new SpielerAdvancedDto();
        boardgameAPI.GetGastgeberAdresse(spieltermin.getId(), new GastgeberAdresseCallback() {
            @Override
            public void onSuccess(SpielerDto response) {
                if (response != null) {
                    spielerAdvancedDto.setSpielerDto(response);
                    loadGruppennamen(spieltermin.getId(),spieltermin.getSpielgruppeId(), spielerAdvancedDto);
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadGruppennamen(int spielterminId,int spielgruppeId, SpielerAdvancedDto spielerAdvancedDto) {
        boardgameAPI.getSpielgruppeById(spielgruppeId, new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                spielerAdvancedDto.setGruppenName(response);
                adressenMap.put(spielterminId, spielerAdvancedDto);
                runOnUiThread(() ->  {
                    if (adressenMap.size() == spieltermineList.size()) {
                        addCardViewsDynamically(spieltermineList);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
}
