package com.example.boardgameapp.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SpielterminDto implements Serializable {
    private int id;
    private LocalDateTime termin;
    private int spielgruppeId;
    private int gastgeberId;
    private String gruppenName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTermin() {
        return termin;
    }

    public void setTermin(LocalDateTime termin) {
        this.termin = termin;
    }

    public int getSpielgruppeId() {
        return spielgruppeId;
    }

    public void setSpielgruppeId(int spielgruppeId) {
        this.spielgruppeId = spielgruppeId;
    }

    public int getGastgeberId() {
        return gastgeberId;
    }

    public void setGastgeberId(int gastgeberId) {
        this.gastgeberId = gastgeberId;
    }
    public String getGruppenName() {
        return gruppenName;
    }
    public void setGruppenName(String gruppenName) {
        this.gruppenName = gruppenName;
    }
}

