package com.example.boardgameapp.Callbacks;

import java.util.ArrayList;

public interface GetSpielerCallback {
    void onSuccess(ArrayList<String> response);
    void onFailure(Exception e);
}
