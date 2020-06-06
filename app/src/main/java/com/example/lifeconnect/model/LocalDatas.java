package com.example.lifeconnect.model;

import java.util.ArrayList;

public class LocalDatas {

    private ArrayList<DataPerUser> dataPerUsers;

    public LocalDatas(ArrayList<DataPerUser> dataPerUsers) {
        this.dataPerUsers = dataPerUsers;
    }

    public ArrayList<DataPerUser> getDataPerUsers() {
        return dataPerUsers;
    }

    public void setDataPerUsers(ArrayList<DataPerUser> dataPerUsers) {
        this.dataPerUsers = dataPerUsers;
    }
}
