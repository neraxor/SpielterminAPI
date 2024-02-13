package com.example.boardgameapp.Callbacks;

public interface NachrichtCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
