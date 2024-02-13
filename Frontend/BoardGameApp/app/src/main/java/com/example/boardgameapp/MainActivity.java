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
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BoardgameAPI boardgameAPI;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appNavigation();
        boardgameAPI = new BoardgameAPI(this);
        LoadSpieltermine(false);
    }

    private void addCardViewsDynamically(List<SpielterminDto> spieltermineList) {
        LinearLayout container = findViewById(R.id.containerForCards);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < spieltermineList.size(); i++) {
            final int index = i;
            final SpielterminDto spieltermin = spieltermineList.get(i);
            View cardView = inflater.inflate(R.layout.group_view, container, false);

            TextView gruppenName = cardView.findViewById(R.id.gruppenName);
            TextView gruppenId = cardView.findViewById(R.id.gruppe);
            TextView ort = cardView.findViewById(R.id.ort);
            TextView termin = cardView.findViewById(R.id.termin);
            TextView gastgeber = cardView.findViewById(R.id.gastgeber);
            gruppenName.setText("DefaultName");
            gruppenId.setText(String.valueOf(spieltermin.getSpielgruppeId()));
            ort.setText(String.valueOf(spieltermin.getGastgeberId()));
            termin.setText(spieltermin.getTermin().toString());
            gastgeber.setText(String.valueOf(spieltermin.getGastgeberId()));

            cardView.setOnClickListener(v -> {
                Log.d("MainActivity", "LinearLayout angeklickt! Index: " + index);
                Intent intent = new Intent(MainActivity.this, TerminActivity.class);
                intent.putExtra("spieltermin", spieltermin);
                //intent.putExtra("SpielgruppeDTO", spielGruppenListe.get(index));
                startActivity(intent);
            });

            container.addView(cardView);
        }
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
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
    private void LoadSpieltermine(Boolean filterByDate){
        boardgameAPI.GetSpieltermineBySpieler(filterByDate, new SpielterminCallback() {
            @Override
            public void onSuccess(ArrayList<SpielterminDto> spielterminDto) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addCardViewsDynamically(spielterminDto);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }


}
