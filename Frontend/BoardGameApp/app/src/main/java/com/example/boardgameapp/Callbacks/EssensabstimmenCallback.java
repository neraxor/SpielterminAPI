package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielgruppeDTO;

public interface EssensabstimmenCallback {
    void onSuccess(String response);
    void onFailure(Exception e);
}
