package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielgruppeDTO;

import okhttp3.Call;
import okhttp3.Response;

public interface CreateSpielgruppeCallback {
    void onSuccess(SpielgruppeDTO spielgruppeDTO);
    void onFailure(Exception e);
}
