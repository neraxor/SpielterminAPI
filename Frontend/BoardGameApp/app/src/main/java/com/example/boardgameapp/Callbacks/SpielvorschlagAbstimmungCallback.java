package com.example.boardgameapp.Callbacks;

public interface SpielvorschlagAbstimmungCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
