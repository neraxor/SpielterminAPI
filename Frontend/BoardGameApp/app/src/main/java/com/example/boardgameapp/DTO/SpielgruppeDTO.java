package com.example.boardgameapp.DTO;

import java.io.Serializable;

public class SpielgruppeDTO implements Serializable {
    String name;
    Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
