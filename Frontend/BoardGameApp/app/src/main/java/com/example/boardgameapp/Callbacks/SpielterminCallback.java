package com.example.boardgameapp.Callbacks;

import com.example.boardgameapp.DTO.SpielterminDto;

import java.util.ArrayList;

public interface SpielterminCallback {
    void onSuccess(ArrayList<SpielterminDto> spielterminDto);
    void onFailure(Exception e);
}
