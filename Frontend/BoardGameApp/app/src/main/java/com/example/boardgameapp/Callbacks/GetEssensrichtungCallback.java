package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.Essensrichtung;

import java.util.ArrayList;

public interface GetEssensrichtungCallback {
    void onSuccess(ArrayList<Essensrichtung>  response);
    void onFailure(Exception e);
}
