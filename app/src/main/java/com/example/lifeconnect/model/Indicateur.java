package com.example.lifeconnect.model;

import java.io.Serializable;

public class Indicateur implements Serializable {

    private int id;
    private String name;
    private int type;
    private String description;
    private String value;
    private String objectif;

    public Indicateur(int id, String name, int type, String description, String objectif) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.objectif = objectif;
    }

    public Indicateur(int id, String name, int type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String content) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }
}
