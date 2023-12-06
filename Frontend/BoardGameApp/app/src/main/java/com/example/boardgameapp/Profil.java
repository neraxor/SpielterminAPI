package com.example.boardgameapp;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Profil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        appNavigation();
    }

    private void appNavigation(){
        ImageButton buttonCreate = findViewById(R.id.create);
        ImageButton buttonHome = findViewById(R.id.home);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, CreatGroup.class);
                startActivity(intent);
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}