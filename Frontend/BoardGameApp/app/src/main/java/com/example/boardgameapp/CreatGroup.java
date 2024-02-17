package com.example.boardgameapp;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.boardgameapp.Callbacks.CreateSpielgruppeCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielgruppeDTO;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreatGroup extends AppCompatActivity {

    private BoardgameAPI boardgameAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_group);
        Calendar todaysDate = Calendar.getInstance();
        boardgameAPI = new BoardgameAPI(this);
        final  DatePicker groupMeeting = findViewById(R.id.groupDate);
        groupMeeting.updateDate(todaysDate.get(Calendar.YEAR), todaysDate.get(Calendar.MONTH) , todaysDate.get(Calendar.DAY_OF_MONTH));
        appNavigation();
        final Button joinGroupButton = findViewById(R.id.joinGroup);
        final Button createGroupButton = findViewById(R.id.createGroup);

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup(groupMeeting);
            }
        });
    }

    private void joinGroup() {
        final EditText joinGroup = findViewById(R.id.joinGroupText);
        String groupName = joinGroup.getText().toString();
        //TODO add Database access
        Intent intent = new Intent(CreatGroup.this, MainActivity.class);
        startActivity(intent);
    }

    private void createGroup(DatePicker groupMeeting) {
        EditText groupName = findViewById(R.id.groupName);
        EditText groupRotation = findViewById(R.id.groupRotation);

        String name = groupName.getText().toString();
        String rotation = groupRotation.getText().toString();
        int year = groupMeeting.getYear();
        int month = groupMeeting.getMonth();
        int day = groupMeeting.getDayOfMonth();
        //TODO - add Database access
        // - creat a instance of the Group Class -> pass this in to the Database
        // - get current user and pass user into Databas or Group Class
        Calendar calendar = new GregorianCalendar(year, month, day);
        Date date = calendar.getTime();
        boardgameAPI.CreateSpielgruppe(name, new CreateSpielgruppeCallback() {
            @Override
            public void onSuccess(SpielgruppeDTO spielgruppeDTO) {
                boardgameAPI.CreateSpieltermin(spielgruppeDTO.getId(), date);
            }
            @Override
            public void onFailure(Exception e) {
                // Fehlerbehandlung Gruppe wurde nicht erstellt Benachrichtigung des Benutzers
            }
        });

        //SpielgruppeDTO spielgruppeDTO =  boardgameAPI.CreateSpielgruppe(name);
        Intent intent = new Intent(CreatGroup.this, MainActivity.class);
        startActivity(intent);
    }


    private void appNavigation(){
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonProfil = findViewById(R.id.profil);
        ImageButton buttonHome = findViewById(R.id.home);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatGroup.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatGroup.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatGroup.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
}