package com.example.boardgameapp;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CreatGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_group);
        appNavigation();
    }

    private void appNavigation(){
        ImageButton buttonHome = findViewById(R.id.create);
        ImageButton buttonProfil = findViewById(R.id.profil);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( CreatGroup.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatGroup.this, Profil.class);
                startActivity(intent);
            }
        });
    }
}