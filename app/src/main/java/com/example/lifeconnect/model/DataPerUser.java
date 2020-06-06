package com.example.lifeconnect.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPerUser implements Serializable {

    private ArrayList<Data> datas;
    private int user;

    public DataPerUser(ArrayList<Data> datas, int user) {
        this.datas = datas;
        this.user = user;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Data> datas) {
        this.datas = datas;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
