package com.example.boardgameapp.Callbacks;

public interface CreateAbendbewertungCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
