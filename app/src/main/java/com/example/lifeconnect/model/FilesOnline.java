package com.example.lifeconnect.model;

public class FilesOnline {

    private int id;
    private String name;
    private String fileToString;
    private int idUser;

    public FilesOnline(int id, String name, String fileToString, int idUser) {
        this.id = id;
        this.name = name;
        this.fileToString = fileToString;
        this.idUser = idUser;
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

    public String getFileToString() {
        return fileToString;
    }

    public void setFileToString(String fileToString) {
        this.fileToString = fileToString;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
