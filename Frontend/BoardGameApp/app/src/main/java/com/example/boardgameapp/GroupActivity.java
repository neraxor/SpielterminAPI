package com.example.boardgameapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boardgameapp.Callbacks.BasicCallback;
import com.example.boardgameapp.Callbacks.GastgeberAdresseCallback;
import com.example.boardgameapp.Callbacks.SpielterminCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielerAdvancedDto;
import com.example.boardgameapp.DTO.SpielerDto;
import com.example.boardgameapp.DTO.SpielgruppeDTO;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class GroupActivity extends AppCompatActivity {
    private SpielgruppeDTO spielgruppeDTO;
    private BoardgameAPI boardgameAPI;
    private ArrayList<SpielterminDto> spieltermineList = new ArrayList<>();
    private Map<Integer, SpielerAdvancedDto> adressenMap = new HashMap<>();

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
        LoadSpieltermine(false);
    }
    private void addCardViewsDynamically(List<SpielterminDto> spieltermineList) {
        //header
        TextView gruppenNummerheader = findViewById(R.id.gruppenNummer);
        TextView gruppenNameheader = findViewById(R.id.gruppenName);
        SpielerAdvancedDto spielerAdvancedheader = null;
        if (!spieltermineList.isEmpty()) {;
            spielerAdvancedheader = adressenMap.get(spieltermineList.get(0).getSpielgruppeId());
        }
        if (spielerAdvancedheader != null) {
            gruppenNameheader.setText(String.valueOf(spielerAdvancedheader.getGruppenName()));
        }else{
                gruppenNameheader.setText("");
        }
        gruppenNummerheader.setText(String.valueOf(spielgruppeDTO.getId()));
        //body
        LinearLayout container = findViewById(R.id.containerForCards);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < spieltermineList.size(); i++) {
            final int index = i;
            final SpielterminDto spieltermin = spieltermineList.get(i);
            View cardView = inflater.inflate(R.layout.termin_view, container, false);

            TextView gruppenName = cardView.findViewById(R.id.gruppenName);
            TextView terminId = cardView.findViewById(R.id.terminId);
            TextView ort = cardView.findViewById(R.id.ort);
            TextView termin = cardView.findViewById(R.id.termin);
            TextView gastgeber = cardView.findViewById(R.id.gastgeber);

            gruppenName.setText(spieltermin.getGruppenName());
            SpielerAdvancedDto spielerAdvancedDto = adressenMap.get(spieltermin.getId());

            if (spielerAdvancedDto != null) {
                SpielerDto gastgeberdto = spielerAdvancedDto.getSpielerDto();
                gruppenName.setText(spielerAdvancedDto.getGruppenName());
                gastgeber.setText(gastgeberdto.getVorname() + " " + gastgeberdto.getNachname());
                ort.setText(gastgeberdto.getWohnort()+ " "+ gastgeberdto.getPlz() + " " + gastgeberdto.getStraße() + " " + gastgeberdto.getHausnummer());
            } else {
                gruppenName.setText("");
                gastgeber.setText("");
                ort.setText("");
            }

            terminId.setText(String.valueOf(spieltermin.getId()));
            LocalDateTime datum = spieltermin.getTermin();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formatiertesDatum = datum.format(formatter);
            termin.setText(formatiertesDatum);

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(GroupActivity.this, TerminActivity.class);
                intent.putExtra("spieltermin", spieltermin);
                startActivity(intent);
            });

            container.addView(cardView);
        }
    }
    private void InvitePlayer(){
        TextView spielerName = findViewById(R.id.SpielerInvite);
        String spieler = spielerName.getText().toString();
        if(spieler.trim().isEmpty()){
            popUp("Gruppe", "Spielername darf nicht leer sein");
            return;
        }
        boardgameAPI.AddSpielerToSpielgruppe(spieler,spielgruppeDTO.getId(),new BasicCallback() {
            @Override
            public void onSuccess(String response) {
                popUp("Gruppe", response);
            }

            @Override
            public void onFailure(Exception e) {
                popUp("Gruppe", "Spieler konnte nicht eingeladen werden");
            }
        });
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
                    popUp("Gruppe", "Datum konnte nicht geparst werden");
                    return;
                }
                if (!datum.trim().isEmpty()) {
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
        ImageButton buttonProfil = findViewById(R.id.profil);
        ImageButton buttonHome = findViewById(R.id.home);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
    private void LoadSpieltermine(Boolean filterByDate) {
        boardgameAPI.GetSpieltermineByGruppe(filterByDate, spielgruppeDTO.getId(), new SpielterminCallback() {
            @Override
            public void onSuccess(ArrayList<SpielterminDto> spielterminDto) {
                spieltermineList = spielterminDto;
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
                Log.d("GroupActivity", "SpielgruppeByIdCallback onFailure: " + e.getMessage());
            }
        });
    }
    public void popUp(String title ,String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
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
