package com.example.martin.try4;


import java.util.ArrayList;

public class Profile {

    private String objectName;
    private ArrayList<String> arrayColorList;

    public Profile(String objectName, ArrayList<String> arrayList){
        this.objectName=objectName;
        this.arrayColorList=arrayList;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public ArrayList<String> getArrayList() {
        return arrayColorList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayColorList = arrayList;
    }
}
