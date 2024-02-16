package com.example.boardgameapp.DTO;

public class SpielerAdvancedDto {
    private SpielerDto spielerDto;
    private String gruppenName;

    public SpielerDto getSpielerDto() {
        return spielerDto;
    }

    public void setSpielerDto(SpielerDto spielerDto) {
        this.spielerDto = spielerDto;
    }

    public String getGruppenName() {
        return gruppenName;
    }

    public void setGruppenName(String gruppenName) {
        this.gruppenName = gruppenName;
    }
}
