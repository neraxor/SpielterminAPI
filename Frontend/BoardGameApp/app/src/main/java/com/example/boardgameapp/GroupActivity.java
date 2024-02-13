package com.example.boardgameapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielgruppeDTO;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GroupActivity extends AppCompatActivity {
    private SpielgruppeDTO spielgruppeDTO;
    private BoardgameAPI boardgameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        boardgameAPI = new BoardgameAPI(this);
        appNavigation();
        spielgruppeDTO = (SpielgruppeDTO) getIntent().getSerializableExtra("SpielgruppeDTO");
        InitializeDatetimePicker();

        LoadGruppenDaten();
    }
    private void LoadGruppenDaten(){
        TextView gruppenName = findViewById(R.id.gruppenName);
        TextView gruppenNummer = findViewById(R.id.gruppenNummer);
        gruppenNummer.setText(String.valueOf(spielgruppeDTO.getId()));
        gruppenName.setText("DefaultName");
        LoadSpieltermine(false);
    }
    private void LoadSpieltermine(Boolean filterByDate) {
        boardgameAPI.GetSpieltermineByGruppe(filterByDate,spielgruppeDTO.getId(), new SpielterminCallback() {
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
            });

            container.addView(cardView);
        }
    }
    private void InvitePlayer(){
        TextView spielerName = findViewById(R.id.SpielerInvite);
        String spieler = spielerName.getText().toString();
        if(spieler.isEmpty()){
            return;
        }
        boardgameAPI.AddSpielerToSpielgruppe(spieler,spielgruppeDTO.getId());
    }
    private void InitializeDatetimePicker(){
        Button einladungsButton = findViewById(R.id.einladungButton);
        einladungsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InvitePlayer();
            }
        });
        EditText datumGruppenName = findViewById(R.id.datumGruppenName);
        Button btnTerminFestlegenGruppenName = findViewById(R.id.btnTerminFestlegenGruppenName);

        btnTerminFestlegenGruppenName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datum = datumGruppenName.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                Date date;
                try {
                    date = dateFormat.parse(datum);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (!datum.isEmpty()) {
                    boardgameAPI.CreateSpieltermin(spielgruppeDTO.getId(),date);
                    LoadSpieltermine(true);
                }
            }
        });
        datumGruppenName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(GroupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(GroupActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                String formattedDate = String.format("%02d.%02d.%d %02d:%02d", dayOfMonth, monthOfYear + 1, year, hourOfDay, minute);
                                                datumGruppenName.setText(formattedDate);
                                            }
                                        }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
    private void appNavigation(){
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonHome = findViewById(R.id.home);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
