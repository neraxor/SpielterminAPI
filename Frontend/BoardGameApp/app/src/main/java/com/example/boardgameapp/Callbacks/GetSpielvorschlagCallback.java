package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielvorschlagDto;

import java.util.List;

public interface GetSpielvorschlagCallback {
    void onSuccess(List<SpielvorschlagDto> spielvorschlaege);
    void onFailure(Exception e);
}
