package com.example.boardgameapp.DTO;

public class SpielvorschlagDto {
    int id;
    String spielvorschlagName;
    public SpielvorschlagDto(String spiel) {
        this.spielvorschlagName = spielvorschlagName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getspielvorschlagName() {
        return spielvorschlagName;
    }

    public void setspielvorschlagName(String spielvorschlagName) {
        spielvorschlagName = spielvorschlagName;
    }
}
