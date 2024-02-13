package com.example.boardgameapp.Callbacks;

public interface SpielvorschlagCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
