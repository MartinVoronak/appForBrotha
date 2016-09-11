package com.example.martin.try4;


import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    private String objectName;
    private ArrayList<String> arrayColorList;
    private ArrayList<Float> gradients;

    public Profile(String objectName, ArrayList<String> arrayList, ArrayList<Float> gradients){
        this.objectName=objectName;
        this.arrayColorList=arrayList;
        this.gradients=gradients;
    }

    public String getObjectName() {
        return objectName;
    }

    public ArrayList<String> getArrayList() {
        return arrayColorList;
    }

    public ArrayList<Float> getGradients() {
        return gradients;
    }
}
