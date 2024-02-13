package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielgruppeDTO;
import com.example.boardgameapp.DTO.SpielterminDto;

import java.util.ArrayList;

public interface ProfilCallback {
    void onSuccess(ArrayList<SpielgruppeDTO> spielgruppeDto);
    void onFailure(Exception e);
}
