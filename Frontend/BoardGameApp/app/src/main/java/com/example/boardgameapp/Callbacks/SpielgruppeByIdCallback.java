package com.example.boardgameapp.Callbacks;

import java.util.ArrayList;

public interface SpielgruppeByIdCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
