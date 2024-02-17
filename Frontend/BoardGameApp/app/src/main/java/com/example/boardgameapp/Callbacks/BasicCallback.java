package com.example.boardgameapp.Callbacks;

public interface BasicCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
