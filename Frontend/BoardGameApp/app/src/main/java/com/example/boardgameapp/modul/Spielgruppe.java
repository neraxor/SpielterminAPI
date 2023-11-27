package com.example.boardgameapp.modul;

import java.util.List;

//Datenhaltungsklasse für alle Daten der Spielgruppe
public class Spielgruppe {
    private String name;
    private String id;
    private List<String> Spielvorschläge;
    private List<String> Essensvorschläge;
    private List<String> Teilnehmer;
    private String Gastgeber;
    private String GastgeberOrt;
    private String GastgeberPLZ;

    private String GastgeberStrasse;
    private String GastgeberHausnummer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSpielvorschläge() {
        return Spielvorschläge;
    }

    public void setSpielvorschläge(List<String> spielvorschläge) {
        Spielvorschläge = spielvorschläge;
    }

    public List<String> getEssensvorschläge() {
        return Essensvorschläge;
    }

    public void setEssensvorschläge(List<String> essensvorschläge) {
        Essensvorschläge = essensvorschläge;
    }

    public List<String> getTeilnehmer() {
        return Teilnehmer;
    }

    public void setTeilnehmer(List<String> teilnehmer) {
        Teilnehmer = teilnehmer;
    }

    public String getGastgeber() {
        return Gastgeber;
    }

    public void setGastgeber(String gastgeber) {
        Gastgeber = gastgeber;
    }

    public String getGastgeberOrt() {
        return GastgeberOrt;
    }

    public void setGastgeberOrt(String gastgeberOrt) {
        GastgeberOrt = gastgeberOrt;
    }

    public String getGastgeberPLZ() {
        return GastgeberPLZ;
    }

    public void setGastgeberPLZ(String gastgeberPLZ) {
        GastgeberPLZ = gastgeberPLZ;
    }

    public String getGastgeberStrasse() {
        return GastgeberStrasse;
    }

    public void setGastgeberStrasse(String gastgeberStrasse) {
        GastgeberStrasse = gastgeberStrasse;
    }

    public String getGastgeberHausnummer() {
        return GastgeberHausnummer;
    }

    public void setGastgeberHausnummer(String gastgeberHausnummer) {
        GastgeberHausnummer = gastgeberHausnummer;
    }
// to be continued

}
