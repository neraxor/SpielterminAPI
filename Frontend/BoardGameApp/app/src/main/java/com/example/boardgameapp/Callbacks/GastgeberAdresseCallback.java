package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielerDto;

public interface GastgeberAdresseCallback {
    void onSuccess(SpielerDto response);
    void onFailure(Exception e);
}

